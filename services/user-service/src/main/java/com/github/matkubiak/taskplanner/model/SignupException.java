/*
 * Task Planner
 * Copyright (C) Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.model;

public class SignupException extends RuntimeException {
    public SignupException(String message) {
        super(message);
    }
}
