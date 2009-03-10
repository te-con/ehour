/**
 * Created on Aug 31, 2007
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

package net.rrm.ehour.ui.common.ajax;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.model.IModel;

/**
 * Display demo decorator
 **/

public class DemoDecorator implements IAjaxCallDecorator
{
	private static final long serialVersionUID = 1432993030793501257L;

	private IModel	msgModel;
	
	public DemoDecorator(IModel msgModel)
	{
		this.msgModel = msgModel;
	}
	
	public CharSequence decorateOnFailureScript(CharSequence script)
	{
		return "alert('" + msgModel.getObject() + "');";
	}

	public CharSequence decorateOnSuccessScript(CharSequence script)
	{
		return "alert('" +  msgModel.getObject() + "');";
	}

	public CharSequence decorateScript(CharSequence script)
	{
		return "alert('" +  msgModel.getObject() + "');";
	}
}
