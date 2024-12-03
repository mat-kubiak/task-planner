/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.github.matkubiak.taskplanner.payload.EmailRequest;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    TemplateService templateService;

    private void sendHtmlEmail(String address, String subject, String body) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(address);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(msg);
    }

    private void sendTemplateEmail(
            Map<String, Object> root,
            String address,
            String templateName,
            String subject) throws TemplateException, IOException, MessagingException {

        String output = templateService.applyTemplate(root, templateName);
        sendHtmlEmail(address, subject, output);
    }

    public void sendEmail(EmailRequest event) throws MessagingException, IOException, TemplateException {
        Map<String, Object> root = new HashMap<>();
        root.put("username", event.getUsername());

        String templateName = "";
        String subject = "";

        switch (event.getType()) {
            case DEBUG -> {
                templateName = "debug.html";
                subject = "Debug Message";
            }
            default -> throw new IllegalStateException("Unexpected EmailRequest type: " + event.getType());
        }

        sendTemplateEmail(root, event.getAddress(), templateName, subject);
    }
}
