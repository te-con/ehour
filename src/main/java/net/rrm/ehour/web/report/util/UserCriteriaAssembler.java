/**
 * Created on Feb 1, 2007
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

package net.rrm.ehour.web.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.web.report.form.ReportCriteriaForm;

/**
 * TODO 
 **/

public class UserCriteriaAssembler
{
	// do not instantiate
	private UserCriteriaAssembler()
	{
	}
	
	/**
	 * Assemble user criteria from a reportcriteriaform
	 * @param rcf
	 * @return
	 * @throws ParseException 
	 */
	public static UserCriteria getUserCriteria(ReportCriteriaForm rcf) throws ParseException
	{
		UserCriteria		uc;
		SimpleDateFormat	dateParser = new SimpleDateFormat("yyyy-MM-dd");
		DateRange			reportRange;
		boolean				dateSupplied = false;
		
		uc = new UserCriteria();

		reportRange = new DateRange();
		
		if (rcf.getDateStart() != null)
		{
			reportRange.setDateStart(dateParser.parse(rcf.getDateStart()));
			dateSupplied = true;
		}
		
		if (rcf.getDateEnd() != null)
		{
			reportRange.setDateEnd(dateParser.parse(rcf.getDateEnd()));
			dateSupplied = true;
		}

		if (dateSupplied)
		{
			uc.setReportRange(reportRange);
		}
		
		return uc;
	}
}
