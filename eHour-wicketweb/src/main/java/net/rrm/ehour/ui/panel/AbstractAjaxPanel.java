/**
 * Created on Mar 10, 2008
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

package net.rrm.ehour.ui.panel;

import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.AjaxEvent;

import org.apache.wicket.model.IModel;

/**
 * Base panel
 **/

public abstract class AbstractAjaxPanel extends AbstractBasePanel implements AjaxAwareContainer
{
	private static final long serialVersionUID = 5723792133447447887L;

	/**
	 * 
	 * @param id
	 */
	public AbstractAjaxPanel(String id)
	{
		super(id);
	}

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractAjaxPanel(String id, IModel model)
	{
		super(id, model);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		return true;
	}
}
