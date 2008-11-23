/**
 * Created on Mar 9, 2008
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

package net.rrm.ehour.ui.common.renderers;

import java.io.Serializable;

import net.rrm.ehour.domain.DomainObject;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * TODO 
 **/

public abstract class LocalizedResourceRenderer implements IChoiceRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3533972441275552509L;

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(Object obj)
	{
		String key = getResourceKey(obj);
			
		Localizer localizer = Application.get().getResourceSettings().getLocalizer();
		
		return localizer.getString(key, null);
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	protected abstract String getResourceKey(Object o);

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
	 */
	@SuppressWarnings("unchecked")
	public String getIdValue(Object object, int index)
	{
		return ((DomainObject<Serializable, Serializable>)object).getPK().toString();
	}
}
