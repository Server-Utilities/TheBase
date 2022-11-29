package tv.quaint.storage.resources.databases;

import com.mongodb.MongoClient;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;

public abstract class MongoResource extends DatabaseResource<MongoClient> {
    @Getter @Setter
    Document document;

    public MongoResource(String discriminatorKey, String discriminator, String table, DatabaseConfig config) {
        super(MongoClient.class, discriminatorKey, discriminator, table, config);
        this.document = new Document();
    }

    @Override
    public <O> O getOrSetDefault(String key, O value) {
        if (get(key, value.getClass()) == null) {
            write(key, value);
        }

        O o = (O) get(key, value.getClass());

        return o == null ? value : o;
    }
}
