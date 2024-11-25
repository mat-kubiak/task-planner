/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.controller;

import com.github.matkubiak.taskplanner.model.*;
import com.github.matkubiak.taskplanner.service.AuthenticationService;
import com.github.matkubiak.taskplanner.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/health")
    public ResponseEntity<Object> healthCheck() {
        return new ResponseEntity<>("Service is up and running!", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dto) {
        try {
            authenticationService.signup(dto);
        } catch (EmailMismatchException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("User created successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LoginDTO dto) {
        User user;
        try {
            user = authenticationService.authenticate(dto);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        String jwt = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setExpiresIn(jwtService.getExpirationTime());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}