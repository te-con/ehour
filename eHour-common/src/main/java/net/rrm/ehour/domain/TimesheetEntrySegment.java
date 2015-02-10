package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "TIMESHEET_ENTRY_SEGMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimesheetEntrySegment extends DomainObject<TimesheetEntryId, TimesheetEntrySegment> {

    @Id
    @Valid
    private TimesheetEntryId entryId;

    @Column(name = "START_WORK_TIME")
    @NotNull
    private Date startWorkTime;

    @Column(name = "END_WORK_TIME")
    @NotNull
    private Date endWorkTime;

    @Override
    public TimesheetEntryId getPK() {
        return getEntryId();
    }

    @Override
    public int compareTo(TimesheetEntrySegment object) {
        return new CompareToBuilder().append(this, object).toComparison();
    }

    public TimesheetEntryId getEntryId() {
        return entryId;
    }

    public void setEntryId(TimesheetEntryId entryId) {
        this.entryId = entryId;
    }

    public Date getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(Date startWorkTime) {
        this.startWorkTime = startWorkTime;
    }

    public Date getEndWorkTime() {
        return endWorkTime;
    }

    public void setEndWorkTime(Date endWorkTime) {
        this.endWorkTime = endWorkTime;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TimesheetEntrySegment)) {
            return false;
        }
        TimesheetEntrySegment other = (TimesheetEntrySegment) object;
        return new EqualsBuilder()
                .append(getEntryId(), other.getEntryId())
                .append(getStartWorkTime(), other.getStartWorkTime())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getEntryId()).append(startWorkTime).toHashCode();
    }
}
