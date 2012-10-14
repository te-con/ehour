/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.timesheet.export.excel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.util.DateUtil;

/**
 * Created on Apr 23, 2009, 4:24:47 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportDummyCreater
{
	public static List<FlatReportElement> createMonthData(EhourConfig config)
	{
		List<FlatReportElement>	elements = new ArrayList<FlatReportElement>();
		
		DateRange range = getDateRangeForCurrentMonth();
		
		List<Date> month = DateUtil.createDateSequence(range, config);
		
		for (Date date : month)
		{
			if (Math.random() >= 0.2)
			{
				elements.add(createElement(date));	
			}
		}
		return elements;
	}
	
	public static DateRange getDateRangeForCurrentMonth()
	{
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		
		return DateUtil.getDateRangeForMonth(cal);
	}
	
	private static FlatReportElement createElement(Date date)
	{
		FlatReportElement element = new FlatReportElement();
		element.setCustomerCode("TE1");
		element.setCustomerName("TEST #1");
		element.setProjectName("Project #1");
        element.setProjectCode("PRJ");
		element.setDayDate(date);
		element.setHours(Math.random() * 8);
		element.setTotalHours(element.getHours());

		return element;
	}	
}
