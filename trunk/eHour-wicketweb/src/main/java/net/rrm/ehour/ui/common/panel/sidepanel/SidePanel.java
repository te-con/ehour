/**
 * Created on May 21, 2007
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

package net.rrm.ehour.ui.common.panel.sidepanel;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.IModel;

/**
 * Blue navigation side panel
 * TODO convert to border
 **/

public abstract class SidePanel extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4573739445050690761L;


	/**
	 * 
	 * @param id
	 */
	public SidePanel(String id)
	{
		this(id, null);
	}

	public SidePanel(String id, IModel model)
	{
		super(id, model);
		add(new StyleSheetReference("navSidePanelStyle", navSidePanelStyle()));
	}

	
	/**
	 * Create a style
	 * 
	 * @return a style
	 */
	protected ResourceReference navSidePanelStyle()
	{
		return new CompressedResourceReference(SidePanel.class, "style/sidePanel.css");
	}		
}
