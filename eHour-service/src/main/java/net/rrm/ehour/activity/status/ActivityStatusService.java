package net.rrm.ehour.activity.status;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;

public interface ActivityStatusService {

	ActivityStatus getActivityStatus(Activity activity);

	ActivityStatus getActivityStatus(Activity activity, DateRange period);
}
