/**
 * Created on Feb 4, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.report.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class ReportChartForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4650287107802113182L;
	private	String	key;
	private	int		chartWidth;
	private	int		chartHeight;
	private	String	reportName;
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		chartWidth = 0;
		chartHeight = 0;
	
	
	}
	/**
	 * @return the chartHeight
	 */
	public int getChartHeight()
	{
		return chartHeight;
	}
	/**
	 * @param chartHeight the chartHeight to set
	 */
	public void setChartHeight(int charHeight)
	{
		this.chartHeight = charHeight;
	}
	/**
	 * @return the chartWidth
	 */
	public int getChartWidth()
	{
		return chartWidth;
	}
	/**
	 * @param chartWidth the chartWidth to set
	 */
	public void setChartWidth(int chartWidth)
	{
		this.chartWidth = chartWidth;
	}
	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String sessionKey)
	{
		this.key = sessionKey;
	}
	/**
	 * @return the reportName
	 */
	public String getReportName()
	{
		return reportName;
	}
	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}
}
