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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class TimesheetEntryId implements Serializable, Comparable<TimesheetEntryId>
{
    private static final long serialVersionUID = 6439918043325585774L;

    @Column(name = "ENTRY_DATE", nullable = false)
    @NotNull
    private Date entryDate;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID", nullable = false)
    @Basic(fetch = FetchType.LAZY)
    @NotNull
    private Activity activity;

    /**
     * full constructor
     */
    public TimesheetEntryId(Date entryDate, Activity activity)
    {
        this.entryDate = entryDate;
        this.activity = activity;
    }

    /**
     * default constructor
     */
    public TimesheetEntryId()
    {
    }

    public Date getEntryDate()
    {
        return this.entryDate;
    }

    public void setEntryDate(Date entryDate)
    {
        this.entryDate = entryDate;
    }

    public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
    public String toString()
    {
        return new ToStringBuilder(this).append("entryDate", getEntryDate())
                .append("activity", getActivity())
                .toString();
    }

    @Override
    public boolean equals(Object other)
    {
        if ((this == other))
        {
            return true;
        }
        if (!(other instanceof TimesheetEntryId))
        {
            return false;
        }
        TimesheetEntryId castOther = (TimesheetEntryId) other;
        return new EqualsBuilder().append(this.getEntryDate(), castOther.getEntryDate())
                .append(this.getActivity(), castOther.getActivity()).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getEntryDate())
                .append(getActivity()).toHashCode();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(TimesheetEntryId object)
    {
        return new CompareToBuilder()
                .append(this.getActivity(), object.getActivity())
                .append(this.getEntryDate(), object.getEntryDate())
                .toComparison();
    }
}
