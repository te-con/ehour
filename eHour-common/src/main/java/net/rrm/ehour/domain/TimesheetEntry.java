/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "TIMESHEET_ENTRY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimesheetEntry extends DomainObject<TimesheetEntryId, TimesheetEntry> {
    private static final long serialVersionUID = 3258176976827482751L;

    @Id
    @Valid
    private TimesheetEntryId entryId;

    @Column(name = "HOURS")
    private BigDecimal hours;

    @Column(name = "COMMENT", length = 2048)
    private String comment;

    @Column(name = "UPDATE_DATE")
    @NotNull
    private Date updateDate;

    public TimesheetEntry() { }
    /**
     * full constructor
     */
    public TimesheetEntry(TimesheetEntryId entryId, BigDecimal hours) {
        this.setEntryId(entryId);
        this.setHours(hours);
    }

    public TimesheetEntry(TimesheetEntryId entryId) {
        this.setEntryId(entryId);
    }

    public boolean isEmptyEntry() {
        return getHours() == null;
    }

    @Override
    public TimesheetEntryId getPK() {
        return getEntryId();
    }

    public int compareTo(TimesheetEntry object) {
        return new CompareToBuilder()
                .append(this.getEntryId(), object.getEntryId()).toComparison();
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("hours", this.getHours())
                .append("entryId", this.getEntryId())
                .append("comment", this.getComment())
                .append("updateDate", this.getUpdateDate())
                .toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof TimesheetEntry))
            return false;
        TimesheetEntry castOther = (TimesheetEntry) other;
        return new EqualsBuilder().append(getEntryId(), castOther.getEntryId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getEntryId()).toHashCode();
    }

    public TimesheetEntryId getEntryId() {
        return entryId;
    }

    public void setEntryId(TimesheetEntryId entryId) {
        this.entryId = entryId;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}