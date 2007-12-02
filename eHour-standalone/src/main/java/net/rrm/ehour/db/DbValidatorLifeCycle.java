/**
 * Created on Oct 16, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import org.apache.log4j.Logger;
import org.mortbay.component.AbstractLifeCycle;

/**
 * Db validator life cycle
 **/

public class DbValidatorLifeCycle extends AbstractLifeCycle
{
	private String	xmlPath;
	private String	requiredVersion;
	private final static Logger logger = Logger.getLogger(DbValidatorLifeCycle.class);
	
	/**
	 * 
	 */
    public void doStart() throws Exception
    {
    	logger.info("Verifying installed datamodel. Required version is v" + requiredVersion);
		
		DataSource dataSource = getDataSource();
		
		new DbValidator().checkDatabaseState(dataSource, requiredVersion, xmlPath);
    }

    
    /**
     * Get datasource
     * @return
     * @throws NamingException
     */
    private DataSource getDataSource() throws NamingException
    {
		InitialContext initialContext = new InitialContext();
		DataSource datasource = (DataSource)initialContext.lookup("jdbc/eHourDS");
    
		return datasource;
    }


	/**
	 * @param requiredVersion the requiredVersion to set
	 */
	public void setRequiredVersion(String requiredVersion)
	{
		this.requiredVersion = requiredVersion;
	}


	/**
	 * @param xmlPath the xmlPath to set
	 */
	public void setXmlPath(String xmlPath)
	{
		this.xmlPath = xmlPath;
	}
}
