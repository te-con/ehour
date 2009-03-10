/**
 * Created on Aug 22, 2007
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

package net.rrm.ehour.ui.common.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Javascript confirmation dialog
 */

public class JavaScriptConfirmation extends AttributeModifier
{
	private static final long serialVersionUID = -4438660782434392618L;

	/**
	 * 
	 * @param event
	 * @param msg
	 */
	public JavaScriptConfirmation(String event, String msg)
	{
		this(event, new Model(msg));
	}

	/**
	 * 
	 * @param event
	 * @param msg
	 */
	public JavaScriptConfirmation(String event, IModel msg)
	{
		super(event, true, msg);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.AttributeModifier#newValue(java.lang.String, java.lang.String)
	 */
	protected String newValue(final String currentValue, final String replacementValue)
	{
		String result = "if (confirm('" + replacementValue + "')) { ";
		
		if (currentValue != null)
		{
			result = result + currentValue + "; } else { return false; }";
		}
		return result;
	}
}