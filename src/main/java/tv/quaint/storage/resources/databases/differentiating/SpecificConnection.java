package tv.quaint.storage.resources.databases.differentiating;

import org.bson.Document;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.processing.DBSchematic;
import tv.quaint.storage.resources.databases.processing.interfacing.DBColumn;
import tv.quaint.storage.resources.databases.processing.interfacing.DBDataLike;
import tv.quaint.storage.resources.databases.processing.interfacing.DBRow;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoRow;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public interface SpecificConnection<T, D extends DBDataLike<?>, C extends DBColumn, R extends DBRow<C, D>, S extends DBSchematic<C>> {
    /**
     * Creates a connection defined by {@link T}.
     * Note: All objects implementing this interface must have a constructor with a {@link DatabaseConfig}.
     */
    T createConnection();

    /**
     * Replaces an object in the database with a defined {@link DBDataLike}.
     *
     * @param table            The table to replace the object in.
     * @param discriminatorKey The discriminator key to use.
     * @param discriminator    The discriminator to use.
     * @param key              The key to replace.
     * @param to               The value to replace the key with.
     */
    void replace(String table, String discriminatorKey, String discriminator, String key, D to);

    /**
     * Gets a defined {@link DBDataLike} by its key.
     *
     * @param table            The table to get the {@link DBDataLike} from.
     * @param discriminatorKey THhe discriminator key to use.
     * @param discriminator    The discriminator to use.
     * @param key              The key to use for getting the {@link DBDataLike}.
     */
    D get(String table, String discriminatorKey, String discriminator, String key);

    /**
     * Checks if a {@link DBDataLike} exists with the defined key on the defined object.
     *
     * @param table            The table to check the {@link DBDataLike} in.
     * @param discriminatorKey The discriminator key to use.
     * @param discriminator    The discriminator to use.
     * @param key              The key to check for.
     * @return Whether the {@link DBDataLike} exists or not.
     */
    boolean exists(String table, String discriminatorKey, String discriminator, String key);

    /**
     * Gets a row as a(n) {@link R} using a table name.
     *
     * @param table            The table to get the row from.
     * @param discriminatorKey The discriminator key to use.
     * @param discriminator    The discriminator to use.
     * @return The row as the {@link R}.
     */
    R getRow(String table, String discriminatorKey, String discriminator);


    /**
     * Creates a row as a(n) {@link R} using a table name.
     *
     * @param table            The table to create the row on.
     * @param discriminatorKey The discriminator key to use.
     * @param discriminator    The discriminator to use.
     * @param schematic The schematic to use for the new row.
     * @return The row as the {@link R}.
     */
     R createRow(String table, String discriminatorKey, String discriminator, S schematic);

    /**
     * Creates a row as a(n) {@link R} using a table name.
     *
     * @param table            The table to create the row on.
     * @param discriminatorKey The discriminator key to use.
     * @param discriminator    The discriminator to use.
     * @param row              The row to use as the parent.
     * @return The row as the {@link R}.
     */
    R createRow(String table, String discriminatorKey, String discriminator, R row);
}
