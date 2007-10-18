/**
 * Created on Jun 16, 2007
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

package net.rrm.ehour.db;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.log4j.Logger;

/**
 * Derby database accessor methods
 **/

public class DbValidator 
{
	private String	xmlPath;
	
	private final static Logger logger = Logger.getLogger(DbValidator.class);
	private final static String DDL_XML = "ddl-ehour-0.7.xml";
	private final static String DML_XML = "dml-ehour-0.7.xml";
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.db.DbAccessor#checkDatabaseState(javax.sql.DataSource, java.lang.String)
	 */
	public void checkDatabaseState(DataSource dataSource, String version, String xmlPath)
	{
		boolean databaseInState = false;
		String 	currentVersion = null;
		
		this.xmlPath = xmlPath;
		
		try
		{
			Connection connection = dataSource.getConnection();
			
			currentVersion = getCurrentVersion(connection);
			
			databaseInState = (currentVersion != null) ? currentVersion.equalsIgnoreCase(version) : false;
		} catch (SQLException e)
		{
			logger.info("Could not determine datamodel's version, recreating..");
			currentVersion = "unknown";
			logger.debug(e);
			
			createDatamodel(dataSource);
			
			databaseInState = true;
		}

		if (!databaseInState)
		{
			logger.info("Datamodel of version " + currentVersion + " found. Upgrading to " + version);
			
			upgradeDatamodel(dataSource);
		}
		else
		{
			logger.info("Datamodel is the requested version.");
		}			
	}
	
	/**
	 * 
	 */
	private void upgradeDatamodel(DataSource dataSource)
	{
		// TODO implement in 0.8
	}

	/**
	 * Create datamodel and fill with initial data
	 */
	private void createDatamodel(DataSource dataSource)
	{
		Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);
		Database ddlModel = readModelFromXML(xmlPath + DDL_XML);
		platform.createTables(ddlModel, false, false);

		insertData(platform, ddlModel);
	}

	/**
	 * Insert data
	 * @param platform
	 * @param model
	 */
	private void insertData(Platform platform, Database model)
	{
		DatabaseDataIO	dataIO = new DatabaseDataIO();
		
		DataReader dataReader = dataIO.getConfiguredDataReader(platform, model);
        dataReader.getSink().start();
        
        dataIO.writeDataToDatabase(dataReader, new File(xmlPath + DML_XML).getAbsolutePath());
	}
	
	private Database readModelFromXML(String fileName)
	{
	    return new DatabaseIO().read(fileName);
	}	
	
	/**
	 * Get current version of database state
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private String getCurrentVersion(Connection connection) throws SQLException
	{
		String version = null;
		
		Statement stmt = connection.createStatement();
		ResultSet results = stmt.executeQuery("SELECT config_value FROM CONFIGURATION WHERE config_key = 'version'");
		
		if (results.next())
		{
			version = results.getString("config_value");
		}
		
		return version;
		
	}

	/**
	 * @return the xmlPath
	 */
	public String getXmlPath()
	{
		return xmlPath;
	}

	/**
	 * @param xmlPath the xmlPath to set
	 */
	public void setXmlPath(String xmlPath)
	{
		this.xmlPath = xmlPath;
	}
}
