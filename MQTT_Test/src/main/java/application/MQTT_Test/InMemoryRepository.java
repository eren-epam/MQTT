package application.MQTT_Test;

import java.util.HashMap;
import java.util.Map;


/**
 * In-memory implementation of the Repository interface.
 * This class stores items in a local HashMap.
 */
public class InMemoryRepository implements Repository<Map<String, Object>> {
    private Map<Integer, Map<String, Object>> store = new HashMap<>(); // In-memory storage for items

    /**
     * Creates a new item and adds it to the in-memory store.
     * 
     * @param item The item to be created.
     */
    @Override
    public void create(Map<String, Object> item) {
        int id = getIdFromItem(item); // Extract the ID from the item
        store.put(id, item); // Store the item in the map
        System.out.println("Created item with ID: " + id);
    }

    /**
     * Retrieves an item from the in-memory store by its ID.
     * 
     * @param id The ID of the item to retrieve.
     * @return The item with the specified ID, or null if not found.
     */
    @Override
    public Map<String, Object> read(int id) {
        Map<String, Object> item = store.get(id); // Get the item from the map
        System.out.println("Read item with ID: " + id + " -> " + item);
        return item;
    }

    /**
     * Updates an existing item in the in-memory store.
     * 
     * @param id The ID of the item to update.
     * @param item The updated item data.
     */
    @Override
    public void update(int id, Map<String, Object> item) {
        store.put(id, item); // Replace the existing item with the updated one
        System.out.println("Updated item with ID: " + id);
    }

    /**
     * Deletes an item from the in-memory store by its ID.
     * 
     * @param id The ID of the item to delete.
     */
    @Override
    public void delete(int id) {
        store.remove(id); // Remove the item from the map
        System.out.println("Deleted item with ID: " + id);
    }

    /**
     * Extracts the ID from the given item.
     * Ensures the ID is an integer by checking and converting if necessary.
     * 
     * @param item The item from which to extract the ID.
     * @return The ID of the item as an integer.
     */
    private int getIdFromItem(Map<String, Object> item) {
        Object idObject = item.get("id"); // Get the ID from the item
        if (idObject instanceof Number) {
            return ((Number) idObject).intValue(); // Convert the ID to int
        } else {
            throw new IllegalArgumentException("ID must be a number"); // Throw exception if ID is not a number
        }
    }
}


