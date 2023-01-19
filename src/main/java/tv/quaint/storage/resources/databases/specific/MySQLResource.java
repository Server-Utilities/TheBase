package tv.quaint.storage.resources.databases.specific;

import tv.quaint.storage.resources.databases.SQLResource;

public abstract class MySQLResource extends SQLResource {
    public MySQLResource(String discriminatorKey, Object discriminator) {
        super(discriminatorKey, discriminator);
    }

    @Override
    public String getDriverName() {
        return "com.mysql.cj.jdbc.Driver"; // new driver name: "com.mysql.cj.jdbc.Driver"
    }
}
