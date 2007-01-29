/**
 * Created on Jan 14, 2007
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

package net.rrm.ehour.report.criteria;

import net.rrm.ehour.report.service.ReportService;

import org.apache.log4j.Logger;


/**
 * TODO 
 **/

public class ReportCriteria
{
	private	Logger				logger = Logger.getLogger(this.getClass());
	private	AvailableCriteria	availableCriteria;
	private	UserCriteria		userCriteria;
	private	ReportService		reportService;
	
	/**
	 * Initialize the available criteria so they're available
	 * right after object construction
	 *
	 */
	public void initialize()
	{
		logger.debug("Initializing report criteria");
		
		availableCriteria = new AvailableCriteria();
		userCriteria = new UserCriteria();
		
		updateAvailableCriteria();
	}
	
	/**
	 * 
	 *
	 */
	public void updateAvailableCriteria()
	{
		reportService.syncUserReportCriteria(this);
	}

	/**
	 * @return the userCriteria
	 */
	public UserCriteria getUserCriteria()
	{
		return userCriteria;
	}
	
	/**
	 * @param userCriteria the userCriteria to set
	 */
	public void setUserCriteria(UserCriteria userCriteria)
	{
		this.userCriteria = userCriteria;
		updateAvailableCriteria();
	}

	/**
	 * @param reportService the reportService to set
	 */
	public void setReportService(ReportService reportService)
	{
		this.reportService = reportService;
	}

	/**
	 * @return the availableCriteria
	 */
	public AvailableCriteria getAvailableCriteria()
	{
		return availableCriteria;
	}

	/**
	 * @param availableCriteria the availableCriteria to set
	 */
	public void setAvailableCriteria(AvailableCriteria availableCriteria)
	{
		this.availableCriteria = availableCriteria;
	}
}