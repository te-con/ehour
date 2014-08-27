package net.rrm.ehour.activity.status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;

/**
 * An Activity can have 2 statusses: - booked hours before allotted hours mark
 * (IN_ALLOTTED_PHASE) - over the alloted hours mark, no more hours can be
 * booked and mail should be sent (OVER_ALLOTTED_PHASE)
 * 
 * Additionally an Activity can have either a before start, running, after
 * deadline status
 **/

@SuppressWarnings("serial")
public class ActivityStatus implements Serializable {

	public enum Status {
		IN_ALLOTTED, OVER_ALLOTTED, BEFORE_START, RUNNING, AFTER_DEADLINE
	}

	private List<Status> statusses;

	private ActivityAggregateReportElement aggregate;
	
	private boolean valid;

	public ActivityStatus() {
		this.valid = true;
	}

	public List<Status> getStatusses() {
		return statusses;
	}

	public void setStatusses(List<Status> statusses) {
		this.statusses = statusses;
	}

	public void addStatus(Status status) {
		if (this.statusses == null) {
			this.statusses = new ArrayList<Status>();
		}
		this.statusses.add(status);
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public ActivityAggregateReportElement getAggregate() {
		return aggregate;
	}

	public void setAggregate(ActivityAggregateReportElement aggregate) {
		this.aggregate = aggregate;
	}

	public boolean isActivityBookable() {
		boolean isBookable = true;
		//TODO-NK Need to find a better way of doing this with Collections
		if (statusses != null && statusses.size() > 0) {
			for (Status status : statusses) {
				isBookable &= (status == Status.IN_ALLOTTED);
			}
		}
		return isBookable;
	}
}
