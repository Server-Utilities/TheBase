package tv.quaint.storage.resources.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.ResultSet;

public abstract class SQLResource extends DatabaseResource<Connection> {
    @Getter @Setter
    private HikariDataSource dataSource;

    public SQLResource(String discriminatorKey, Object discriminator) {
        super(Connection.class, discriminatorKey, discriminator);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public abstract String getCreateTablesString();

    public abstract String getCheckExistsString();

    public abstract String getDeleteString();

    public void create() {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(getCreateTablesString()).execute();
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
    public void delete() {
        execute(getDeleteString());
    }

    @Override
    public boolean exists() {
        try (ResultSet resultSet = query(getCheckExistsString())) {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
