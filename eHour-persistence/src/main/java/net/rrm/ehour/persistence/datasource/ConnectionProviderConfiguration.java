package net.rrm.ehour.persistence.datasource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class ConnectionProviderConfiguration {
    static final String CP_C3P0 = "c3p0";
    static final String CP_HIKARI = "hikari";

    @Value("${ehour.database.cp:c3p0}")
    String connectionPool;

    @Autowired
    private DatabaseConfig databaseConfig;

    @Bean
    public ConnectionProvider createConnectionProvider() throws IOException, PropertyVetoException, URISyntaxException {
        switch (connectionPool.toLowerCase()) {
            case  CP_C3P0:
            default:
                return new C3p0ConfiguredConnectionProvider(databaseConfig);

            case CP_HIKARI:
                return new HikariConnectionProvider(databaseConfig);

        }
    }
//
//    public SupportedDatabases getDatabaseType() {
//        return SupportedDatabases.valueOf(databaseType.toUpperCase());
//    }
}
