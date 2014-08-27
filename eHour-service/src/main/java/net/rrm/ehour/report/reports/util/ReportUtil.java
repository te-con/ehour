package net.rrm.ehour.report.reports.util;

import java.util.Collection;

import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;

/**
 * Created on May 17, 2010 12:10:04 AM
 * 
 * @author thies (www.te-con.nl)
 * 
 */
public class ReportUtil {

	public static boolean isEmptyAggregateList(Collection<ActivityAggregateReportElement> aggregates) {
		float hours = 0f;

		if (aggregates != null) {
			for (ActivityAggregateReportElement activityAggregateReportElement : aggregates) {
				if (activityAggregateReportElement.getHours() != null) {
					hours += activityAggregateReportElement.getHours().floatValue();
				}
			}
		}

		return hours == 0f;
	}
}
