/**
 * Created on Feb 4, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.element.FlatReportElement;

/**
 * TODO 
 **/

public interface DetailedReportDAO
{
	
	/**
	 * TODO refactor method signatures (perMonth vs Weekly? hmm)
	 * @param userId
	 * @param projectId
	 * @param dateRange
	 * @return
	 */
//	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForUsers(List<Serializable> userId, List<Serializable> projectId, DateRange dateRange);
//	
//	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForUsers(List<Serializable> userId, DateRange dateRange);
//	
//	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignment(DateRange dateRange);
//	
//	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForProjects(List<Serializable> projectId, DateRange dateRange);
	
	/**
	 * Get hours per day for assignments
	 * @param assignmentId
	 * @param dateRange
	 * @return
	 */
	public List<FlatReportElement> getHoursPerDayForAssignment(List<Serializable> assignmentId, DateRange dateRange);

	/**
	 * Get hours per day for users
	 * @param userIds
	 * @param dateRange
	 * @return
	 */
	public List<FlatReportElement> getHoursPerDayForUsers(List<Serializable> userIds, DateRange dateRange);

	/**
	 * Get hours per day for projects
	 * @param userIds
	 * @param dateRange
	 * @return
	 */
	public List<FlatReportElement> getHoursPerDayForProjects(List<Serializable> projectIds, DateRange dateRange);

	/**
	 * Get hours per day for projects & users
	 * @param userIds
	 * @param dateRange
	 * @return
	 */
	public List<FlatReportElement> getHoursPerDayForProjectsAndUsers(List<Serializable> projectIds, List<Serializable> userIds, DateRange dateRange);

	/**
	 * Get hours per day
	 * @param dateRange
	 * @return
	 */
	public List<FlatReportElement> getHoursPerDay(DateRange dateRange);

}
