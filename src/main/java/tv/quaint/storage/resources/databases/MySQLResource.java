package tv.quaint.storage.resources.databases;

import com.zaxxer.hikari.HikariDataSource;
import tv.quaint.storage.resources.databases.connections.SQLConnection;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;
import tv.quaint.storage.resources.databases.processing.sql.data.AbstractSQLData;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLColumn;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLDataLike;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLRow;
import tv.quaint.storage.resources.databases.processing.sql.data.defined.DefinedSQLData;

import java.util.concurrent.ConcurrentSkipListMap;

public class MySQLResource extends DatabaseResource<HikariDataSource, SQLDataLike<?>, SQLColumn, SQLRow, SQLSchematic, SQLConnection> {
    public MySQLResource(String discriminatorKey, String discriminator, String table, SQLRow row, SQLConnection connection) {
        super(discriminatorKey, discriminator, table, row, connection);
    }

    public MySQLResource(String discriminatorKey, String discriminator, String table, SQLSchematic schematic, SQLConnection connection) {
        super(discriminatorKey, discriminator, table, new SQLRow(schematic), connection);
    }

    @Override
    public void continueReloadResource() {
        setRow(getConnection().getRow(getTable(), getDiscriminatorKey(), getDiscriminator()));
    }

    @Override
    public void write(String key, Object value) {
        AbstractSQLData<?> data = DefinedSQLData.getFromType(SQLSchematic.SQLType.fromObject(value), value);
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
        getConnection().replace(getTable(), getDiscriminatorKey(), getDiscriminator(), getRow());
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
