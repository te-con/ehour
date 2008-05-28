/**
 * Created on Jan 14, 2008
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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.config.PageConfig;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Base Panel
 **/

public abstract class BasePanel extends Panel
{
	private static final long serialVersionUID = 8437967307064528806L;

	public BasePanel(String id)
	{
		super(id);
	}

	public BasePanel(String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Get page config
	 * @return
	 */
	protected PageConfig getPageConfig()
	{
		return ((EhourWebApplication)getApplication()).getPageConfig();
	}
	
	/**
	 * 
	 * @return
	 */
	protected EhourWebSession getEhourWebSession()
	{
		return ((EhourWebSession)this.getSession());
	}
	
	/**
	 * Get this user's config
	 * @return
	 */
	protected EhourConfig getConfig()
	{
		return EhourWebSession.getSession().getEhourConfig();
	}	
}
