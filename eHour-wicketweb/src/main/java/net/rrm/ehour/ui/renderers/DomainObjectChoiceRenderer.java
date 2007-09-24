/**
 * Created on Jul 10, 2007
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

package net.rrm.ehour.ui.renderers;

import net.rrm.ehour.domain.DomainObject;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Domain object renderer choice renders
 **/

public class DomainObjectChoiceRenderer implements IChoiceRenderer
{
	private static final long serialVersionUID = 9021045667533511410L;

	public Object getDisplayValue(Object object)
	{
		if  (object instanceof DomainObject<?, ?>)
		{
			return ((DomainObject<?, ?>) object).getFullName();
		}

		return object.toString();
	}

	public String getIdValue(Object object, int index)
	{
		if  (object instanceof DomainObject<?, ?>)
		{
			return ((DomainObject<?, ?>) object).getPK().toString();
		}

		return Integer.toString(index);
	}
}
