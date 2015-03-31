package net.rrm.ehour.persistence.hibernate;

import com.google.common.collect.Lists;
import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.persistence.database.Database;
import net.rrm.ehour.persistence.database.DatabaseConfig;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {
    protected static final PathMatchingResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

    private static final Logger LOGGER = Logger.getLogger(HibernateConfiguration.class);

    private boolean isInTestMode() {
        return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
    }

    @Bean(name = "sessionFactory")
    @Autowired
    public SessionFactory getSessionFactory(DatabaseConfig databaseConfig) throws Exception {
        String databaseName = databaseConfig.databaseType.name().toLowerCase();

        Properties configProperties = EhourHomeUtil.loadDatabaseProperties(databaseName);
        LOGGER.info("Using database type: " + databaseName);

        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

        if (databaseConfig.databaseType == Database.DERBY) {
            EmbeddedConnectionPoolDataSource dataSource = createDerbyDataSource();
            sessionFactoryBean.setDataSource(dataSource);
        }

        List<Resource> mappingResources = getMappingResources(configProperties);
        sessionFactoryBean.setMappingLocations(mappingResources.toArray(new Resource[mappingResources.size()]));

        sessionFactoryBean.setPackagesToScan(getPackagesToScan());

        Properties hibernateProperties = getHibernateProperties(configProperties, databaseConfig);
        sessionFactoryBean.setHibernateProperties(hibernateProperties);

        beforeFinalizingSessionFactoryBean(sessionFactoryBean);

        sessionFactoryBean.afterPropertiesSet();

        return sessionFactoryBean.getObject();
    }

    private EmbeddedConnectionPoolDataSource createDerbyDataSource() throws SQLException {
        if (isInTestMode()) {
            EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
            dataSource.setDatabaseName("memory:ehourDb");

            return dataSource;
        } else {
            EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
            dataSource.setDatabaseName("ehourDb");
            return dataSource;
        }
    }

    private Properties getHibernateProperties(Properties configProperties, DatabaseConfig databaseConfig) {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty(AvailableSettings.DIALECT, (String) configProperties.get("hibernate.dialect"));
        hibernateProperties.setProperty(AvailableSettings.SHOW_SQL, "false");
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "hibernate-ehcache.xml");
        hibernateProperties.put(AvailableSettings.CACHE_REGION_FACTORY, EhCacheRegionFactory.class.getName());
        hibernateProperties.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        hibernateProperties.setProperty(AvailableSettings.USE_QUERY_CACHE, "true");

        addConnectionProvider(hibernateProperties, databaseConfig);

        if (databaseConfig.databaseType != Database.DERBY) {
            hibernateProperties.setProperty(AvailableSettings.URL, databaseConfig.url);
            hibernateProperties.setProperty(AvailableSettings.DRIVER, databaseConfig.driver);
            hibernateProperties.setProperty(AvailableSettings.USER, databaseConfig.username);
            hibernateProperties.setProperty(AvailableSettings.PASS, databaseConfig.password);
        }
        hibernateProperties.setProperty(AvailableSettings.AUTOCOMMIT, "false");
        hibernateProperties.setProperty("dataSource.cachePrepStmts", "true");
        hibernateProperties.setProperty("dataSource.prepStmtCacheSize", "250");
        hibernateProperties.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        hibernateProperties.setProperty("dataSource.useServerPrepStmts", "true");

        String validateSchema = (String) configProperties.get(AvailableSettings.HBM2DDL_AUTO);
        if (!"false".equalsIgnoreCase(validateSchema)) {
            hibernateProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, validateSchema);
        }

        return hibernateProperties;
    }


    protected void addConnectionProvider(Properties hibernateProperties, DatabaseConfig databaseConfig) {
        if (databaseConfig.databaseType != Database.DERBY) {
            hibernateProperties.setProperty(AvailableSettings.CONNECTION_PROVIDER, HikariConnectionProvider.class.getCanonicalName());
        }
    }

    protected void beforeFinalizingSessionFactoryBean(LocalSessionFactoryBean bean) {

    }

    protected String[] getPackagesToScan() {
        return new String[]{"net.rrm.ehour.domain"};
    }


    protected List<Resource> getMappingResources(Properties configProperties) throws IOException {
        Resource[] queryResources = RESOLVER.getResources("classpath:query/common/*.hbm.xml");
        ClassPathResource dbQueryResource = new ClassPathResource("query/" + configProperties.getProperty("reportquery.filename"));
        return Lists.asList(dbQueryResource, queryResources);
    }

    @Bean
    @Autowired
    public PlatformTransactionManager txManager(DatabaseConfig databaseConfig) {
        try {
            return new HibernateTransactionManager(getSessionFactory(databaseConfig));
        } catch (Exception e) {
            throw new RuntimeException("Screwed", e);
        }
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
