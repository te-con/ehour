package net.rrm.ehour.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

//<!-- Session factory definition -->
//<bean id="sessionFactory"
//	class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
//	<property name="dataSource" ref="eHourDataSource" />
//
//	<property name="mappingLocations">
//		<list>
//			<value>classpath:mapping/*.hbm.xml</value>
//			<value>classpath:query/common/*.hbm.xml</value>
//			<value>classpath:query/${reportquery.filename}</value>
//		</list>
//	</property>
//	<property name="hibernateProperties">
//		<props>
//			<prop key="hibernate.dialect">${hibernate.dialect}</prop>
//			<prop key="show_sql">true</prop>
//			<prop key="use_outer_join">true</prop>
//			<prop key="hibernate.cache.provider_class">org.hibernate.cache.EhCacheProvider</prop>
//			<prop key="hibernate.cache.use_second_level_cache">true</prop>
//			<prop key="net.sf.ehcache.configurationResourceName">ehcache.xml</prop>
//			<prop key="hibernate.cache.use_query_cache">true</prop>
//			<prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</prop>
//			<!-- <prop key="hibernate.generate_statistics">true</prop>--> 
//		</props>
//	</property>
//</bean>

@Configuration
public class HibernateConfiguration
{
	@Autowired
	private DataSource dataSource;
	
	private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	@Bean(name="sessionFactory")
	public SessionFactory getSessionFactory() throws Exception
	{
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(dataSource);

		List<Resource> resources = getMappingResources();
		factoryBean.setMappingLocations(resources.toArray(new Resource[resources.size()]));
		
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
		hibernateProperties.put("show_sql", "false");
		hibernateProperties.put("use_outer_join", "true");
		hibernateProperties.put("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
		hibernateProperties.put("hibernate.cache.use_second_level_cache", "true");
		hibernateProperties.put("net.sf.ehcache.configurationResourceName", "ehcache.xml");
		hibernateProperties.put("hibernate.cache.use_query_cache", "true");
		hibernateProperties.put("hibernate.hbm2ddl.auto", "false");
		
		factoryBean.setHibernateProperties(hibernateProperties);
		factoryBean.afterPropertiesSet();
		
		return factoryBean.getObject();
	}

	private List<Resource> getMappingResources() throws IOException
	{
		List<Resource> resources = new ArrayList<Resource>();
		
		Resource[] mappingResources = resolver.getResources("classpath:mapping/*.hbm.xml");
		resources.addAll(Arrays.asList(mappingResources));
		
		Resource[] queryResources = resolver.getResources("classpath:query/common/*.hbm.xml");
		System.err.println(queryResources.length);
		resources.addAll(Arrays.asList(queryResources));

		ClassPathResource dbQueryResource = new ClassPathResource("query/report.queries_derby.hbm.xml");
		resources.add(dbQueryResource);
		return resources;
	}
	
	@Bean(name="transactionManager")
	public  HibernateTransactionManager getTransactionManager() throws Exception
	{
		HibernateTransactionManager manager = new HibernateTransactionManager(getSessionFactory());
		
		return manager;
	}
	
	public @Bean HibernateTemplate getHibernateTemplate() throws Exception
	{
		return new HibernateTemplate(getSessionFactory());
	}
}
