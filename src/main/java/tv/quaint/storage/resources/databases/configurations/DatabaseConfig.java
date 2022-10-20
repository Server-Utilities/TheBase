package tv.quaint.storage.resources.databases.configurations;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.StorageUtils;
import tv.quaint.storage.resources.databases.processing.MongoConnection;
import tv.quaint.storage.resources.databases.processing.MySQLConnection;

public class DatabaseConfig {
    @Getter @Setter
    String connectionUri;
    @Getter @Setter
    String database;
    @Getter @Setter
    String prefix;
    @Getter @Setter
    StorageUtils.SupportedDatabaseType type;
    @Getter @Setter
    MongoConnection mongoConnection;
    @Getter @Setter
    MySQLConnection mySQLConnection;

    public DatabaseConfig(String connectionUri, String database, String prefix, StorageUtils.SupportedDatabaseType type) {
        this.connectionUri = connectionUri;
        this.database = database;
        this.prefix = prefix;
        this.type = type;
    }

    public MongoConnection mongoConnection() {
        if (this.mongoConnection == null) {
            this.mongoConnection = new MongoConnection(this.connectionUri, this.database, this.prefix);
        }
        return this.mongoConnection;
    }

    public MySQLConnection mySQLConnection() {
        if (this.mySQLConnection == null) {
            this.mySQLConnection = new MySQLConnection(this.connectionUri, this.database, this.prefix);
        }
        return this.mySQLConnection;
    }
}
