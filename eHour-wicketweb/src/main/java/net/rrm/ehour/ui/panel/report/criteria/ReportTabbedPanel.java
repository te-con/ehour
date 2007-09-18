/**
 * Created on Sep 18, 2007
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

package net.rrm.ehour.ui.panel.report.criteria;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;

/**
 * Ajax tabbed report panel
 **/

public class ReportTabbedPanel extends AjaxTabbedPanel
{
	private static final long serialVersionUID = 5957279200970383021L;

	/**
	 * Default constructor
	 * @param id
	 * @param tabs
	 */
	public ReportTabbedPanel(String id, List<AbstractTab> tabs)
	{
		super(id, tabs);
		// TODO Auto-generated constructor stub
	}

}
