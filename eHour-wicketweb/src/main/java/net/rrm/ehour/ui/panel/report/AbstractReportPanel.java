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
import org.apache.wicket.model.IModel;

/**
 *  
 **/

public abstract class AbstractReportPanel extends Panel
{
	protected final static int REPORT_WIDTH = 950;
	
	protected EhourConfig	config;
	protected int		chartWidth;
	protected int		chartHeight;
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id)
	{
		this(id, -1);
	}
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id, int chartWidth)
	{
		this(id, null, chartWidth);
	}	

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractReportPanel(String id, IModel model, int chartWidth)
	{
		super(id, model);
		
		config = ((EhourWebSession)getSession()).getEhourConfig();

		if (chartWidth == -1)
		{
			this.chartWidth = !config.isShowTurnover() ? 700 : 350;
		}
		else
		{
			this.chartWidth = chartWidth;
		}
		
		chartHeight = 200;		
	}
}
