package tv.quaint.storage.resources.databases.connections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import tv.quaint.storage.StorageUtils;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.differentiating.SQLSpecific;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;
import tv.quaint.storage.resources.databases.processing.sql.data.AbstractSQLData;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLColumn;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLDataLike;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLRow;
import tv.quaint.storage.resources.databases.processing.sql.data.defined.DefinedSQLData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SQLConnection implements SQLSpecific {
    @Getter
    private final StorageUtils.SupportedSQLType type;

    @Getter @Setter
    private DatabaseConfig config;

    public SQLConnection(DatabaseConfig config, StorageUtils.SupportedSQLType type) {
        this.type = type;
        this.config = config;
        if (type.equals(StorageUtils.SupportedSQLType.SQLITE)) ensureSqliteFileExists();
    }

    /**
     * If the database is SQLite, this will create a new .db file in the specified directory if it doesn't exist.
     */
    public void ensureSqliteFileExists() {
        if (! type.equals(StorageUtils.SupportedSQLType.SQLITE)) return;

        String link = config.getLink();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(link);
            Statement statement = connection.createStatement();
            statement.execute("SELECT 1");
//            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + config.getDatabase() + ";");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * A function for executing an update on the database.
     * @param execution The execution to execute.
     */
    public void executeUpdate(String execution) {
        try (HikariDataSource dataSource = this.createConnection()) {
            dataSource.getConnection().createStatement().executeUpdate(execution);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A function for executing a query on the database.
     * @param query The query to execute.
     */
    public ResultSet executeQuery(String query) {
        try (HikariDataSource dataSource = this.createConnection()) {
            return dataSource.getConnection().createStatement().executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void replace(String table, String discriminatorKey, String discriminator, String replacement) {
        executeUpdate("UPDATE " + table + " SET " + replacement + " WHERE " + discriminatorKey + " = '" + discriminator + "'");
    }

    @Override
    public void replace(String table, String discriminatorKey, String discriminator, SQLRow replacement) {
        replace(table, discriminatorKey, discriminator, replacement.toSqlStringForReplace());
    }

    @Override
    public void delete(String table, String discriminatorKey, String discriminator) {
        executeUpdate("DELETE FROM " + table + " WHERE " + discriminatorKey + " = '" + discriminator + "'");
    }

    @Override
    public boolean exists(String table, String discriminatorKey, String discriminator) {
        try (ResultSet resultSet = executeQuery("SELECT * FROM " + table + " WHERE " + discriminatorKey + " = '" + discriminator + "'")) {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ResultSet pull(String table, String discriminatorKey, String discriminator) {
        return executeQuery("SELECT * FROM " + table + " WHERE " + discriminatorKey + " = '" + discriminator + "'");
    }

    @Override
    public SQLRow getRow(String table, String discriminatorKey, String discriminator) {
        try {
            ResultSet set = pull(table, discriminatorKey, discriminator);
            ConcurrentSkipListMap<SQLColumn, AbstractSQLData<?>> columns = new ConcurrentSkipListMap<>();
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
                Object object = set.getObject(i);
                columns.put(
                        new SQLColumn(set.getMetaData().getColumnName(i), SQLSchematic.SQLType.fromObject(object), i),
                        DefinedSQLData.getFromType(SQLSchematic.SQLType.fromObject(object), object)
                );
            }
            SQLColumn[] columnArray = new SQLColumn[columns.size()];
            AbstractSQLData<?>[] data = new AbstractSQLData<?>[columns.size()];
            AtomicInteger atomicInteger = new AtomicInteger(0);
            columns.forEach((column, abstractSQLData) -> {
                int i = atomicInteger.get();
                columnArray[i] = column;
                data[i] = abstractSQLData;
                atomicInteger.incrementAndGet();
            });

            set.close();
            return new SQLRow(table, columnArray, data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SQLRow createRow(String table, String discriminatorKey, String discriminator, SQLSchematic schematic) {
        executeUpdate(schematic.getCreateTableQuery());

        return getRow(table, discriminatorKey, discriminator);
    }

    @Override
    public SQLRow createRow(String table, String discriminatorKey, String discriminator, SQLRow row) {
        return createRow(table, discriminatorKey, discriminator, row.getSQLSchematic(true));
    }

    @Override
    public void createTable(SQLSchematic schematic) {
        executeUpdate(schematic.getCreateTableQuery());
    }

    @Override
    public String getAsInsert(String table, ConcurrentSkipListMap<String, SQLDataLike<?>> data) {
        StringBuilder builder = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder values = new StringBuilder("VALUES (");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        data.forEach((k, v) -> {
            builder.append(k);
            if (v.getType().equals(SQLSchematic.SQLType.VARCHAR)) {
                values.append("'").append(v.getData()).append("'");
            } else {
                values.append(v.getData());
            }
            if (atomicInteger.get() != data.size() - 1) {
                builder.append(", ");
                values.append(", ");
            }
            atomicInteger.incrementAndGet();
        });
        builder.append(") ").append(values).append(")");
        return builder.toString();
    }

    @Override
    public HikariDataSource createConnection() {
        HikariConfig hikariConfig = new HikariConfig();
        switch (type) {
            case MYSQL:
                String mysql = config.getLink();

                mysql = mysql.replace("{{user}}", config.getUsername());
                mysql = mysql.replace("{{pass}}", config.getPassword());
                mysql = mysql.replace("{{host}}", config.getHost());
                mysql = mysql.replace("{{port}}", String.valueOf(config.getPort()));
                mysql = mysql.replace("{{options}}", config.getOptions());
                mysql = mysql.replace("{{database}}", config.getDatabase());
                mysql = mysql.replace("{{table_prefix}}", config.getTablePrefix());

                hikariConfig.setJdbcUrl(mysql);
                hikariConfig.setUsername(getConfig().getUsername());
                hikariConfig.setPassword(getConfig().getPassword());
                hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
                hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
                hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                hikariConfig.addDataSourceProperty("allowMultiQueries", "true");
                return new HikariDataSource(hikariConfig);
            case SQLITE:
                String sqlite = config.getLink();

                sqlite = sqlite.replace("{{user}}", config.getUsername());
                sqlite = sqlite.replace("{{pass}}", config.getPassword());
                sqlite = sqlite.replace("{{host}}", config.getHost());
                sqlite = sqlite.replace("{{port}}", String.valueOf(config.getPort()));
                sqlite = sqlite.replace("{{options}}", config.getOptions());
                sqlite = sqlite.replace("{{database}}", config.getDatabase());
                sqlite = sqlite.replace("{{table_prefix}}", config.getTablePrefix());

                hikariConfig.setJdbcUrl(sqlite);
                hikariConfig.setUsername(getConfig().getUsername());
                hikariConfig.setPassword(getConfig().getPassword());
                hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
                hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
                hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                hikariConfig.addDataSourceProperty("allowMultiQueries", "true");
                return new HikariDataSource(hikariConfig);
            default:
                return null;
        }
    }

    @Override
    public void replace(String table, String discriminatorKey, String discriminator, String key, SQLDataLike<?> to) {
        StringBuilder builder = new StringBuilder(key + " = ");
        if (to instanceof DefinedSQLData.SQLVarcharData) {
            builder.append("'").append(to.getData()).append("'");
        } else {
            builder.append(to.getData());
        }

        replace(table, discriminatorKey, discriminator, builder.toString());
    }

    @Override
    public SQLDataLike<?> get(String table, String discriminatorKey, String discriminator, String key) {
        try {
            ResultSet set = pull(table, discriminatorKey, discriminator);
            if (set.getString(key) != null) {
                return DefinedSQLData.getFromType(SQLSchematic.SQLType.fromObject(set.getObject(key)), set.getObject(key));
            }
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean exists(String table, String discriminatorKey, String discriminator, String key) {
        try {
            ResultSet set = pull(table, discriminatorKey, discriminator);
            if (set.getString(key) != null) {
                return true;
            }
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
