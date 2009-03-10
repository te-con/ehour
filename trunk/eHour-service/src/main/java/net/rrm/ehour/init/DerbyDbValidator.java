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

package net.rrm.ehour.init;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.beanutils.DynaBean;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Derby database accessor methods
 **/

public class DerbyDbValidator implements ApplicationListener, ResourceLoaderAware  
{
	private String		ddlFile;
	private String		dmlFile;
	private DataSource	dataSource;
	private	String		version;
	private ResourceLoader	resourceLoader;
	
	private enum DdlType {NONE, CREATE_TABLE, ALTER_TABLE};
	
	private final static Logger logger = Logger.getLogger(DerbyDbValidator.class);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(ApplicationEvent event)
	{
		if (event instanceof ContextClosedEvent)
		{
			logger.info("Application shutting down, shutting down database");
			((EmbeddedDataSource)dataSource).setShutdownDatabase("shutdown");	
		}
	}	
	
	/**
	 * 
	 * @param dataSource
	 * @param version
	 * @param xmlPath
	 */
	public void checkDatabaseState()
	{
		boolean databaseInState = false;
		String 	currentVersion = null;
		DdlType ddlType = DdlType.CREATE_TABLE;
		
		logger.info("Verifying datamodel version. Minimum version: " + version);

		try
		{
			((EmbeddedDataSource)dataSource).setCreateDatabase("create");

			Connection connection = dataSource.getConnection();
			
			currentVersion = getCurrentVersion(connection);
			
			databaseInState = (currentVersion != null) ? currentVersion.equalsIgnoreCase(version) : false;
			
			if (databaseInState)
			{
				ddlType = DdlType.NONE;
				logger.info("Datamodel is the required version.");
			}
			else
			{
				logger.info("Datamodel of version " + currentVersion + " found. Upgrading to " + version);

				ddlType = DdlType.ALTER_TABLE;
			}
			
			
		} catch (SQLException e)
		{
			ddlType = DdlType.CREATE_TABLE;
			logger.info("Could not determine datamodel's version, recreating..");
		}
		finally
		{
			((EmbeddedDataSource)dataSource).setCreateDatabase("");
		}
		
		if (ddlType != DdlType.NONE)
		{
			try
			{
				createOrAlterDatamodel(dataSource, ddlType);
			} catch (Exception e)
			{
				logger.error("Failed to create or upgrade datamodel", e);
			}
		}
	}

	/**
	 * Create datamodel and fill with initial data
	 * @throws IOException 
	 * @throws DdlUtilsException 
	 */
	private void createOrAlterDatamodel(DataSource dataSource, DdlType ddlType) throws DdlUtilsException, IOException
	{
		Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);
		
		Resource resource = this.resourceLoader.getResource(ddlFile);

		DatabaseIO reader = new DatabaseIO();
		reader.setValidateXml(false);
		reader.setUseInternalDtd(true); 
		
		Database ddlModel = reader.read(new InputStreamReader(resource.getInputStream()));
		
		if (ddlType == DdlType.CREATE_TABLE)
		{
			platform.createTables(ddlModel, false, false);
			insertData(platform, ddlModel, dmlFile);
		}
		else
		{
			platform.alterTables(ddlModel, false);
			updateVersion(dataSource, ddlModel, platform);
		}
	}

	/**
	 * Insert data
	 * @param platform
	 * @param model
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws DdlUtilsException 
	 */
	private void insertData(Platform platform, Database model, String dmlFile) throws DdlUtilsException, FileNotFoundException, IOException
	{
		DatabaseDataIO	dataIO = new DatabaseDataIO();
		
		DataReader dataReader = dataIO.getConfiguredDataReader(platform, model);
		
        dataReader.getSink().start();
        
        Resource resource = this.resourceLoader.getResource(dmlFile);
        
        dataIO.writeDataToDatabase(dataReader, new InputStreamReader(resource.getInputStream()));
        
        logger.info("Data inserted");
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
		
		
		results.close();
		stmt.close();
		
		return version;
	}

	/**
	 * 
	 * @param dataSource
	 * @param database
	 * @param platform
	 */
	private void updateVersion(DataSource dataSource, Database database, Platform platform)
	{
		DynaBean configuration = database.createDynaBeanFor("CONFIGURATION", false);
		configuration.set("config_key", "version");
		platform.delete(database, configuration);

		configuration.set("config_value", version);

		platform.insert(database, configuration);
	}
	
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
	 */
	public void setResourceLoader(ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader; 
	}

	/**
	 * @param ddlFile the ddlFile to set
	 */
	public void setDdlFile(String ddlFile)
	{
		this.ddlFile = ddlFile;
	}

	public void setDmlFile(String dmlFile)
	{
		this.dmlFile = dmlFile;
	}
}
