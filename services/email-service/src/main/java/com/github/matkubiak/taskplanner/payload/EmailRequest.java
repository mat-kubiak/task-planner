/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.payload;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailRequest {

    private EmailType type;

    private String address;

    private String username;

    public enum EmailType {}

    public static EmailRequest fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        return new EmailRequest()
                .setType(jsonObject.getEnum(EmailType.class, "type"))
                .setAddress(jsonObject.getString("address"))
                .setUsername(jsonObject.getString("username"));
    }

    public EmailType getType() {
        return type;
    }

    public EmailRequest setType(EmailType type) {
        this.type = type;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public EmailRequest setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public EmailRequest setUsername(String username) {
        this.username = username;
        return this;
    }
}
