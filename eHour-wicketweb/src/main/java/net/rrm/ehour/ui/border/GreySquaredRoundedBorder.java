/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.border;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;


/**
 * Rounded border with a square left top mainly used for tabs 
 **/

public class GreySquaredRoundedBorder extends Border
{
	private static final long serialVersionUID = -793890240017442386L;

	public GreySquaredRoundedBorder(String id)
	{
		this(id, null);
	}
	
	public GreySquaredRoundedBorder(String id, Integer width)
	{
		super(id);
		
		WebMarkupContainer greyFrame = new WebMarkupContainer("greyFrame");
		
		if (width != null)
		{
			greyFrame.add(new SimpleAttributeModifier("style", "width: " + width.toString() + "px"));
		}
		
		add(greyFrame);
		
		WebMarkupContainer greySquaredFrame = new WebMarkupContainer("greySquaredFrame");
		
		if (width != null)
		{
			greySquaredFrame.add(new SimpleAttributeModifier("style", "width: " + width.toString() + "px"));
		}
		
		greySquaredFrame.add(getBodyContainer());
		greyFrame.add(greySquaredFrame);
	}

}
