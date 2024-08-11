package application.MQTT_Test;

import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Main class to start the MQTT CRUD processor application.
 */
public class AppTest {
    public static void main(String[] args) {
        // Create an in-memory repository for storing items
        Repository<Map<String, Object>> repository = new InMemoryRepository();
        // Create a new MQTT CRUD processor with the repository
        MqttCrudProcessor processor = new MqttCrudProcessor(repository);

        try {
            // Start the MQTT CRUD processor
            processor.start();

            // Add a shutdown hook to ensure the client disconnects gracefully on exit
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                processor.stop(); // Stop the processor
                System.out.println("Application stopped");
            }));
        } catch (MqttException e) {
            System.err.println("Failed to start MQTT processor: " + e.getMessage()); // Log startup errors
            e.printStackTrace();
        }
    }
}