package net.rrm.ehour.dao;

import java.io.File;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:context-props_test.xml",
									"classpath:context-datasource_test.xml",
									"classpath:context-dbconnectivity.xml", 
									"classpath:test-context-scanner-repository.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=true)
public abstract class AbstractAnnotationDaoTest
{
	@Autowired
	private DataSource	eHourDataSource;
	
	@Before
	public void setUpDatabase() throws Exception
	{
		Connection con = DataSourceUtils.getConnection(eHourDataSource);
		IDatabaseConnection connection = new DatabaseConnection(con);

		IDataSet dataSet = new FlatXmlDataSet(new File("src/test/resources/test-dataset.xml"));
		
		try
		{
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} finally
		{
			connection.close();
		}
	}
}
