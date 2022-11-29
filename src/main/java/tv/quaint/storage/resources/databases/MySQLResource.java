package tv.quaint.storage.resources.databases;

import com.zaxxer.hikari.HikariDataSource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;

public abstract class MySQLResource extends DatabaseResource<HikariDataSource> {
    public MySQLResource(String discriminatorKey, String discriminator, String table, DatabaseConfig config) {
        super(HikariDataSource.class, discriminatorKey, discriminator, table, config);
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
