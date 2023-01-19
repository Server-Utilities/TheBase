package tv.quaint.storage.resources.databases.specific;

import tv.quaint.storage.resources.databases.SQLResource;

public abstract class SQLiteResource extends SQLResource {
    public SQLiteResource(String discriminatorKey, Object discriminator) {
        super(discriminatorKey, discriminator);
    }

    @Override
    public String getDriverName() {
        return "org.sqlite.JDBC";
    }
}
