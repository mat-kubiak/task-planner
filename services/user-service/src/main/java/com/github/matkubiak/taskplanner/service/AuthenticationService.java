/*
 * Task Planner
 * Copyright (C) Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.github.matkubiak.taskplanner.model.*;
import com.github.matkubiak.taskplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void signup(RegisterDTO input) throws EmailMismatchException {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new EmailMismatchException("This email is already registered!");
        }

        User user = new User();
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        userRepository.save(user);
    }

    public User authenticate(LoginDTO input) throws AuthenticationException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
            );
        } catch (AuthenticationException e) { // bypasses ExceptionTranslationFilter which intercepts the exception
            throw e;
        }

        return userRepository.findByEmail(input.getEmail()).get();
    }
}