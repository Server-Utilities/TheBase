package tv.quaint.storage.resources.databases.singled;

import tv.quaint.storage.resources.databases.specific.SQLiteResource;

import java.sql.Connection;

public class SQLiteSingle extends DatabaseSingle<Connection, SQLiteResource> {
    public SQLiteSingle(SQLiteResource database, String table, String discriminatorKey, String discriminator) {
        super(database, table, discriminatorKey, discriminator);
    }
}
