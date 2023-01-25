package tv.quaint.storage.resources.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class SQLResource extends DatabaseResource<Connection> {
    @Getter @Setter
    private HikariDataSource dataSource;

    public SQLResource(DatabaseConfig config) {
        super(Connection.class, config);
    }

    @Override
    protected Connection connect() {
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(String table, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        DatabaseValue<?>[] valueArray = values.toArray(new DatabaseValue<?>[0]);
        create(table, valueArray);
    }

    @Override
    public void insert(String table, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        DatabaseValue<?>[] valueArray = values.toArray(new DatabaseValue<?>[0]);
        insert(table, valueArray);
    }

    @Override
    public <O> O get(String table, String keyKey, String key, Class<O> def) {
        try (Connection connection = getCachedConnection()) {
            ResultSet resultSet = connection.prepareStatement(getSelectString(table, keyKey, key)).executeQuery();
            if (resultSet.next()) {
                return (O) resultSet.getObject(keyKey);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <O> O getOrSetDefault(String table, String keyKey, String key, O value) {
        O o = get(table, keyKey, key, (Class<O>) value.getClass());
        if (o == null) {
            updateSingle(table, keyKey, key, value);
            return value;
        } else {
            return o;
        }
    }

    public HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();

        config.setPoolName("TheBase - " + this.getClass().getSimpleName());
        config.setDriverClassName(getDriverName());
        config.setJdbcUrl(getJdbcUrl());
        config.setConnectionTestQuery("SELECT 1");
        config.setMaxLifetime(60000);
        config.setIdleTimeout(45000);
        config.setMaximumPoolSize(50);

        try {
            return new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract String getDriverName();

    public abstract String getJdbcUrl();

    public String getCreateTablesString(String table, DatabaseValue<?>... values) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table + " ( ");

        for (DatabaseValue<?> value : values) {
            builder.append(value.getKey()).append(" ").append(value.getSQLType()).append(", ");
        }

        builder.append("PRIMARY KEY (").append(values[0].getKey()).append(" ) );");

        return builder.toString();
    }

    public String getInsertString(String table, DatabaseValue<?>... values) {
        StringBuilder builder = new StringBuilder("INSERT INTO " + table + " ( ");

        int i = 0;
        for (DatabaseValue<?> value : values) {
            builder.append(value.getKey());
            if (i != values.length - 1) {
                builder.append(", ");
            }
            i ++;
        }

        builder.append(" ) VALUES ( ");

        i = 0;
        for (DatabaseValue<?> value : values) {
            builder.append("?");
            if (i != values.length - 1) {
                builder.append(", ");
            }
            i ++;
        }

        builder.append(" );");

        return builder.toString();
    }

    public String getCheckExistsString(String table, String keyKey, String key) {
        return "SELECT * FROM " + table + " WHERE " + keyKey + " = '" + key + "'";
    }

    public String getDeleteString(String table, String keyKey, String key) {
        return "DELETE FROM " + table + " WHERE " + keyKey + " = '" + key + "';";
    }

    public String getSelectString(String table, String keyKey, String key) {
        return "SELECT * FROM " + table + " WHERE " + keyKey + " = '" + key + "';";
    }

    public String getUpdateString(String table, String keyKey, String key, DatabaseValue<?> value) {
        String s = "";

        if (value.isString()) {
            s = "'" + value.getValue() + "'";
        } else {
            s = value.getValue().toString();
        }

        return "UPDATE " + table + " SET " + value.getKey() + " = " + s + " WHERE " + keyKey + " = '" + key + "';";
    }

    public void create(String table, DatabaseValue<?>... values) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getCreateTablesString(table, values)).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String table, DatabaseValue<?>... values) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getInsertString(table, values)).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute(String query) {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        try (Connection connection = getConnection()) {
            return connection.createStatement().executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String table, String keyKey, String key) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getDeleteString(table, keyKey, key)).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String table) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement("DROP TABLE " + table).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String table, String keyKey, String key) {
        try (ResultSet resultSet = query(getCheckExistsString(table, keyKey, key))) {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean exists(String table) {
        try (ResultSet resultSet = query("SELECT * FROM " + table)) {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <V> void updateSingle(String table, String keyKey, String key, V value) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getUpdateString(table, keyKey, key, new DatabaseValue<>(keyKey, value))).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <V> void updateMultiple(String table, String keyKey, String key, ConcurrentSkipListMap<String, V> values) {
        try (Connection connection = getConnection()) {
            for (Map.Entry<String, V> entry : values.entrySet()) {
                connection.prepareStatement(getUpdateString(table, keyKey, key, new DatabaseValue<>(entry.getKey(), entry.getValue()))).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
