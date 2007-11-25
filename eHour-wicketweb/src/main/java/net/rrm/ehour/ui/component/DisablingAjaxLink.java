/**
 * Created on Nov 25, 2007
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

package net.rrm.ehour.ui.component;

import net.rrm.ehour.ui.ajax.OnClickDecorator;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 * AjaxLink which disables itself after first click
 **/

public abstract class DisablingAjaxLink extends AjaxLink
{
	public DisablingAjaxLink(String id)
	{
		super(id);
	}
	
	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator()
	{
		return new OnClickDecorator("if (this.disabled) { return false; }; this.disabled = true;", null, null,
						"this.disabled=false");
	}
}
