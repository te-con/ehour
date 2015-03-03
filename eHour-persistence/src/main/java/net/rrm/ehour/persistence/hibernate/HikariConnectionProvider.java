package net.rrm.ehour.persistence.hibernate;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.zaxxer.hikari.hibernate.HikariConfigurationUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Connection provider for Hibernate 4.3.
 *
 * @author Brett Wooldridge, Luca Burgazzoli
 */
public class HikariConnectionProvider implements ConnectionProvider, Configurable, Stoppable {
    private static final long serialVersionUID = -9131625057941275711L;

    private static final Logger LOGGER = LoggerFactory.getLogger(HikariConnectionProvider.class);

    /**
     * HikariCP configuration.
     */
    private HikariConfig hcfg;

    /**
     * HikariCP data source.
     */
    private HikariDataSource hds;

    // *************************************************************************
    //
    // *************************************************************************

    /**
     * c-tor
     */
    public HikariConnectionProvider() {
        this.hcfg = null;
        this.hds = null;
    }

    private HikariDataSource getDataSource() {
        return hds;
    }

    // *************************************************************************
    // Configurable
    // *************************************************************************

    @SuppressWarnings("rawtypes")
    @Override
    public void configure(Map props) throws HibernateException {
        try {
            LOGGER.debug("Configuring HikariCP");

            this.hcfg = HikariConfigurationUtil.loadConfiguration(props);
            this.hds = new HikariDataSource(this.hcfg);

        } catch (Exception e) {
            throw new HibernateException(e);
        }

        LOGGER.debug("HikariCP Configured");
    }

    // *************************************************************************
    // ConnectionProvider
    // *************************************************************************

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        if (this.hds != null) {
            conn = this.hds.getConnection();
        }

        return conn;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean isUnwrappableAs(Class unwrapType) {
        return ConnectionProvider.class.equals(unwrapType) || HikariConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    @SuppressWarnings( {"unchecked"})
    public <T> T unwrap(Class<T> unwrapType) {
        if ( ConnectionProvider.class.equals( unwrapType ) ||
                HikariConnectionProvider.class.isAssignableFrom( unwrapType ) ) {
            return (T) this;
        }
        else if ( DataSource.class.isAssignableFrom( unwrapType ) ) {
            return (T) getDataSource();
        }
        else {
            throw new UnknownUnwrapTypeException( unwrapType );
        }
    }

    // *************************************************************************
    // Stoppable
    // *************************************************************************

    @Override
    public void stop() {
        this.hds.close();
    }
}
