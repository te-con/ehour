package net.rrm.ehour.activity.status;

import org.springframework.stereotype.Service;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;


@Service("activityStatusService")
public class ActivityStatusServiceImpl implements ActivityStatusService {

	@Override
	public ActivityStatus getActivityStatus(Activity activity) {
		// TODO-NK Need to implement properly in association with ReportDao changed. 
		// For the time-being creating a parallel hierarchy
		// for getting the compilation errors away
		return new ActivityStatus();
	}

	@Override
	public ActivityStatus getActivityStatus(Activity activity, DateRange period) {
		// TODO-NK Need to implement properly in association with ReportDao changed. 
		// For the time-being creating a parallel hierarchy
		// for getting the compilation errors away		
		return new ActivityStatus();
	}

}
