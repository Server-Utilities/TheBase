package tv.quaint.storage.resources.databases.configurations;

import de.leonhard.storage.sections.FlatFileSection;
import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.StorageUtils;

public class DatabaseConfig {
    @Getter @Setter
    private StorageUtils.SupportedDatabaseType type;
    @Getter @Setter
    private String link;
    @Getter @Setter
    private String tablePrefix;

    public DatabaseConfig(StorageUtils.SupportedDatabaseType type, String link, String tablePrefix) {
        this.type = type;
        this.link = link;
        this.tablePrefix = tablePrefix;
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
        private String tablePrefix;

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
            this.tablePrefix = section.getString("table-prefix");
        }

        public Builder setType(StorageUtils.SupportedDatabaseType type) {
            this.type = type;
            return this;
        }

        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        public Builder setTablePrefix(String tablePrefix) {
            this.tablePrefix = tablePrefix;
            return this;
        }

        public DatabaseConfig build() {
            return new DatabaseConfig(type, link, tablePrefix);
        }
    }
}
