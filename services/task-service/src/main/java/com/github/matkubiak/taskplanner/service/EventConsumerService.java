/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventConsumerService implements DisposableBean {

    @Autowired
    TaskService taskService;

    private final static String QUEUE_NAME = "deleted_userid";

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

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            Long userId = Long.valueOf(new String(delivery.getBody(), "UTF-8"));

            try {
                taskService.deleteAllUserTasks(userId);
                System.out.println("Successfully deleted tasks of user: " + userId);
            } catch (Exception e) {
                System.err.println("There was a problem with deleting tasks of user " + userId + ": " + e);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    @Override
    public void destroy() throws Exception {
        channel.close();
        connection.close();
    }
}


