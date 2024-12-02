/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendTextEmail(String address, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(address);
        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);
    }

    public void sendHtmlEmail(String address, String subject, String body) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(address);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(msg);
    }
}
