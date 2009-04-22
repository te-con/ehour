/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *  
 **/

public abstract class AbstractReportPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private WebGeo webWidth= WebGeo.W_CONTENT_WIDE;
	
	private WebGeo	chartWidth;
	private WebGeo	chartHeight;
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id)
	{
		this(id, WebGeo.NOT_DEFINED);
	}
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id, WebGeo chartWidth)
	{
		this(id, null, chartWidth, WebGeo.W_CONTENT_WIDE);
	}	
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id, WebGeo chartWidth, WebGeo reportWidth)
	{
		this(id, null, chartWidth, reportWidth);
	}	

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractReportPanel(String id, IModel model, WebGeo chartWidth, WebGeo webWidth)
	{
		super(id, model);
		
		EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		this.webWidth = webWidth; 
		
		if (chartWidth == WebGeo.NOT_DEFINED)
		{
			this.chartWidth = !config.isShowTurnover() ? WebGeo.W_CHART_WIDE : WebGeo.W_CHART_SMALL;
		}
		else
		{
			this.chartWidth = chartWidth;
		}
		
		chartHeight = WebGeo.H_CHART;		
	}
	
	public WebGeo getChartWidth()
	{
		return chartWidth;
	}
	
	public WebGeo getChartHeight()
	{
		return chartHeight;
	}
	
	protected WebGeo getReportWidth()
	{
		return webWidth;
	}
}
