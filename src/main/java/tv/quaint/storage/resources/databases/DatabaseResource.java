package tv.quaint.storage.resources.databases;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.StorageResource;
import tv.quaint.storage.resources.databases.differentiating.SpecificConnection;
import tv.quaint.storage.resources.databases.processing.DBSchematic;
import tv.quaint.storage.resources.databases.processing.interfacing.DBColumn;
import tv.quaint.storage.resources.databases.processing.interfacing.DBDataLike;
import tv.quaint.storage.resources.databases.processing.interfacing.DBRow;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class DatabaseResource<T, D extends DBDataLike<?>, C extends DBColumn, R extends DBRow<C, D>, S extends DBSchematic<C>, M extends SpecificConnection<T, D, C, R, S>> extends StorageResource<R> {
    @Getter @Setter
    private M connection;
    @Getter @Setter
    private R row;
    @Getter @Setter
    private String table;

    public DatabaseResource(String discriminatorKey, String discriminator, String table, R row, M connection) {
        super((Class<R>) row.getClass(), discriminatorKey, discriminator);

        this.connection = connection;
        this.row = row;
        this.table = table;
    }

    public DatabaseResource(String discriminatorKey, String discriminator, String table, S schematic, M connection) {
        this(discriminatorKey, discriminator, table, connection.createRow(table, discriminatorKey, discriminator, schematic), connection);
    }

    @Override
    public String getDiscriminator() {
        return (String) super.getDiscriminator();
    }

    @Override
    public <O> O get(String key, Class<O> def) {
        try {
            return (O) this.row.getColumn(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
