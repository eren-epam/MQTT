package application.MQTT_Test;

/**
 * Generic interface for a repository that supports basic CRUD operations.
 * 
 * @param <T> The type of items the repository will manage.
 */
public interface Repository<T> {
    /**
     * Creates a new item in the repository.
     * 
     * @param item The item to be created.
     */
    void create(T item);

    /**
     * Reads an item from the repository by its ID.
     * 
     * @param id The ID of the item to read.
     * @return The item with the specified ID.
     */
    T read(int id);

    /**
     * Updates an existing item in the repository by its ID.
     * 
     * @param id The ID of the item to update.
     * @param item The updated item data.
     */
    void update(int id, T item);

    /**
     * Deletes an item from the repository by its ID.
     * 
     * @param id The ID of the item to delete.
     */
    void delete(int id);
}

