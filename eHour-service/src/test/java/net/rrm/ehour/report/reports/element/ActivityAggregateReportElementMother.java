package net.rrm.ehour.report.reports.element;

import net.rrm.ehour.domain.ActivityMother;

public class ActivityAggregateReportElementMother {

	public static ActivityAggregateReportElement createActivityAggregate(int baseId, int customerId, int userId) {
		ActivityAggregateReportElement activityAggregateReportElement = new ActivityAggregateReportElement();
		activityAggregateReportElement.setHours(baseId);
		activityAggregateReportElement.setActivity(ActivityMother.createActivity(baseId, customerId, userId));
		return activityAggregateReportElement;
	}
}
