package net.rrm.ehour.datasource;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;

/**
 * 
 * @author thies
 * 
 */
public class DerbyDataSourceFactory
{
	public DataSource createDataSource(String databaseName)
	{
		EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
		dataSource.setDatabaseName(databaseName);
		return dataSource;
	}
}
