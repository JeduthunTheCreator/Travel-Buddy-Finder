package com.example.myrestservice;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Send {

    private final static String QUEUE_NAME = "task_queue";
    private static final Logger logger = LoggerFactory.getLogger(Send.class);

    public static void sendMessage(String message) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            logger.info(" [x] Sent '{}'", message);
        } catch (IOException | TimeoutException e) {
            logger.error("Failed to send message due to: {}", e.getMessage(), e);
            // Implement retry logic or other recovery mechanism here
        }
    }

    public static void main(String[] args) {
        try {
            Send.sendMessage("User has performed an action");
        } catch (Exception e) {
            logger.error("Exception in main: {}", e.getMessage(), e);
        }
    }
}
