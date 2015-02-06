package net.rrm.ehour.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import java.util.Date;

@Data
@Entity
@Table(name = "TIMESHEET_ENTRY_SEGMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode
@ToString
public class TimesheetEntrySegment extends DomainObject<TimesheetEntryId, TimesheetEntrySegment> {

    @Id
    @Valid
    private TimesheetEntryId entryId;

    @Column(name = "START_WORK_TIME")
    private Date startWorkTime;

    @Column(name = "END_WORK_TIME")
    private Date endWorkTime;

    @Override
    public TimesheetEntryId getPK() {
        return entryId;
    }

    @Override
    public int compareTo(TimesheetEntrySegment object) {
        return new CompareToBuilder().append(this, object).toComparison();
    }
}
