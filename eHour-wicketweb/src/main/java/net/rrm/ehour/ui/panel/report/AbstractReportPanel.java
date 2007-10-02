/**
 * Created on Sep 27, 2007
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

package net.rrm.ehour.ui.panel.report;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * TODO 
 **/

public abstract class AbstractReportPanel extends Panel
{
	private EhourConfig	config;
	protected int		chartWidth;
	protected int		chartHeight;
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id)
	{
		super(id);
		
		config = ((EhourWebSession)getSession()).getEhourConfig();

		chartWidth = !config.isShowTurnover() ? 600 : 310;
		chartHeight = 200;		
	}

}
