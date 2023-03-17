package tv.quaint.storage.resources.databases.singled;

import tv.quaint.storage.resources.cache.CachedResource;
import tv.quaint.storage.resources.databases.specific.SQLiteResource;

import java.sql.Connection;

public class SQLiteSingle extends SQLSingle<SQLiteResource> {
    public SQLiteSingle(SQLiteResource database, String table, String discriminatorKey, String discriminator, CachedResource<?> cachedResource) {
        super(database, table, discriminatorKey, discriminator, cachedResource);
    }
}
