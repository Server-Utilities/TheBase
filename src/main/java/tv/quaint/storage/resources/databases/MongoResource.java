package tv.quaint.storage.resources.databases;

import com.mongodb.MongoClient;
import tv.quaint.storage.resources.databases.connections.MongoConnection;
import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;
import tv.quaint.storage.resources.databases.processing.mongo.data.AbstractMongoData;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoColumn;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoDataLike;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoRow;
import tv.quaint.storage.resources.databases.processing.mongo.data.defined.DefinedMongoData;

import java.util.concurrent.ConcurrentSkipListMap;

public class MongoResource extends DatabaseResource<MongoClient, MongoDataLike<?>, MongoColumn, MongoRow, MongoConnection> {
    public MongoResource(String discriminatorKey, String discriminator, String table, MongoRow row, MongoConnection connection) {
        super(discriminatorKey, discriminator, table, row, connection);
    }

    public MongoResource(String discriminatorKey, String discriminator, String table, ConcurrentSkipListMap<String, MongoDataLike<?>> schematic, MongoConnection connection) {
        super(discriminatorKey, discriminator, table, schematic, connection);
    }

    @Override
    public void continueReloadResource() {
        setRow(getConnection().getRow(getTable(), getDiscriminatorKey(), getDiscriminator()));
    }

    @Override
    public void write(String key, Object value) {
        AbstractMongoData<?> data = DefinedMongoData.getFromType(MongoSchematic.MongoType.fromObject(value), value);
        if (data == null) return;

        getConnection().replace(getTable(), getDiscriminatorKey(), getDiscriminator(), key, data);
    }

    @Override
    public <O> O getOrSetDefault(String key, O value) {
        if (get(key, value.getClass()) == null) {
            write(key, value);
        }

        O o = (O) get(key, value.getClass());

        return o == null ? value : o;
    }

    @Override
    public void push() {
        getConnection().replace(getTable(), getDiscriminatorKey(), getDiscriminator(), getRow().asDocument());
    }

    @Override
    public void delete() {
        getConnection().delete(getTable(), getDiscriminatorKey(), getDiscriminator());
    }

    @Override
    public boolean exists() {
        return getConnection().exists(getTable(), getDiscriminatorKey(), getDiscriminator());
    }
}
