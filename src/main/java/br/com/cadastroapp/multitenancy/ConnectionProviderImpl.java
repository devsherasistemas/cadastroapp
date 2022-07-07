package br.com.cadastroapp.multitenancy;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;


import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

/**
 * @author GHajba
 *
 */

public class ConnectionProviderImpl implements ConnectionProvider {

    private static final long serialVersionUID = 1697402608114701057L;
    private final MysqlDataSource basicDataSource = new MysqlDataSource();

    public ConnectionProviderImpl(String database) {

        // these should come from a property file
        this.basicDataSource.setUrl("jdbc:mysql://localhost:3306/");
        this.basicDataSource.setUser("root");
        this.basicDataSource.setPassword("265969sm");
    }

    @Override
    public boolean isUnwrappableAs(Class arg0) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) {
        return null;
    }

    @Override
    public void closeConnection(Connection arg0) throws SQLException {
        arg0.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.basicDataSource.getConnection();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }
}
