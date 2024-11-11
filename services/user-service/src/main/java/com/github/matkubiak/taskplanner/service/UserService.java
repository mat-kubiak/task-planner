/*
 * Task Planner
 * Copyright (C) Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

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

}
