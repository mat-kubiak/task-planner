/*
 * Task Planner
 * Copyright (C) Mateusz Kubiak
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dto) {
        User user;
        try {
            user = authenticationService.signup(dto);
        } catch (EmailMismatchException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("User created successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO dto) {
        User user = authenticationService.authenticate(dto);
        String jwt = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setExpiresIn(jwtService.getExpirationTime());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}