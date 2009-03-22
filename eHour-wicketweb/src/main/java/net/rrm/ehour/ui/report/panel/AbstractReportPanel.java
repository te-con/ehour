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

package net.rrm.ehour.ui.report.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *  
 **/

public abstract class AbstractReportPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private int reportWidth = 950;
	
	private int	chartWidth;
	private int	chartHeight;
	
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
		this(id, null, chartWidth, 950);
	}	
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id, int chartWidth, int reportWidth)
	{
		this(id, null, chartWidth, reportWidth);
	}	

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractReportPanel(String id, IModel model, int chartWidth, int reportWidth)
	{
		super(id, model);
		
		EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		this.reportWidth = reportWidth; 
		
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
	
	/**
	 * @return the chartWidth
	 */
	public int getChartWidth()
	{
		return chartWidth;
	}
	
	/**
	 * @return the chartHeight
	 */
	public int getChartHeight()
	{
		return chartHeight;
	}
	
	protected int getReportWidth()
	{
		return reportWidth;
	}
}
