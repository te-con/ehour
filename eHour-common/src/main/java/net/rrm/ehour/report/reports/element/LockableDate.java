package net.rrm.ehour.report.reports.element;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class LockableDate implements Serializable {
    private final Date date;
    private final Boolean locked;

    public LockableDate(Date date, Boolean locked) {
        this.date = date;
        this.locked = locked;
    }

    public Date getDate() {
        return date;
    }

    public Boolean isLocked() {
        return locked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LockableDate) {
            if (obj == this) {
                return true;
            } else {
                LockableDate other = (LockableDate) obj;

                return new EqualsBuilder().append(other.date, this.date).append(other.locked, this.locked).isEquals();
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.date).append(this.locked).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Date", date).append("Locked", locked).toString();
    }
}
