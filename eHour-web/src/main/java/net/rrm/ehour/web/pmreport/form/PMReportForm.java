/**
 * Created on Apr 11, 2007
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

package net.rrm.ehour.web.pmreport.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *  
 **/

public class PMReportForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6217637101679695022L;
	private	Integer	projectId;
	private	String	dateStart;
	private	String	dateEnd;
	private boolean infiniteStartDate;
	private boolean	infiniteEndDate;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		infiniteStartDate = false;
		infiniteEndDate = false;
	}	
	
	/**
	 * @return the projectId
	 */
	public Integer getProjectId()
	{
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	/**
	 * @return the dateEnd
	 */
	public String getDateEnd()
	{
		return dateEnd;
	}
	
	public Date getDateEndAsDate()
	{
		Date date = null;
		
		if (!infiniteEndDate && dateEnd != null)
		{
			try
			{
				date = sdf.parse(dateEnd);
			}
			catch (ParseException pe)
			{
				
			}
		}
		
		return date;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(String dateEnd)
	{
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the dateStart
	 */
	public String getDateStart()
	{
		return dateStart;
	}
	
	
	public Date getDateStartAsDate()
	{
		Date date = null;
		
		if (!infiniteStartDate && dateStart != null)
		{
			try
			{
				date = sdf.parse(dateStart);
			}
			catch (ParseException pe)
			{
				
			}
		}
		
		return date;
	}	

	/**
	 * @param dateStart the dateStart to set
	 */
	public void setDateStart(String dateStart)
	{
		this.dateStart = dateStart;
	}

	/**
	 * @return the infiniteEndDate
	 */
	public boolean isInfiniteEndDate()
	{
		return infiniteEndDate;
	}

	/**
	 * @param infiniteEndDate the infiniteEndDate to set
	 */
	public void setInfiniteEndDate(boolean infiniteEndDate)
	{
		this.infiniteEndDate = infiniteEndDate;
	}

	/**
	 * @return the infiniteStartDate
	 */
	public boolean isInfiniteStartDate()
	{
		return infiniteStartDate;
	}

	/**
	 * @param infiniteStartDate the infiniteStartDate to set
	 */
	public void setInfiniteStartDate(boolean infiniteStartDate)
	{
		this.infiniteStartDate = infiniteStartDate;
	}
}
