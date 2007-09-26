/**
 * Created on Sep 27, 2007
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

package net.rrm.ehour.ui.model;

import org.apache.wicket.model.ResourceModel;

/**
 * Resource model which exposes its resource key 
 **/

public class KeyResourceModel extends ResourceModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7897696537214857003L;
	private	String	key;
	
	/**
	 * Constructor
	 * 
	 * @param resourceKey
	 *            key of the resource this model represents
	 */
	public KeyResourceModel(String resourceKey)
	{
		super(resourceKey);
		
		this.key = resourceKey;
		
		
	}

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}
}
