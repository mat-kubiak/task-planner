/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.controller;

import com.github.matkubiak.taskplanner.model.UserNotFoundException;
import com.github.matkubiak.taskplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/health")
    public ResponseEntity<Object> healthCheck() {
        return new ResponseEntity<>("Service is up and running!", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Object> getUserDetails(@RequestHeader("X-Subject") Long userId) {
        try {
            return new ResponseEntity<>(userService.getUserDetails(userId), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Account not found. The account appears to be missing or has been deleted.", HttpStatus.NOT_FOUND);
        }
    }

}
