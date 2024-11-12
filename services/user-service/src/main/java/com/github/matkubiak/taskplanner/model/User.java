/*
 * Task Planner
 * Copyright (C) Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Length(min=1, max=250)
    private String email;

    @NotNull
    @Length(min=8, max=50)
    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public @Length(min = 1, max = 250) String getEmail() {
        return email;
    }

    public void setEmail(@Length(min = 1, max = 250) String email) {
        this.email = email;
    }

    public @Length(min = 8, max = 50) String getPassword() {
        return password;
    }

    public void setPassword(@Length(min = 8, max = 50) String password) {
        this.password = password;
    }
}
