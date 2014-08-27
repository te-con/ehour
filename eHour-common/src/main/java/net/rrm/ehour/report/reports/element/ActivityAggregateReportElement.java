package net.rrm.ehour.report.reports.element;

import com.google.common.base.Optional;
import net.rrm.ehour.domain.Activity;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ReportElement for aggregate reports
 */

@SuppressWarnings("serial")
public class ActivityAggregateReportElement implements Comparable<ActivityAggregateReportElement>, ProjectStructuredReportElement {

	private Activity activity;
	
	private Number hours;
    private Boolean emptyEntry;

	public ActivityAggregateReportElement() {

	}

	public ActivityAggregateReportElement(Activity activity, Number hours) {
		this.activity = activity;
		this.hours = hours;
	}

    public ActivityAggregateReportElement(Activity activity) {
        this.activity = activity;
        emptyEntry = true;
    }

    /**
	 * Get the progress (booked hours) in percentage of the allotted hours,
	 * leaving out the overrun or for date ranges use the current date vs start
	 * & end date (if they're both null)
	 * 
	 * @return
	 */
	public Optional<Float> getProgressPercentage() {
        Optional<Float> percentage = Optional.absent();

		if (activity == null) {
			return percentage;
		}

        // everything is allotted
		if (hours != null && activity.getAllottedHours() != null && hours.floatValue() > 0 && activity.getAllottedHours() > 0) {
			percentage = Optional.of((hours.floatValue() / activity.getAllottedHours()) * 100);
		}

		return percentage;
	}

	/**
	 * For flex/fixed allotted, give the available hours
	 */
	public Optional<Float> getAvailableHours() {
        Optional<Float> available = Optional.absent();

		if (activity == null) {
			return available;
		}
		if (hours != null && activity.getAllottedHours() != null && hours.floatValue() > 0
				&& activity.getAllottedHours() > 0) {
            available = Optional.of(activity.getAllottedHours() - hours.floatValue());
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
        //TODO-TRE: Let's get rid completely of the turn over
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

    @Override
    public Integer getProjectId() {
        return activity.getProject().getProjectId();
    }

    @Override
    public Boolean isEmptyEntry() {
        return emptyEntry == null ? false : emptyEntry;
    }
}
