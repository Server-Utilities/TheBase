package tv.quaint.storage.resources.databases.differentiating;

import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.processing.interfacing.DBDataLike;

public interface SpecificConnection<T> {
    /**
     * Creates a connection defined by {@link T}.
     * Note: All objects implementing this interface must have a constructor with a {@link DatabaseConfig}.
     */
    T createConnection();

    /**
     * Replaces an object in the database with a defined {@link DBDataLike}.
     * @param table The table to replace the object in.
     * @param discriminatorKey The discriminator key to use.
     * @param discriminator The discriminator to use.
     * @param key The key to replace.
     * @param to The value to replace the key with.
     * @param <D> The type of {@link DBDataLike} to use.
     */
    <D extends DBDataLike<?>> void replace(String table, String discriminatorKey, String discriminator, String key, D to);

    /**
     * Gets a defined {@link DBDataLike} by its key.
     * @param table The table to get the {@link DBDataLike} from.
     * @param discriminatorKey THhe discriminator key to use.
     * @param discriminator The discriminator to use.
     * @param key The key to use for getting the {@link DBDataLike}.
     * @param <D> The type of {@link DBDataLike} to use.
     */
    <D extends DBDataLike<?>> D get(String table, String discriminatorKey, String discriminator, String key);

    /**
     * Checks if a {@link DBDataLike} exists with the defined key on the defined object.
     * @param table The table to check the {@link DBDataLike} in.
     * @param discriminatorKey The discriminator key to use.
     * @param discriminator The discriminator to use.
     * @param key The key to check for.
     * @return Whether the {@link DBDataLike} exists or not.
     */
    boolean exists(String table, String discriminatorKey, String discriminator, String key);
}
