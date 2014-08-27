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

package net.rrm.ehour.report.reports;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Project manager report
 */

public class ProjectManagerReport implements Serializable {
    private static final long serialVersionUID = 1768574303126675320L;

    private Project project;
    private SortedSet<ActivityAggregateReportElement> aggregates = new TreeSet<ActivityAggregateReportElement>();
    private DateRange reportRange;
    private Float totalHoursBooked;
    private Float totalHoursAvailable;

    /**
     * Derive the totals
     */
    public void deriveTotals() {
        float hours = 0;
        float avail = 0;

        if (aggregates != null) {
            for (ActivityAggregateReportElement aggregate : aggregates) {
                if (aggregate.getHours() == null) {
                    continue;
                }

                hours += aggregate.getHours().floatValue();

                //TODO-NK Need to see how we have to do the same for Activity
//				if (aggregate.getProjectAssignment().getAssignmentType().isAllottedType())
//				{
                avail += aggregate.getActivity().getAllottedHours().floatValue();
//				}
            }
        }

        totalHoursBooked = hours;
        totalHoursAvailable = avail;
    }

    /**
     * @return the aggregates
     */
    public SortedSet<ActivityAggregateReportElement> getAggregates() {
        return aggregates;
    }

    /**
     * @param aggregates the aggregates to set
     */
    public void setAggregates(SortedSet<ActivityAggregateReportElement> aggregates) {
        this.aggregates = aggregates;
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the reportRange
     */
    public DateRange getReportRange() {
        return reportRange;
    }

    /**
     * @param reportRange the reportRange to set
     */
    public void setReportRange(DateRange reportRange) {
        this.reportRange = reportRange;
    }

    /**
     * @return the totalHoursAvailable
     */
    public Float getTotalHoursAvailable() {
        return totalHoursAvailable;
    }

    /**
     * @param totalHoursAvailable the totalHoursAvailable to set
     */
    public void setTotalHoursAvailable(Float totalHoursAvailable) {
        this.totalHoursAvailable = totalHoursAvailable;
    }

    /**
     * @return the totalHoursBooked
     */
    public Float getTotalHoursBooked() {
        return totalHoursBooked;
    }

    /**
     * @param totalHoursBooked the totalHoursBooked to set
     */
    public void setTotalHoursBooked(Float totalHoursBooked) {
        this.totalHoursBooked = totalHoursBooked;
    }
}
