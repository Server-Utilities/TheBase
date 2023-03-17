package tv.quaint.storage.resources.databases.specific;

import tv.quaint.storage.resources.databases.SQLResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.events.SQLResourceStatementEvent;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

public class SQLiteResource extends SQLResource {
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
        if (! link.startsWith("jdbc:sqlite:")) {
            link = "jdbc:sqlite:" + link;
        }
        return link;
    }

    @Override
    public void onReload() {

    }

    @Override
    public String getInsertString(String table, DatabaseValue<?>... values) {
        StringBuilder builder = new StringBuilder("INSERT OR REPLACE INTO " + table + " ( ");

        int i = 0;
        for (DatabaseValue<?> value : values) {
            DatabaseValue<?> t = fromCollectionOrArray(value.getKey(), value.getValue());
            builder.append(t.getKey());
            if (i != values.length - 1) {
                builder.append(", ");
            }
            i ++;
        }

        builder.append(" ) VALUES ( ");

        i = 0;
        for (DatabaseValue<?> value : values) {
            DatabaseValue<?> t = fromCollectionOrArray(value.getKey(), value.getValue());
            if (t.isString()) {
                builder.append("'").append(t.getValue()).append("'");
            } else {
                builder.append(t.getValue());
            }
            if (i != values.length - 1) {
                builder.append(", ");
            }
            i ++;
        }

        builder.append(" );");

        new SQLResourceStatementEvent(this, builder.toString()).fire();

        return builder.toString();
    }
}
