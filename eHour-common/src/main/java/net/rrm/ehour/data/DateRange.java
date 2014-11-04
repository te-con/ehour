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

package net.rrm.ehour.data;

import com.google.common.collect.Maps;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Object containing a range of dates.
 * Time is set to 00:00 for the start date and 23:59 for the date end therefore
 * including the start & the end
 */
public class DateRange implements Serializable {
    private static final long serialVersionUID = 4901436851703213753L;

    private static final Map<Date, DateTime> CACHE_START = Maps.newHashMap();
    private static final Map<Date, DateTime> CACHE_END = Maps.newHashMap();

    private Date dateStart;
    private Date dateStartCached;
    private DateTime localDateStart;

    private Date dateEnd;
    private Date dateEndCached;
    private DateTime localDateEnd;

    public DateRange() {
    }

    public DateRange(Date dateStart, Date dateEnd) {
        setDateStart(dateStart);
        setDateEnd(dateEnd);
    }

    public DateRange(Interval range) {
        setDateStart(range.getStart().toDate());
        setDateEnd(range.getEnd().toDate());
    }

    public Date getDateEnd() {
        if (dateEndCached == null && dateEnd != null) {
            this.dateEndCached = DateUtil.maximizeTime(dateEnd);
        }

        return dateEndCached;
    }

    public boolean isOpenEnded() {
        return dateStart == null && dateEnd == null;
    }

    public Interval toInterval() {
        return new Interval(localDateStart, localDateEnd);
    }

    /**
     * Is DateRange empty?
     *
     * @return
     */
    public boolean isEmpty() {
        return dateStart == null && dateEnd == null;
    }

    /**
     * Set the date end, time is set to 23:59:59.999
     *
     * @param dateEnd
     */
    public final void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;

        this.localDateEnd = new LocalDate(dateEnd).toDateTime(new LocalTime(23, 59, 59));
//
//        if (localDateEnd == null) {
//            this.localDateEnd = new LocalDate(dateEnd).toDateTimeAtStartOfDay().plusDays(1);
//            CACHE_END.put(dateEnd, localDateEnd);
//        }
    }

    /**
     * @return
     */
    public Date getDateStart() {
        if (dateStartCached == null && dateStart != null) {
            this.dateStartCached = DateUtil.nullifyTime(dateStart);
        }
        return dateStartCached;
    }

    /**
     * Set the date start, time is set to 00:00
     *
     * @param dateStart
     */
    public final void setDateStart(Date date) {
        long ts = date.getTime() - date.getTimezoneOffset()*60000L;
        this.dateStart = new Date(ts - ts % (3600000L*24L));
        long time = dateStart.getTime() + dateStart.getTimezoneOffset() * 60000L;
        this.dateStart.setTime(time);

        this.localDateStart = new DateTime(time);
//
//        if (localDateStart == null) {
//            this.localDateStart = new LocalDate(dateStart).toDateTimeAtStartOfDay();
//            CACHE_START.put(dateStart, localDateStart);
//            System.out.println("miss: " + dateStart.toString() + ", " +  CACHE_START.size());
//        }
    }

    public String toString() {
        return "date start: " + ((dateStart != null) ? dateStart.toString() : "null")
                + ", date end: " + ((dateEnd != null) ? dateEnd.toString() : "null");
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DateRange)) {
            return false;
        }
        DateRange rhs = (DateRange) object;
        return new EqualsBuilder().append(this.dateEnd, rhs.dateEnd).append(this.dateStart, rhs.dateStart).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(213586715, -454293689).append(this.dateEnd).append(this.dateStart).toHashCode();
    }
}
