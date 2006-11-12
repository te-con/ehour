package net.rrm.ehour.timesheet.domain;

import java.io.Serializable;

import net.rrm.ehour.project.domain.ProjectAssignment;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public class TimesheetEntry implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3258176976827482751L;

	/** identifier field */
    private TimesheetEntryId entryId;

    /** nullable persistent field */
    private Float hours;

    /** nullable persistent field */
    private ProjectAssignment projectAssignment;

    /** full constructor */
    public TimesheetEntry(TimesheetEntryId entryId, Float hours, ProjectAssignment projectAssignment) {
        this.entryId = entryId;
        this.hours = hours;
        this.projectAssignment = projectAssignment;
    }

    /** default constructor */
    public TimesheetEntry() {
    }

    /** minimal constructor */
    public TimesheetEntry(TimesheetEntryId entryId) {
        this.entryId = entryId;
    }

    public TimesheetEntryId getEntryId() {
        return this.entryId;
    }

    public void setEntryId(TimesheetEntryId entryId) {
        this.entryId = entryId;
    }

    public Float getHours() {
        return this.hours;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }

    public ProjectAssignment getProjectAssignment() {
        return this.projectAssignment;
    }

    public void setProjectAssignment(ProjectAssignment projectAssignment) {
        this.projectAssignment = projectAssignment;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("entryID", getEntryId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof TimesheetEntry) ) return false;
        TimesheetEntry castOther = (TimesheetEntry) other;
        return new EqualsBuilder()
            .append(this.getEntryId(), castOther.getEntryId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getEntryId())
            .toHashCode();
    }
}