/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.messaging;

import com.github.matkubiak.taskplanner.payload.EmailRequest;
import com.github.matkubiak.taskplanner.service.EmailSenderService;
import com.rabbitmq.client.*;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.json.JSONException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class EventConsumer implements DisposableBean {

    private final EmailSenderService emailSenderService;

    private final static String QUEUE_NAME = "email";

    private Channel channel;

    private Connection connection;

    public EventConsumer(
            @Value("${rabbitmq.username}") String username,
            @Value("${rabbitmq.password}") String password,
            EmailSenderService emailSenderService) throws Exception {

        this.emailSenderService = emailSenderService;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setUsername(username);
        factory.setPassword(password);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            System.err.println("RabbitMQ connection failed: " + e);
        }

        DeliverCallback deliverCallback = this::onEvent;

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    private void onEvent(String consumerTag, Delivery delivery) {
        String payload = new String(delivery.getBody(), StandardCharsets.UTF_8);

        EmailRequest emailRequest;
        try {
            emailRequest = EmailRequest.fromJson(payload);
        } catch (JSONException e) {
            System.out.printf("Parsing email request failed: %s\n", e);
            return;
        }

        try {
            emailSenderService.sendEmail(emailRequest);
            System.out.printf("Sent %s email to: %s\n", emailRequest.getType(), emailRequest.getAddress());
        } catch (MessagingException | IOException | TemplateException e) {
            System.out.printf("Sent email failed: %s\n", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        channel.close();
        connection.close();
    }
}
