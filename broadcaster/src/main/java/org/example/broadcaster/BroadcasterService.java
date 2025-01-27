package org.example.broadcaster;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class BroadcasterService {

    private final Connection natsConnection;

    @Value("${EXTERNAL_SERVICE_URL}")
    private String externalServiceUrl;

    //Constructor which will run the service
    public BroadcasterService(@Value("${NATS_URL}") String natsUrl) {
        try {
            this.natsConnection = Nats.connect(natsUrl);
            subscribeToNats();
        } catch (Exception e) {
            System.err.println("Failed to connect to NATS: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void subscribeToNats() {
        Dispatcher dispatcher = natsConnection.createDispatcher((msg) -> {
            String message = new String(msg.getData(), StandardCharsets.UTF_8);
            sendToExternalService(message);
        });

        dispatcher.subscribe("todos");
    }

    private void sendToExternalService(String message) {
        try {
            URL url = new URL(externalServiceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String payload = String.format("{\"user\": \"bot\", \"message\": \"%s\"}", message);

            connection.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Message sent successfully: " + message);
            } else {
                System.err.println("Failed to send message. Response code: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error while sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanup() {
        if (natsConnection != null) {
            try {
                natsConnection.close();
                System.out.println("NATS connection closed.");
            } catch (Exception e) {
                System.err.println("Failed to close NATS connection: " + e.getMessage());
            }
        }
    }
}
