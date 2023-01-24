package tv.quaint.storage.resources.databases.specific;

import tv.quaint.storage.resources.databases.SQLResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;

public class MySQLResource extends SQLResource {
    public MySQLResource(DatabaseConfig config) {
        super(config);
    }

    @Override
    public String getDriverName() {
        // new driver name: "com.mysql.cj.jdbc.Driver"
        // old driver name: "com.mysql.jdbc.Driver"
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String getJdbcUrl() {
        String link = getConfig().getLink();
        if (! link.startsWith("jdbc:mysql://")) {
            link = "jdbc:mysql://" + link;
        }
        return link;
    }

    @Override
    public void onReload() {

    }
}
