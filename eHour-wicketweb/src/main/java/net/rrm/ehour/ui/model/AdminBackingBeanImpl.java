/**
 * Created on Feb 24, 2008
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

package net.rrm.ehour.ui.model;

/**
 * Admin backing bean impl
 **/

public class AdminBackingBeanImpl implements AdminBackingBean
{
	private static final long serialVersionUID = 1L;
	
	private String	serverMessage;

	/**
	 * @return the serverMessage
	 */
	public String getServerMessage()
	{
		return serverMessage;
	}

	/**
	 * @param serverMessage the serverMessage to set
	 */
	public void setServerMessage(String serverMessage)
	{
		this.serverMessage = serverMessage;
	}
}
