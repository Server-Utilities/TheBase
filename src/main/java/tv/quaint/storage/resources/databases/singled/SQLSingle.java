package tv.quaint.storage.resources.databases.singled;

import com.mongodb.MongoClient;
import tv.quaint.storage.resources.databases.SQLResource;
import tv.quaint.storage.resources.databases.specific.MongoResource;

import java.sql.Connection;

public class SQLSingle<R extends SQLResource> extends DatabaseSingle<Connection, R> {
    public SQLSingle(R database, String table, String discriminatorKey, String discriminator) {
        super(database, table, discriminatorKey, discriminator);
    }
}
