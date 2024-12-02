/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class EventConsumerService implements DisposableBean {

    private final static String QUEUE_NAME = "email";

    private Channel channel;

    private Connection connection;

    public EventConsumerService(@Value("${rabbitmq.username}") String username, @Value("${rabbitmq.password}") String password) throws Exception {
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

    private void onEvent(String consumerTag, Delivery delivery) throws UnsupportedEncodingException {
        String destinationAddress = new String(delivery.getBody(), "UTF-8");
        System.out.println("Sending email to: " + destinationAddress);
    }

    @Override
    public void destroy() throws Exception {
        channel.close();
        connection.close();
    }
}
