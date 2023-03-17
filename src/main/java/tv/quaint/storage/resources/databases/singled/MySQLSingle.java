package tv.quaint.storage.resources.databases.singled;

import tv.quaint.storage.resources.cache.CachedResource;
import tv.quaint.storage.resources.databases.SQLResource;
import tv.quaint.storage.resources.databases.specific.MySQLResource;

import java.sql.Connection;

public class MySQLSingle extends SQLSingle<MySQLResource> {
    public MySQLSingle(MySQLResource database, String table, String discriminatorKey, String discriminator, CachedResource<?> cachedResource) {
        super(database, table, discriminatorKey, discriminator, cachedResource);
    }
}
