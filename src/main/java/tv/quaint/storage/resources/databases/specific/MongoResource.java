package tv.quaint.storage.resources.databases.specific;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;

public abstract class MongoResource extends DatabaseResource<MongoClient> {
    public MongoResource(DatabaseConfig config) {
        super(MongoClient.class, config);
    }

    public MongoClientURI getMongoClientURI() {
        return new MongoClientURI(getConfig().getLink());
    }

    public MongoClient connect() {
        return new MongoClient(getMongoClientURI());
    }

    public MongoDatabase getDatabase() {
        return getCachedConnection().getDatabase(getMongoClientURI().getDatabase());
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }
}
