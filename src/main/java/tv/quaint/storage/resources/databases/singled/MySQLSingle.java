package tv.quaint.storage.resources.databases.singled;

import tv.quaint.storage.resources.databases.SQLResource;
import tv.quaint.storage.resources.databases.specific.MySQLResource;

import java.sql.Connection;

public class MySQLSingle extends DatabaseSingle<Connection, MySQLResource> {
    public MySQLSingle(MySQLResource database, String table, String discriminatorKey, String discriminator) {
        super(database, table, discriminatorKey, discriminator);
    }
}
