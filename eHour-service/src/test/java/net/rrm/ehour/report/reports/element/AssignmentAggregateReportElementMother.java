package net.rrm.ehour.report.reports.element;

import net.rrm.ehour.domain.ProjectAssignmentMother;

/**
 * Created on Feb 7, 2010 2:58:49 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class AssignmentAggregateReportElementMother
{
	public static AssignmentAggregateReportElement createProjectAssignmentAggregate(int baseId, int customerId, int userId)
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours(baseId);
		pag.setProjectAssignment(ProjectAssignmentMother.createProjectAssignment(baseId, customerId, userId));
		return pag;
	}
}
