/**
 * Created on Nov 3, 2008
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

package net.rrm.ehour.domain;

/**
 * Auditable action (which CRUD)
 **/

public enum AuditActionType
{
	CREATE("CREATE"), 
	READ("READ"), 
	UPDATE("UPDATE"), 
	DELETE("DELETE"),
	LOGIN("LOGIN"),
	LOGOUT("LOGOUT");

	private String value;

	AuditActionType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public static AuditActionType fromString(String value)
	{
		if (CREATE.getValue().equalsIgnoreCase(value))
		{
			return CREATE;
		} else if (READ.getValue().equalsIgnoreCase(value))
		{
			return READ;
		} else if (UPDATE.getValue().equalsIgnoreCase(value))
		{
			return UPDATE;
		} else
		{
			return DELETE;
		}
	}
}
