/*
 * Task Planner
 * Copyright (C) Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.github.matkubiak.taskplanner.model.RegisterDTO;
import com.github.matkubiak.taskplanner.model.RegistrationException;
import com.github.matkubiak.taskplanner.model.User;
import com.github.matkubiak.taskplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public List<User> getUsers() {
        return repository.findAll();
    }

    public void registerUser(RegisterDTO dto) throws RegistrationException {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RegistrationException("This email is already registered");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        repository.save(user);
    }
}
