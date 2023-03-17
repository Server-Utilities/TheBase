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

        if (link.startsWith("mysql://")) {
            link = link.replace("mysql://", "jdbc:mysql://");
        }
        if (link.startsWith("jdbc:mysql://")) {
            link = link.replace("jdbc:mysql://", "");
        }

//        link = link.replace("/", "%2F");
//        link = link.replace(":", "%3A");
//        link = link.replace("@", "%40");
//        link = link.replace("(", "%28");
//        link = link.replace(")", "%29");
//        link = link.replace("[", "%5B");
//        link = link.replace("]", "%5D");
//        link = link.replace("&", "%26");
//        link = link.replace("#", "%23");
//        link = link.replace("=", "%3D");
//        link = link.replace("?", "%3F");
//        link = link.replace(" ", "%20");

        link = "jdbc:mysql://" + link;

        return link;
    }

    @Override
    public void onReload() {

    }
}
