package tv.quaint.storage.resources.databases.differentiating;

import com.zaxxer.hikari.HikariDataSource;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;
import tv.quaint.storage.resources.databases.processing.sql.data.AbstractSQLData;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLRow;

import java.sql.ResultSet;

public interface SQLSpecific extends SpecificConnection<HikariDataSource> {
    /**
     * Replaces an object in the database.
     */
    void replace(String table, String discriminatorKey, String discriminator, String replacement);

    /**
     * Replaces an object in the database.
     */
    void replace(String table, String discriminatorKey, String discriminator, SQLRow replacement);

    /**
     * Deletes an object from the database.
     */
    void delete(String table, String discriminatorKey, String discriminator);

    /**
     * Checks if an object exists in the database.
     */
    boolean exists(String table, String discriminatorKey, String discriminator);

    /**
     * Pulls an object from the database.
     */
    ResultSet pull(String table, String discriminatorKey, String discriminator);

    /**
     * Gets a document as a {@link SQLRow} using a table name.
     */
    SQLRow getRow(String table, String discriminatorKey, String discriminator);

    /**
     * Creates a table if it doesn't exist.
     */
    void createTable(SQLSchematic schematic);
}
