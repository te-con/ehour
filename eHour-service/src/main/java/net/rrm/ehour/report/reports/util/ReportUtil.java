package net.rrm.ehour.report.reports.util;

import java.util.Collection;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

/**
 * Created on May 17, 2010 12:10:04 AM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ReportUtil
{
	private ReportUtil() {
	}

	/**
	 * Check if aggregate list is empty
	 * @param aggregates
	 * @return
	 */
	public static boolean isEmptyAggregateList(Collection<AssignmentAggregateReportElement> aggregates)
	{
		float	hours = 0f;
		
		if (aggregates != null)
		{
			for (AssignmentAggregateReportElement assignmentAggregateReportElement : aggregates)
			{
				if (assignmentAggregateReportElement.getHours() != null)
				{
					hours += assignmentAggregateReportElement.getHours().floatValue();
				}
			}
		}
		
		return hours == 0f;
	}	
}
