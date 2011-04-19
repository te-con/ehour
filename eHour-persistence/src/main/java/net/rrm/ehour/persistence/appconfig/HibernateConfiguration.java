package net.rrm.ehour.persistence.appconfig;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.domain.DomainObjects;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
public class HibernateConfiguration
{
	@Autowired
	private DataSource dataSource;

	@Value("${ehour.database}")
	private String databaseName;

	private static final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	private static final Logger LOGGER = Logger.getLogger(HibernateConfiguration.class);

	@Bean(name="sessionFactory")
	public SessionFactory getSessionFactory() throws Exception
	{
		Properties configProperties = EhourHomeUtil.loadDatabaseProperties(databaseName);

		LOGGER.info("Using database type: " + databaseName);

        AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
		factoryBean.setDataSource(dataSource);

		List<Resource> resources = getMappingResources(configProperties);
		factoryBean.setMappingLocations(resources.toArray(new Resource[resources.size()]));
        factoryBean.setAnnotatedClasses(DomainObjects.DOMAIN_OBJECTS);

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", configProperties.get("hibernate.dialect"));
		hibernateProperties.put("show_sql", "false");
		hibernateProperties.put("use_outer_join", "true");
		hibernateProperties.put("hibernate.cache.region.factory_class", "net.sf.ehcache.hibernate.EhCacheRegionFactory");
		hibernateProperties.put("hibernate.cache.use_second_level_cache", "true");
		hibernateProperties.put("net.sf.ehcache.configurationResourceName", "ehcache.xml");
		hibernateProperties.put("hibernate.cache.use_query_cache", "true");
		hibernateProperties.put("hibernate.hbm2ddl.auto", configProperties.get("hibernate.hbm2ddl.auto"));

		factoryBean.setHibernateProperties(hibernateProperties);
		factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
	}

	private List<Resource> getMappingResources(Properties configProperties) throws IOException
	{
		List<Resource> resources = new ArrayList<Resource>();

		Resource[] queryResources = resolver.getResources("classpath:query/common/*.hbm.xml");
		resources.addAll(Arrays.asList(queryResources));

		ClassPathResource dbQueryResource = new ClassPathResource("query/" + configProperties.getProperty("reportquery.filename"));
		resources.add(dbQueryResource);
		return resources;
	}

	@Bean(name="transactionManager")
	public  HibernateTransactionManager getTransactionManager() throws Exception
	{
		return new HibernateTransactionManager(getSessionFactory());
	}

	public @Bean HibernateTemplate getHibernateTemplate() throws Exception
	{
		return new HibernateTemplate(getSessionFactory());
	}

	public @Bean JdbcTemplate getJdbcTemplate() throws Exception
	{
		return new JdbcTemplate(dataSource);
	}

	/**
	 * @param databaseName the databaseName to set
	 */
	void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
}
