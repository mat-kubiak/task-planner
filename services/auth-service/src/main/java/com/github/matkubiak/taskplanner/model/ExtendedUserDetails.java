/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface ExtendedUserDetails extends UserDetails {

    Long getId();
}
