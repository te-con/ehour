package net.rrm.ehour.ui.admin.activity.dto;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

/**
 * Backing bean for {@link Activity} administration
 **/
public class ActivityBackingBean extends AdminBackingBeanImpl {

	private static final long serialVersionUID = 3244128199251617458L;
	
	private Activity activity;
	
	public ActivityBackingBean(Activity activity) {
		this.activity = activity;
	}

	@Override
	public Activity getDomainObject() {
		return getActivity();
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
