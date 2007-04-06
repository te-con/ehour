/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
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
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public abstract class BaseDAOTest extends AbstractTransactionalDataSourceSpringContextTests
{
	
	protected String[] getConfigLocations()
	{
		return new String[] { "classpath:applicationContext-dao.xml",
								"classpath:applicationContext-datasource.xml"};	
	}	
	
	protected void onSetUpInTransaction() throws Exception
	{
		setUpDatabase();
	}

	private void setUpDatabase() throws Exception
	{
		DataSource ds = jdbcTemplate.getDataSource();
		Connection con = DataSourceUtils.getConnection(ds);
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
