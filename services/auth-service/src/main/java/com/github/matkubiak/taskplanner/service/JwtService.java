/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.github.matkubiak.taskplanner.model.ExtendedUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private final SecretKey secretKey;

    public JwtService(@Value("${security.jwt.secret-key}") String secretKeyBase64) {
        byte[] decodedKey = Decoders.BASE64.decode(secretKeyBase64);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String generateToken(ExtendedUserDetails userDetails) {
        return Jwts.builder()
            .subject(String.valueOf(userDetails.getId()))
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(secretKey)
            .compact();
    }
}