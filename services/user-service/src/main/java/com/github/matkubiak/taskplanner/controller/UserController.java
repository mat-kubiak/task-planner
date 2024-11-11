/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.controller;

import com.github.matkubiak.taskplanner.model.User;
import com.github.matkubiak.taskplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping(path="/health")
    public ResponseEntity<Object> healthCheck() {
        return new ResponseEntity<>("Service is up and running!", HttpStatus.OK);
    }

    @GetMapping(path="/")
    public ResponseEntity<Object> getAllUsers() {
        List<User> users = service.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
