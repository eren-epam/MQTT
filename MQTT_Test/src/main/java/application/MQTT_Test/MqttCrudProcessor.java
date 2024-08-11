package application.MQTT_Test;

import org.eclipse.paho.client.mqttv3.*;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Processes MQTT messages and performs CRUD operations on the repository.
 */
public class MqttCrudProcessor implements MqttCallback {
    private Repository<Map<String, Object>> repository; // The repository to perform CRUD operations
    private MqttClient client; // MQTT client instance
    private Gson gson = new Gson(); // JSON parser instance

    /**
     * Constructor for MqttCrudProcessor.
     * 
     * @param repository The repository to use for CRUD operations.
     */
    public MqttCrudProcessor(Repository<Map<String, Object>> repository) {
        this.repository = repository;
    }

    /**
     * Starts the MQTT client, connects to the broker, and subscribes to the topic.
     * 
     * @throws MqttException If connection or subscription fails.
     */
    public void start() throws MqttException {
        try {
            client = new MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId()); // Create a new MQTT client
            client.setCallback(this); // Set this class as the callback handler
            
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true); // Set clean session to true for each connection
            options.setAutomaticReconnect(true); // Enable automatic reconnection
            options.setKeepAliveInterval(60);   // Set keep-alive interval to 60 seconds

            client.connect(options); // Connect to the MQTT broker
            client.subscribe("iot/data", 1);     // Subscribe to the topic with QoS 1
            
            System.out.println("Connected and subscribed to iot/data");
        } catch (MqttException e) {
            System.err.println("Failed to connect: " + e.getMessage()); // Log connection errors
            e.printStackTrace();
            throw e; // Re-throw exception
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("Connection lost: " + cause.getMessage()); // Log connection loss
        cause.printStackTrace();
        reconnect();  // Attempt to reconnect to the broker
    }

    /**
     * Attempts to reconnect to the MQTT broker with exponential backoff.
     */
    private void reconnect() {
        int attempt = 0;
        while (true) {
            try {
                Thread.sleep((long) Math.pow(2, attempt) * 1000);  // Exponential backoff
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true); // Set clean session for reconnection
                options.setAutomaticReconnect(true); // Enable automatic reconnection
                options.setKeepAliveInterval(60); // Set keep-alive interval

                client.connect(options);  // Reconnect to the broker
                client.subscribe("iot/data", 1);  // Resubscribe to the topic
                
                System.out.println("Reconnected to broker");
                break;
            } catch (MqttException | InterruptedException e) {
                System.err.println("Reconnection attempt " + (attempt + 1) + " failed: " + e.getMessage()); // Log reconnection failures
                attempt++;
                if (attempt > 5) {  // Limit the number of reconnection attempts
                    System.err.println("Max reconnection attempts reached. Exiting...");
                    break;
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message arrived from topic: " + topic); // Log arrival of a message
        String jsonPayload = new String(message.getPayload()); // Convert message payload to string
        System.out.println("Received payload: " + jsonPayload); // Log received payload

        // Parse JSON payload to a Map
        Map<String, Object> item = gson.fromJson(jsonPayload, Map.class);

        // Handle conversion of 'id' properly
        Object idObject = item.get("id");
        int id;
        if (idObject instanceof Number) {
            id = ((Number) idObject).intValue(); // Convert ID to integer
        } else {
            System.err.println("ID is not a number: " + idObject); // Log if ID is not a number
            throw new IllegalArgumentException("ID must be a number");
        }

        String operation = (String) item.get("operation"); // Get the operation type from the message
        System.out.println("Operation: " + operation); // Log the operation

        switch (operation.toUpperCase()) { // Convert operation to uppercase for consistency
            case "CREATE":
                repository.create(item); // Perform create operation
                break;
            case "READ":
                System.out.println("Read: " + repository.read(id)); // Perform read operation and log result
                break;
            case "UPDATE":
                repository.update(id, item); // Perform update operation
                break;
            case "DELETE":
                repository.delete(id); // Perform delete operation
                break;
            default:
                System.err.println("Unsupported operation: " + operation); // Log unsupported operations
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete: " + token.getMessageId()); // Log message delivery status
    }

    /**
     * Stops the MQTT client and disconnects gracefully.
     */
    public void stop() {
        if (client != null && client.isConnected()) {
            try {
                client.disconnectForcibly(); // Disconnect forcibly if needed
                System.out.println("Disconnected from broker");
            } catch (MqttException e) {
                System.err.println("Failed to disconnect: " + e.getMessage()); // Log disconnection errors
                e.printStackTrace();
            }
        }
    }
}

