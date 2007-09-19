/**
 * Created on Aug 19, 2007
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

package net.rrm.ehour.ui.page.admin;

import net.rrm.ehour.ui.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.model.AdminBackingBean;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base admin page template with 2 tabs, add & edit
 * TODO need refactor, remove those Base methods
 **/

@SuppressWarnings({"unchecked", "serial"})
public abstract class BaseTabbedAdminPage extends BaseAdminPage
{
	private	AddEditTabbedPanel	tabbedPanel;
	
	/**
	 * 
	 * @param pageTitle
	 * @param addTabTitle
	 * @param editTabTitle
	 */
	public BaseTabbedAdminPage(ResourceModel pageTitle,
								ResourceModel addTabTitle,
								ResourceModel editTabTitle)
	{
		super(pageTitle, null);
		
		tabbedPanel = new AddEditTabbedPanel("tabs", addTabTitle, editTabTitle)
		{
			@Override
			protected Panel getAddPanel(String panelId)
			{
				return getBaseAddPanel(panelId);
			}

			@Override
			protected Panel getEditPanel(String panelId)
			{
				return getBaseEditPanel(panelId);
			}

			@Override
			protected AdminBackingBean getNewAddBackingBean()
			{
				return getNewAddBaseBackingBean();
			}

			@Override
			protected AdminBackingBean getNewEditBackingBean()
			{
				return getNewEditBaseBackingBean();
			}
			
		};
		
		add(tabbedPanel);
	}

	/**
	 * Get the backing bean for the add panel
	 * @return
	 */
	protected abstract AdminBackingBean getNewAddBaseBackingBean();
	
	/**
	 * Get the backing bean for the edit panel
	 * @return
	 */
	protected abstract AdminBackingBean getNewEditBaseBackingBean();
	
	/**
	 * Get the panel for the add tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getBaseAddPanel(String panelId);
	
	/**
	 * Get the panel for the edit tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getBaseEditPanel(String panelId);

	/**
	 * @return the tabbedPanel
	 */
	public AddEditTabbedPanel getTabbedPanel()
	{
		return tabbedPanel;
	}	
	
}
