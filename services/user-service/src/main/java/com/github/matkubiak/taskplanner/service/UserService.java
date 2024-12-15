/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.github.matkubiak.taskplanner.model.User;
import com.github.matkubiak.taskplanner.model.UserNotFoundException;
import com.github.matkubiak.taskplanner.model.UserResponse;
import com.github.matkubiak.taskplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventPublisherService eventPublisherService;

    public UserResponse getUserDetails(Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = userOpt.get();

        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());

        return response;
    }

    public void deleteAccount(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        eventPublisherService.publish(EventPublisherService.Queue.DELETED_USER, String.valueOf(userId));
        userRepository.deleteById(userId);

        String template = """
                          {
                            "type": "ACCOUNT_DELETED",
                            "address": "%s",
                            "username": "%s",
                          }
                          """;
        String emailEvent = String.format(template, user.getEmail(), user.getEmail());
        eventPublisherService.publish(EventPublisherService.Queue.EMAIL, emailEvent);
    }

}
