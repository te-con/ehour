package net.rrm.ehour.persistence.datasource;

import net.rrm.ehour.appconfig.ConfigUtil;
import net.rrm.ehour.persistence.dbvalidator.DerbyDbValidator;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 
 * @author thies
 * 
 */
public class DerbyDataSourceFactory
{
	/**
	 * Create datasource and validate database
	 * @param databaseName
	 * @return
	 */
	public DataSource createDataSource(String databaseName)
	{
		EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
		dataSource.setDatabaseName(databaseName);
		
		Properties properties = ConfigUtil.loadDatabaseProperties("derby");
		
		DerbyDbValidator validator = new DerbyDbValidator(properties.getProperty("ehour.db.version"), dataSource);
		validator.checkDatabaseState();
		
		return dataSource;
	}
}
