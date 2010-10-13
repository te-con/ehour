/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

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
@ContextConfiguration(locations={"classpath:applicationContext-props_test.xml", 
									"classpath:applicationContext-datasource_test.xml", 
									"classpath:applicationContext-dao.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=true)
public abstract class AbstractDaoTest
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
