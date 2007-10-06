/**
 * Created on Sep 30, 2007
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

package net.rrm.ehour.ui.border;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.Border;

/**
 * Grey rounded border with custom title
 **/

public class CustomTitledGreyRoundedBorder extends Border
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7069112450640627111L;

	public CustomTitledGreyRoundedBorder(String id, Component title)
	{
		super(id);
		add(title);
	}

}
