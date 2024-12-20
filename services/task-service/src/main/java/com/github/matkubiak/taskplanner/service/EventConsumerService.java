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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class EventConsumerService implements DisposableBean {

    @Autowired
    TaskService taskService;

    @Autowired
    private PlatformTransactionManager transactionManager;

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

        DeliverCallback deliverCallback = this::onDelete;
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    private void onDelete(String consumerTag, Delivery delivery) throws UnsupportedEncodingException {
        Long userId = Long.valueOf(new String(delivery.getBody(), "UTF-8"));

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            taskService.deleteAllUserTasks(userId);
            System.out.println("Successfully deleted tasks of user: " + userId);
            transactionManager.commit(status);
        } catch (Exception e) {
            System.err.println("There was a problem with deleting tasks of user " + userId + ": " + e);
            transactionManager.rollback(status);
        }
    }

    @Override
    public void destroy() throws Exception {
        channel.close();
        connection.close();
    }
}
