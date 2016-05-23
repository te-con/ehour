package net.rrm.ehour.report.reports.element;

import net.rrm.ehour.domain.ProjectAssignmentObjectMother;

/**
 * Created on Feb 7, 2010 2:58:49 PM
 *
 * @author thies (www.te-con.nl)
 */
public class AssignmentAggregateReportElementMother {
    private AssignmentAggregateReportElementMother() {
    }

    public static AssignmentAggregateReportElement createProjectAssignmentAggregate() {
        return createProjectAssignmentAggregate(1, 1, 1);
    }

    public static AssignmentAggregateReportElement createProjectAssignmentAggregate(int baseId, int customerId, int userId) {
        AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
        pag.setHours(baseId);
        pag.setProjectAssignment(ProjectAssignmentObjectMother.createProjectAssignment(baseId, customerId, userId));
        return pag;
    }
}
