package tv.quaint.storage.resources.databases.processing.mongo.data;

import tv.quaint.storage.resources.databases.processing.interfacing.DBDataLike;
import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;

/**
 * An interface to represent data in a SQL table.
 * Takes an {@link MongoSchematic.MongoType} to define the type of data.
 * Will be able to get the data as that type, or as a string.
 */
public interface MongoDataLike<T> extends DBDataLike<T> {

    /**
     * Get the data as a {@link MongoSchematic.MongoType}.
     *
     * @return The data as a {@link MongoSchematic.MongoType}.
     */
    @Override
    MongoSchematic.MongoType getType();
}
