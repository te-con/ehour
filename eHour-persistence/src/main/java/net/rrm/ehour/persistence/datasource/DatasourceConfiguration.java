package net.rrm.ehour.persistence.datasource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class DatasourceConfiguration {
    static final String CP_C3P0 = "c3p0";
    static final String CP_HIKARI = "hikari";

    @Value("${ehour.database}")
    String databaseType;

    @Value("${ehour.database.driver:}")
    String driver;

    @Value("${ehour.database.url:}")
    String url;

    @Value("${ehour.database.username:}")
    String username;

    @Value("${ehour.database.password:}")
    String password;

    @Value("${ehour.database.checkouttimeout:10000}")
    Integer checkoutTimeout;

    @Value("${ehour.database.cp:c3p0}")
    String connectionPool;

    @Bean
    public DataSource createDatasource() throws IOException, PropertyVetoException, URISyntaxException {
        return getDatabaseType().createDatasource(this);
    }

    public SupportedDatabases getDatabaseType() {
        return SupportedDatabases.valueOf(databaseType.toUpperCase());
    }

    @Override
    public String toString() {
        return "DatasourceConfiguration{" +
                "databaseType='" + databaseType + '\'' +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + (StringUtils.isBlank(username) ? "" : "*****") + '\'' +
                ", password='" + (StringUtils.isBlank(password) ? "" : "*****") + '\'' +
                ", checkoutTimeout=" + checkoutTimeout +
                ", connectionPool=" + connectionPool +
                '}';
    }
}
