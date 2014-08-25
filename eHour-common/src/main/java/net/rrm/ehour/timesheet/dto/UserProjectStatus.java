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

package net.rrm.ehour.timesheet.dto;

<<<<<<< HEAD
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

/**
 * Value object for timesheet overview. While the hours in an aggregate
 * reflect only a certain period, totalBookedHours is all hours booked on this assignment
 */

public class UserProjectStatus extends AssignmentAggregateReportElement
{
	private static final long serialVersionUID = 2321889010790629630L;

	private Number	totalBookedHours;

	public UserProjectStatus(AssignmentAggregateReportElement aggregate)
	{
		this(aggregate, null);
	}
	
	public UserProjectStatus(AssignmentAggregateReportElement aggregate, Number totalBookedHours)
	{
		super(aggregate.getProjectAssignment(), aggregate.getHours());
		this.totalBookedHours = totalBookedHours;
	}
	
	/**
	 * Get fixed hours remaining to book on this project
	 * This is applicable to fixed and flex assignments
	 * @return
	 */
	public Float getFixedHoursRemaining()
	{
		Float	remainder = null;
		
		if (totalBookedHours != null)
		{
			if (getProjectAssignment().getAssignmentType().isAllottedType())
			{
				remainder = getProjectAssignment().getAllottedHours() - totalBookedHours.floatValue();
				// Flex assignment that are over their fixed number of hours should report
				// zero fixed remaining.
                if (getProjectAssignment().getAssignmentType().isFlexAllottedType() && remainder < 0) {
                    remainder = 0f;
                }
			}
		}
		
		return remainder;
	}
	
	/**
	 * Get flex hours remaining to book on this project
	 * @return
	 */
	public Float getFlexHoursRemaining()
	{
		Float	remainder = null;

        if (totalBookedHours != null && getProjectAssignment().getAssignmentType().isFlexAllottedType()) {
            if (totalBookedHours.floatValue() <= getProjectAssignment().getAllottedHours()) {
                remainder = getProjectAssignment().getAllowedOverrun();
            } else {
                remainder = getProjectAssignment().getAllottedHours()
                        + getProjectAssignment().getAllowedOverrun()
                        - totalBookedHours.floatValue();
            }
        }
		
		return remainder;
	}
	
	/**
	 * @return the totalBookedHours
	 */
	public Number getTotalBookedHours()
	{
=======
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;

/**
 * Value object for timesheet overview. While the hours in an aggregate reflect
 * only a certain period, totalBookedHours is all hours booked on this
 * assignment
 */

public class UserProjectStatus extends ActivityAggregateReportElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2321889010790629630L;
	private Number totalBookedHours;

	public UserProjectStatus() {

	}

	public UserProjectStatus(ActivityAggregateReportElement aggregate) {
		this(aggregate, null);
	}

	public UserProjectStatus(ActivityAggregateReportElement aggregate, Number totalBookedHours) {
		super(aggregate.getActivity(), aggregate.getHours());
		this.totalBookedHours = totalBookedHours;
	}

	/**
	 * Get fixed hours remaining to book on this project This is applicable to
	 * fixed and flex assignments
	 * 
	 * @return
	 */
	public Float getFixedHoursRemaining() {
		Float remainder = null;

		if (totalBookedHours != null) {
			// TODO-NK Need to check about the assignment Type
			// if (getProjectAssignment().getAssignmentType().isAllottedType())
			// {
			remainder = new Float(getActivity().getAllottedHours().floatValue() - totalBookedHours.floatValue());
			// Flex assignment that are over their fixed number of hours should
			// report
			// zero fixed remaining.
//			if (getProjectAssignment().getAssignmentType().isFlexAllottedType()) {
//				if (remainder.floatValue() < 0) {
//					remainder = new Float(0f);
//				}
//			}

			// }
		}

		return remainder;
	}

	/**
	 * Get flex hours remaining to book on this project
	 * 
	 * @return
	 */
	public Float getFlexHoursRemaining() {
		Float remainder = null;

		if (totalBookedHours != null) {
			// TODO-NK Need to check about the assignment Type
			remainder = new Float(getActivity().getAllottedHours().floatValue() - totalBookedHours.floatValue());			
//			if (getProjectAssignment().getAssignmentType().isFlexAllottedType()) {
//				if (totalBookedHours.floatValue() <= getProjectAssignment().getAllottedHours().floatValue()) {
//					remainder = new Float(getProjectAssignment().getAllowedOverrun().floatValue());
//				} else {
//					remainder = new Float(getProjectAssignment().getAllottedHours().floatValue()
//							+ getProjectAssignment().getAllowedOverrun().floatValue() - totalBookedHours.floatValue());
//				}
//			}
		}

		return remainder;
	}

	/**
	 * @return the totalBookedHours
	 */
	public Number getTotalBookedHours() {
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
		return totalBookedHours;
	}

	/**
<<<<<<< HEAD
	 * @param totalBookedHours the totalBookedHours to set
	 */
	public void setTotalBookedHours(Number totalBookedHours)
	{
		this.totalBookedHours = totalBookedHours;
	}
	
=======
	 * @param totalBookedHours
	 *            the totalBookedHours to set
	 */
	public void setTotalBookedHours(Number totalBookedHours) {
		this.totalBookedHours = totalBookedHours;
	}

>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
}
