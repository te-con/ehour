package net.rrm.ehour.report.reports.element;

import net.rrm.ehour.domain.Activity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ReportElement for aggregate reports
 */

@SuppressWarnings("serial")
public class ActivityAggregateReportElement implements Comparable<ActivityAggregateReportElement>, ReportElement {

	private Activity activity;
	private Number hours;

	public ActivityAggregateReportElement() {

	}

	public ActivityAggregateReportElement(Activity activity, Number hours) {
		this.activity = activity;
		this.hours = hours;
	}

	/**
	 * Get the progress (booked hours) in percentage of the allotted hours,
	 * leaving out the overrun or for date ranges use the current date vs start
	 * & end date (if they're both null)
	 * 
	 * @return
	 */
	public float getProgressPercentage() {
		float percentage = 0;

		if (activity == null) {
			return percentage;
		}

		if (hours != null && activity.getAllottedHours() != null && hours.floatValue() > 0 && activity.getAllottedHours().floatValue() > 0) {
			percentage = (hours.floatValue() / activity.getAllottedHours().floatValue()) * 100;
		}
		return percentage;
	}

	/**
	 * For flex/fixed allotted, give the available hours
	 * 
	 * @return
	 */
	public float getAvailableHours() {
		float available = 0;

		if (activity == null) {
			return available;
		}
		if (hours != null && activity.getAllottedHours() != null && hours.floatValue() > 0
				&& activity.getAllottedHours().floatValue() > 0) {
			available = activity.getAllottedHours().floatValue() - hours.floatValue();
		}

		return available;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return the turnOver
	 */
	public Number getTurnOver() {
		//TODO-NK Need to have HourlyRate information in Activity to calculate the same
		return 0;
	}

	/**
     *
     */
	public int compareTo(ActivityAggregateReportElement pagO) {
		return this.getActivity().compareTo(pagO.getActivity());
	}

	public String toString() {
		return new ToStringBuilder(this).append("Activity", activity).append("hours", hours).toString();
	}

	/**
	 * @return the hours
	 */
	public Number getHours() {
		return hours;
	}

	/**
	 * @param hours
	 *            the hours to set
	 */
	public void setHours(Number hours) {
		this.hours = hours;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof ActivityAggregateReportElement)) {
			return false;
		}
		ActivityAggregateReportElement rhs = (ActivityAggregateReportElement) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.activity, rhs.activity).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(259442803, 2067843191).appendSuper(super.hashCode()).append(this.activity).toHashCode();
	}

}
