package tv.quaint.storage.resources.databases;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.StorageResource;
import tv.quaint.storage.resources.databases.differentiating.SpecificConnection;
import tv.quaint.storage.resources.databases.processing.interfacing.DBRow;

public abstract class DatabaseResource<R extends DBRow<?, ?>, M extends SpecificConnection<?>> extends StorageResource<R> {
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
