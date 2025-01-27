package org.example.todoback.services;

import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class TaskPublisher {

    private final Connection natsConnection;

    public TaskPublisher(@Value("${NATS_URL}") String natsUrl) throws Exception {
        this.natsConnection = Nats.connect(natsUrl);
    }

    public void publishTodoEvent(String message) {
        try {
            natsConnection.publish("todos", message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Published message to NATS: " + message);
        } catch (Exception e) {
            System.err.println("Failed to publish message to NATS: " + e.getMessage());
        }
    }
}
