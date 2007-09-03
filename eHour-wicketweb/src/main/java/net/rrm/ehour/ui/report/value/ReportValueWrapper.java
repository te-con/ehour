/**
 * Created on Mar 4, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.report.value;

import java.io.Serializable;


/**
 * Report value 
 **/

public class ReportValueWrapper implements Comparable<ReportValueWrapper>, Serializable
{
	private static final long serialVersionUID = 6316356835650959522L;
	protected	Integer	id;
	protected	String	name;
	
	/**
	 * 
	 * @param id
	 * @param name
	 */
	public ReportValueWrapper(Integer id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Get identifier
	 * @return
	 */
	public Integer getId()
	{
		return id;
	}
	
	/**
	 * Get row name
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 */
	public int compareTo(ReportValueWrapper o)
	{
		return getName().compareToIgnoreCase(o.getName());
	}	
}
