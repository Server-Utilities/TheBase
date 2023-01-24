package tv.quaint.storage.resources.databases.specific;

import tv.quaint.storage.resources.databases.SQLResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;

public abstract class SQLiteResource extends SQLResource {
    public SQLiteResource(DatabaseConfig config) {
        super(config);
    }

    @Override
    public String getDriverName() {
        return "org.sqlite.JDBC";
    }

    @Override
    public String getJdbcUrl() {
        String link = getConfig().getLink();
        if (! link.startsWith("jdbc:sqlite://")) {
            link = "jdbc:sqlite://" + link;
        }
        return link;
    }

    @Override
    public void onReload() {

    }
}
