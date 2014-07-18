package net.rrm.ehour.domain;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TIMESHEET_LOCK")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimesheetLock extends DomainObject<Integer, TimesheetLock> {
    private static final long serialVersionUID = 2546435367535412269L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOCK_ID")
    private Integer lockId;

    @Column(name = "DATE_START")
    @NotNull
    private Date dateStart;

    @Column(name = "DATE_END")
    @NotNull
    private Date dateEnd;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(targetEntity = User.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "TIMESHEET_LOCK_EXCLUSION",
            joinColumns = @JoinColumn(name = "LOCK_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<User> excludedUsers = Lists.newArrayList();

    public TimesheetLock() {
    }

    public TimesheetLock(Date dateStart, Date dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public TimesheetLock(Date dateStart, Date dateEnd, List<User> excludedUsers) {
        this(dateStart, dateEnd);
        this.excludedUsers = excludedUsers;
    }

    public TimesheetLock(Date dateStart, Date dateEnd, String name, List<User> excludedUsers) {
        this(dateStart, dateEnd, excludedUsers);

        this.name = name;
    }


    public TimesheetLock(Integer lockId, Date dateStart, Date dateEnd, String name, List<User> excludedUsers) {
        this(dateStart, dateEnd, name, excludedUsers);
        this.lockId = lockId;
    }

    public Integer getLockId() {
        return lockId;
    }

    public void setLockId(Integer lockId) {
        this.lockId = lockId;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getExcludedUsers() {
        return excludedUsers;
    }

    public void setExcludedUsers(List<User> excludedUsers) {
        this.excludedUsers = excludedUsers;
    }

    @Override
    public Integer getPK() {
        return lockId;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TimesheetLock)) {
            return false;
        }
        TimesheetLock castOther = (TimesheetLock) other;

        return new EqualsBuilder()
                .append(dateStart, castOther.dateStart)
                .append(dateEnd, castOther.dateEnd)
                .append(name, castOther.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(dateStart)
                .append(dateEnd)
                .append(name).toHashCode();
    }

    @Override
    public int compareTo(TimesheetLock other) {
        return new CompareToBuilder()
                .append(dateStart, other.dateStart)
                .append(dateEnd, other.dateEnd)
                .append(name, other.name)
                .toComparison();
    }
}
