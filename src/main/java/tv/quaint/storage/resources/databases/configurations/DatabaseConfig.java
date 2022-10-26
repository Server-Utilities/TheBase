package tv.quaint.storage.resources.databases.configurations;

import de.leonhard.storage.sections.FlatFileSection;
import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.StorageUtils;
import tv.quaint.storage.resources.databases.connections.MongoConnection;
import tv.quaint.storage.resources.databases.connections.SQLConnection;
import tv.quaint.storage.resources.databases.differentiating.SpecificConnection;

public class DatabaseConfig {
    @Getter @Setter
    private StorageUtils.SupportedDatabaseType type;
    @Getter @Setter
    private String link;
    @Getter @Setter
    private String host;
    @Getter @Setter
    private int port;
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String database;
    @Getter @Setter
    private String tablePrefix;
    @Getter @Setter
    private String options;

    public DatabaseConfig(StorageUtils.SupportedDatabaseType type, String link, String host, int port, String username, String password, String database, String tablePrefix, String options) {
        this.type = type;
        this.link = link;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.tablePrefix = tablePrefix;
        this.options = options;
    }

    /**
     * Creates a new {@link SpecificConnection} from this {@link DatabaseConfig}.
     */
    public SpecificConnection<?> createConnection() {
        switch (this.type) {
            case MONGO:
                return new MongoConnection(this);
            case MYSQL:
                return new SQLConnection(this, StorageUtils.SupportedSQLType.MYSQL);
            case SQLITE:
                return new SQLConnection(this, StorageUtils.SupportedSQLType.SQLITE);
        }

        return null;
    }

    /**
     * A class to build a {@link DatabaseConfig} object.
     */
    public static class Builder {
        @Getter
        private StorageUtils.SupportedDatabaseType type;
        @Getter
        private String link;
        @Getter
        private String host;
        @Getter
        private int port;
        @Getter
        private String username;
        @Getter
        private String password;
        @Getter
        private String database;
        @Getter
        private String tablePrefix;
        @Getter
        private String options;

        public Builder() {
        }

        public Builder(FlatFileSection section) {
            this.type = StorageUtils.SupportedDatabaseType.valueOf(section.getString("type"));
            switch (type) {
                case MONGO:
                    this.link = section.getOrDefault("link", "mongodb://{{user}}:{{pass}}@{{host}}:{{port}}/{{database}}");
                    break;
                case MYSQL:
                    this.link = section.getOrDefault("link", "jdbc:mysql://{{host}}:{{port}}/{{database}}{{options}}");
                    break;
                case SQLITE:
                    this.link = section.getOrDefault("link", "jdbc:sqlite:{{database}}.db");
                    break;
                default:
                    this.link = section.getString("link");
                    break;
            }
            this.host = section.getString("host");
            this.port = section.getInt("port");
            this.username = section.getString("username");
            this.password = section.getString("password");
            this.database = section.getString("database");
            this.tablePrefix = section.getString("table-prefix");
            this.options = section.getString("options");
        }

        public Builder setType(StorageUtils.SupportedDatabaseType type) {
            this.type = type;
            return this;
        }

        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setDatabase(String database) {
            this.database = database;
            return this;
        }

        public Builder setTablePrefix(String tablePrefix) {
            this.tablePrefix = tablePrefix;
            return this;
        }

        public Builder setOptions(String options) {
            this.options = options;
            return this;
        }

        public DatabaseConfig build() {
            return new DatabaseConfig(type, link, host, port, username, password, database, tablePrefix, options);
        }
    }
}
