package tv.quaint.storage.resources.databases.processing.sql.data;

import tv.quaint.storage.resources.databases.processing.interfacing.DBDataLike;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;

/**
 * An interface to represent data in a SQL table.
 * Takes an {@link SQLSchematic.SQLType} to define the type of data.
 * Will be able to get the data as that type, or as a string.
 */
public interface SQLDataLike<T> extends DBDataLike<T> {

    /**
     * Get the data as a {@link SQLSchematic.SQLType}.
     *
     * @return The data as a {@link SQLSchematic.SQLType}.
     */
    @Override
    SQLSchematic.SQLType getType();
}
