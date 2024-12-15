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
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class EventPublisherService implements DisposableBean {

    public enum Queue {
        DELETED_USER, EMAIL;

        public String toString() {
            return this.name().toLowerCase();
        }
    }

    private final HashMap<Queue, Channel> channels = new HashMap<>();

    private Connection connection;

    public EventPublisherService(@Value("${rabbitmq.username}") String username, @Value("${rabbitmq.password}") String password) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setUsername(username);
        factory.setPassword(password);

        connection = factory.newConnection();

        for (Queue queue: Queue.values()) {
            try {
                Channel channel = connection.createChannel();
                channel.queueDeclare(queue.toString(), false, false, false, null);
                channels.put(queue, channel);
            } catch (IOException e) {
                System.err.printf("RabbitMQ connection failed for queue %s: %s\n", queue, e);
            }
        }
    }

    public void publish(Queue queue, String message) throws IOException {
        Channel channel = channels.get(queue);
        if (channel == null || !channel.isOpen()) {
            throw new IOException(String.format("Cannot publish message, channel %s is down\n", queue.toString()));
        }
        channel.basicPublish("", queue.toString(), null, message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void destroy() throws Exception {
        for (Channel channel : channels.values()) {
            try {
                if (channel != null && channel.isOpen()) {
                    channel.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing channel: " + e);
            }
        }

        if (connection != null && connection.isOpen()) {
            try {
                connection.close();
            } catch (IOException e) {
                System.err.println("Error closing RabbitMQ connection: " + e);
            }
        }
    }
}
