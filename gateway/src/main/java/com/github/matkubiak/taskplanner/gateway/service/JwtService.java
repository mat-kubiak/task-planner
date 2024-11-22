/*
 * Task Planner
 * Copyright (C) Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.gateway.service;

import com.github.matkubiak.taskplanner.gateway.JwtHttpException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class JwtService {

    private final JwtParser jwtParser;

    public JwtService(@Value("${security.jwt.secret-key}") String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
    }

    public Claims parseTokenClaims(String token) throws JwtHttpException {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new JwtHttpException("Invalid token format", HttpStatus.BAD_REQUEST);
        } catch (SignatureException | SecurityException e) {
            throw new JwtHttpException("Token verification failed", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new JwtHttpException("Token expired", HttpStatus.UNAUTHORIZED);
        }
    }
}