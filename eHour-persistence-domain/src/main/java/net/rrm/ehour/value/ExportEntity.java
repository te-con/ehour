/**
 * Created on Oct 28, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.value;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

/**
 * Export entities
 **/

public enum ExportEntity
{
	USER(EnumSet.of(ExportEntity.USER_ROLE, ExportEntity.USER_DEPARTMENT), "USERS"),
	USER_ROLE("USER_ROLE", "USER_TO_USERROLE"),
	USER_DEPARTMENT("USER_DEPARTMENT"),

	CONFIGURATION("CONFIGURATION"),
	
	CUSTOMER("CUSTOMER"),
	PROJECT(EnumSet.of(ExportEntity.CUSTOMER), "PROJECT"),
	PROJECT_ASSIGNMENT(EnumSet.of(ExportEntity.PROJECT, ExportEntity.USER), 
				"PROJECT_ASSIGNMENT", "PROJECT_ASSIGNMENT_TYPE", "CUSTOMER_FOLD_PREFERENCE"),
	
	TIMESHEET_ENTRY(EnumSet.of(ExportEntity.PROJECT_ASSIGNMENT), "TIMESHEET_COMMENT", "TIMESHEET_ENTRY"),

	MAIL("MAIL_LOG", "MAIL_LOG_ASSIGNMNENT", "MAIL_TYPE");

	private Collection<ExportEntity> dependencies;
	private Collection<String> databaseTables;
	
	ExportEntity(String... databaseTables)
	{
		this.databaseTables = Arrays.asList(databaseTables);
	}
	
	ExportEntity(Collection<ExportEntity> dependencies, String... databaseTables)
	{
		this(databaseTables);
		this.dependencies = dependencies;
	}
	
	/**
	 * @return the databaseTables
	 */
	public Collection<String> getDatabaseTables()
	{
		return databaseTables;
	}
	/**
	 * @return the dependencies
	 */
	public Collection<ExportEntity> getDependencies()
	{
		return dependencies;
	}
}
