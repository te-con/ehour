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
import net.rrm.ehour.ui.common.util.WebWidth;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *  
 **/

public abstract class AbstractReportPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private WebWidth webWidth= WebWidth.CONTENT_WIDE;
	
	private WebWidth	chartWidth;
	private WebWidth	chartHeight;
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id)
	{
		this(id, WebWidth.DEFAULT);
	}
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id, WebWidth chartWidth)
	{
		this(id, null, chartWidth, WebWidth.CONTENT_WIDE);
	}	
	
	/**
	 * 
	 * @param id
	 */
	public AbstractReportPanel(String id, WebWidth chartWidth, WebWidth reportWidth)
	{
		this(id, null, chartWidth, reportWidth);
	}	

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractReportPanel(String id, IModel model, WebWidth chartWidth, WebWidth webWidth)
	{
		super(id, model);
		
		EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		this.webWidth = webWidth; 
		
		if (chartWidth == WebWidth.DEFAULT)
		{
			this.chartWidth = !config.isShowTurnover() ? WebWidth.CHART_WIDE : WebWidth.CHART_SMALL;
		}
		else
		{
			this.chartWidth = chartWidth;
		}
		
		chartHeight = WebWidth.CHART_HEIGHT;		
	}
	
	public WebWidth getChartWidth()
	{
		return chartWidth;
	}
	
	public WebWidth getChartHeight()
	{
		return chartHeight;
	}
	
	protected WebWidth getReportWidth()
	{
		return webWidth;
	}
}
