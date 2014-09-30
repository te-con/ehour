package net.rrm.ehour.activity.status;

import net.rrm.ehour.activity.status.ActivityStatus.Status;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("activityStatusService")
public class ActivityStatusServiceImpl implements ActivityStatusService {

	@Autowired
	private ReportAggregatedDao reportAggregatedDAO;

	@Override
	public ActivityStatus getActivityStatus(Activity activity) {
		ActivityStatus result = new ActivityStatus();

		getStatusRelatedToHours(activity, result);
		return result;
	}

	@Override
	public ActivityStatus getActivityStatus(Activity activity, DateRange period) {
		ActivityStatus result = new ActivityStatus();

		getStatusRelatedToHours(activity, result);

		computeAndAddStatusRelatedToDeadLines(result, activity, period);
		return result;
	}

	private void getStatusRelatedToHours(Activity activity, ActivityStatus result) {
		ActivityAggregateReportElement aggregateReport = reportAggregatedDAO.getCumulatedHoursForActivity(activity);
		result.setAggregate(aggregateReport);

		computeAndAddStatusRelatedToHours(result, activity);
	}

	private void computeAndAddStatusRelatedToDeadLines(ActivityStatus result, Activity activity, DateRange period) {
		Date activityStartDate = activity.getDateStart();
		Date activityEndDate = activity.getDateEnd();

		DateRange activityDateRange = new DateRange(activityStartDate, activityEndDate);

		if (DateUtil.isDateRangeOverlaps(activityDateRange, period)) {
			result.addStatus(Status.RUNNING);
		} else {
			if (activityStartDate != null && period.getDateEnd().before(activityStartDate)) {
				result.addStatus(Status.BEFORE_START);
			} else if (activityEndDate != null && period.getDateStart().after(activityEndDate)) {
				result.addStatus(Status.AFTER_DEADLINE);
			}
		}
	}

	private void computeAndAddStatusRelatedToHours(ActivityStatus result, Activity activity) {
		if (result.getAggregate() != null) {
			Number alreadyBookedHoursNumber = result.getAggregate().getHours();
			
			float alreadyBookedHours = (alreadyBookedHoursNumber == null) ? 0 : alreadyBookedHoursNumber.floatValue();
			
			float allottedHours = (activity.getAllottedHours() == null) ? 0 : activity.getAllottedHours();
			
			if (alreadyBookedHours > allottedHours) {
				result.setValid(false);
				result.addStatus(Status.OVER_ALLOTTED);
			} else {
				result.addStatus(Status.IN_ALLOTTED);
			}
		} else {
			result.addStatus(Status.IN_ALLOTTED);
		}
	}
}
