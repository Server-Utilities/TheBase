package tv.quaint.storage.resources.databases.singled;

import com.mongodb.MongoClient;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.storage.resources.cache.CachedResource;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;
import tv.quaint.storage.resources.databases.specific.MongoResource;

public class MongoSingle extends DatabaseSingle<MongoClient, MongoResource> {
    public MongoSingle(MongoResource database, String table, String discriminatorKey, String discriminator, CachedResource<?> cachedResource) {
        super(database, table, discriminatorKey, discriminator, cachedResource);
    }
}
