package tv.quaint.storage.resources.databases.differentiating;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoColumn;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoDataLike;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoRow;

public interface MongoSpecific extends SpecificConnection<MongoClient, MongoDataLike<?>, MongoColumn, MongoRow, MongoSchematic> {
    /**
     * Replaces an object in the database.
     */
    void replace(MongoCollection<Document> collection, String discriminatorKey, String discriminator, Document replacement);

    /**
     * Replaces an object in the database using a collection name.
     */
    void replace(String collectionName, String discriminatorKey, String discriminator, Document replacement);

    /**
     * Deletes an object from the database.
     */
    void delete(MongoCollection<Document> collection, String discriminatorKey, String discriminator);

    /**
     * Deletes an object from the database using a collection name.
     */
    void delete(String collectionName, String discriminatorKey, String discriminator);

    /**
     * Checks if an object exists in the database.
     */
    boolean exists(MongoCollection<Document> collection, String discriminatorKey, String discriminator);

    /**
     * Checks if an object exists in the database using a collection name.
     */
    boolean exists(String collectionName, String discriminatorKey, String discriminator);

    /**
     * Pulls an object from the database.
     */
    Document pull(MongoCollection<Document> collection, String discriminatorKey, String discriminator);

    /**
     * Pulls an object from the database using a collection name.
     */
    Document pull(String collectionName, String discriminatorKey, String discriminator);

    /**
     * Gets a document as a {@link MongoRow}.
     */
    MongoRow getRow(MongoCollection<Document> collection, String discriminatorKey, String discriminator);

    /**
     * Gets a document as a {@link MongoRow} using said document.
     */
    MongoRow getRow(String collectionName, Document document);

    /**
     * Creates a collection if it doesn't exist.
     */
    void createCollection(MongoSchematic schematic);
}
