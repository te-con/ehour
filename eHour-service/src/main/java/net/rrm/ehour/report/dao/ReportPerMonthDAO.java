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

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;

/**
 * TODO 
 **/

public interface ReportPerMonthDAO
{
	
	/**
	 * TODO refactor method signatures (perMonth vs Weekly? hmm)
	 * @param userId
	 * @param projectId
	 * @param dateRange
	 * @return
	 */
	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForUsers(List<Integer> userId, List<Integer> projectId, DateRange dateRange);
	
	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForUsers(List<Integer> userId, DateRange dateRange);
	
	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignment(DateRange dateRange);
	
	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForProjects(List<Integer> projectId, DateRange dateRange);
	
	/**
	 * Get hours per day for assignments
	 * @param assignmentId
	 * @param dateRange
	 * @return
	 */
	public List<FlatProjectAssignmentAggregate> getHoursPerDayForAssignment(List<Integer> assignmentId, DateRange dateRange);
	
}
