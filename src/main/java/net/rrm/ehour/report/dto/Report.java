/**
 * Created on 26-jan-2007
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

package net.rrm.ehour.report.dto;

import java.util.Collection;

import net.rrm.ehour.report.criteria.ReportCriteria;

/**
 * TODO 
 **/

public class Report
{
	private	ReportCriteria	criteria;
	private int				reportType;
	
	private	Collection		reportValues;

	/**
	 * @return the criteria
	 */
	public ReportCriteria getCriteria()
	{
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(ReportCriteria criteria)
	{
		this.criteria = criteria;
	}

	/**
	 * @return the reportType
	 */
	public int getReportType()
	{
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(int reportType)
	{
		this.reportType = reportType;
	}

	/**
	 * @return the reportValues
	 */
	public Collection getReportValues()
	{
		return reportValues;
	}

	/**
	 * @param reportValues the reportValues to set
	 */
	public void setReportValues(Collection reportValues)
	{
		this.reportValues = reportValues;
	}
}
