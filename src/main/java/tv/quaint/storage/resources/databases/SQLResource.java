package tv.quaint.storage.resources.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.cache.CachedResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.events.SQLResourceStatementEvent;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;
import tv.quaint.utils.MathUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
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
        if (getDataSource() == null || MathUtils.isDateOlderThan(getLastConnectionCreation(), 1, ChronoUnit.MINUTES)) {
            setDataSource(createDataSource());
            setLastConnectionCreation(new Date());
        }

        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean testConnection() {
        try {
            return getCachedConnection().isClosed();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public ConcurrentSkipListSet<CachedResource<Connection>> listTable(String table) {
        String selectString = "SELECT * FROM " + table;

        ConcurrentSkipListSet<CachedResource<Connection>> cachedResources = new ConcurrentSkipListSet<>();

        try (Connection connection = getConnection()) {
            ResultSet resultSet = connection.prepareStatement(selectString).executeQuery();
            cachedResources = new ConcurrentSkipListSet<>();
            while (resultSet.next()) {
                CachedResource<Connection> cachedResource = null;

                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (cachedResource == null) {
                        cachedResource = new CachedResource<>(Connection.class, resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    } else {
                        cachedResource.write(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }
                }

                if (cachedResource == null) continue;

                cachedResources.add(cachedResource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
        return cachedResources;
    }

    @Override
    public void create(String table, String primaryKey, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        DatabaseValue<?>[] valueArray = values.toArray(new DatabaseValue<?>[0]);
        create(table, primaryKey, valueArray);
    }

    @Override
    public void insert(String table, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        DatabaseValue<?>[] valueArray = values.toArray(new DatabaseValue<?>[0]);
        insert(table, valueArray);
    }

    @Override
    public <O> O get(String table, String discriminatorKey, String discriminator, String key, Class<O> def) {
        O o = null;
        try (Connection connection = getConnection()) {
            ResultSet resultSet = connection.prepareStatement(getSelectString(table, discriminatorKey, discriminator)).executeQuery();
            if (resultSet.next()) {
                Object obj = resultSet.getObject(key);
                if (obj != null) {
                    if (def.isArray()) {
                        o = (O) getArrayFromString((String) obj, def);
                    } else if (def.isAssignableFrom(Collection.class)) {
                        o = (O) getCollectionFromString((String) obj, def);
                    } else {
                        o = (O) obj;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
        return o;
    }

    @Override
    public <O> O getOrSetDefault(String table, String discriminatorKey, String discriminator, String key, O value) {
        O o = get(table, discriminatorKey, discriminator, key, (Class<O>) value.getClass());
        if (o == null) {
            updateSingle(table, discriminatorKey, discriminator, key, value);
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

    public String getCreateTablesString(String table, String primaryKey, DatabaseValue<?>... values) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table + " ( ");

        for (DatabaseValue<?> value : values) {
            builder.append(value.getKey()).append(" ").append(value.getSQLType()).append(", ");
        }

        builder.append("PRIMARY KEY ( ").append(primaryKey).append(" ) );");

        new SQLResourceStatementEvent(this, builder.toString()).fire();

        return builder.toString();
    }

    public String getInsertString(String table, DatabaseValue<?>... values) {
        StringBuilder builder = new StringBuilder("INSERT INTO " + table + " ( ");

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

    public String getCheckExistsString(String table, String discriminatorKey, String key) {
        StringBuilder builder = new StringBuilder("SELECT * FROM " + table + " WHERE " + discriminatorKey + " = '" + key + "';");

        new SQLResourceStatementEvent(this, builder.toString()).fire();

        return builder.toString();
    }

    public String getDeleteString(String table, String discriminatorKey, String key) {
        StringBuilder builder = new StringBuilder("DELETE FROM " + table + " WHERE " + discriminatorKey + " = '" + key + "';");

        new SQLResourceStatementEvent(this, builder.toString()).fire();

        return builder.toString();
    }

    public String getSelectString(String table, String discriminatorKey, String discriminator) {
        StringBuilder builder = new StringBuilder("SELECT * FROM " + table + " WHERE " + discriminatorKey + " = '" + discriminator + "';");

        new SQLResourceStatementEvent(this, builder.toString()).fire();

        return builder.toString();
    }

    public String getUpdateString(String table, String discriminatorKey, String discriminator, String key, DatabaseValue<?> value) {
        String s = "";

        if (value.isString()) {
            s = "'" + value.getValue() + "'";
        } else {
            s = value.getValue().toString();
        }
        StringBuilder builder = new StringBuilder("UPDATE " + table + " SET " + key + " = " + s + " WHERE " + discriminatorKey + " = '" + discriminator + "';");

        new SQLResourceStatementEvent(this, builder.toString()).fire();

        return builder.toString();
    }

    public String getUpdateMultipleString(String table, String discriminatorKey, String discriminator, DatabaseValue<?>... values) {
        StringBuilder builder = new StringBuilder("UPDATE " + table + " SET ");

        int i = 0;
        for (DatabaseValue<?> value : values) {
            String s = "";

            if (value.isString()) {
                s = "'" + value.getValue() + "'";
            } else {
                s = value.getValue().toString();
            }

            builder.append(value.getKey()).append(" = ").append(s);
            if (i != values.length - 1) {
                builder.append(", ");
            }
            i ++;
        }

        builder.append(" WHERE ").append(discriminatorKey).append(" = '").append(discriminator).append("';");

        new SQLResourceStatementEvent(this, builder.toString()).fire();

        return builder.toString();
    }

    public void create(String table, String primaryKey, DatabaseValue<?>... values) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getCreateTablesString(table, primaryKey, values)).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
    }

    public void insert(String table, DatabaseValue<?>... values) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getInsertString(table, values)).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
    }

    public void execute(String query) {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
    }

    public ResultSet query(String query) {
        ResultSet set = null;
        try (Connection connection = getConnection()) {
            set = connection.createStatement().executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
        return set;
    }

    @Override
    public void delete(String table, String discriminatorKey, String key) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getDeleteString(table, discriminatorKey, key)).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
    }

    @Override
    public void delete(String table) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement("DROP TABLE " + table).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
    }

    @Override
    public boolean exists(String table, String discriminatorKey, String key) {
        boolean exists = false;
        try (Connection connection = getConnection()) {
            ResultSet set = connection.prepareStatement(getCheckExistsString(table, discriminatorKey, key)).executeQuery();
            exists = set.next();
        } catch (Exception e) {
            if (! e.getMessage().contains("doesn't exist")) e.printStackTrace();
        }
        setCompleted();
        return exists;
    }

    @Override
    public boolean exists(String table) {
        boolean exists = false;
        try (Connection connection = getConnection()) {
            ResultSet set = connection.prepareStatement("SELECT * FROM " + table).executeQuery();
            exists = set.next();
        } catch (Exception e) {
            if (! e.getMessage().contains("doesn't exist")) e.printStackTrace();
        }
        setCompleted();
        return exists;
    }

    @Override
    public <V> void updateSingle(String table, String discriminatorKey, String discriminator, String key, V value) {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getUpdateString(table, discriminatorKey, discriminator, key, fromCollectionOrArray(key, value))).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
    }

    @Override
    public <V> void updateMultiple(String table, String discriminatorKey, String discriminator, ConcurrentSkipListMap<String, V> values) {
        Collection<DatabaseValue<?>> databaseValues = DatabaseResource.collectionOf(values);

        try (Connection connection = getConnection()) {
            connection.prepareStatement(getUpdateMultipleString(table, discriminatorKey, discriminator, databaseValues.toArray(new DatabaseValue[0]))).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCompleted();
    }
}
