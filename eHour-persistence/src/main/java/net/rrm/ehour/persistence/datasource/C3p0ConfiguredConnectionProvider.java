package net.rrm.ehour.persistence.datasource;

import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.cfg.AvailableSettings;

import java.util.Map;

public class C3p0ConfiguredConnectionProvider extends C3P0ConnectionProvider {
    private DatabaseConfig databaseConfig;

    public C3p0ConfiguredConnectionProvider(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public void configure(Map props) {
        props.put(AvailableSettings.DRIVER, databaseConfig.driver);
        props.put(AvailableSettings.URL, databaseConfig.url);
        props.put(AvailableSettings.USER, databaseConfig.username);
        props.put(AvailableSettings.PASS, databaseConfig.password);

        props.put(AvailableSettings.PASS, databaseConfig.password);

        super.configure(props);
    }
}
