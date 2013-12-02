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

package net.rrm.ehour.project.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.domain.ProjectObjectMother;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ProjectAssignmentServiceImplTest {
    private ProjectAssignmentServiceImpl projectAssignmentService;
    private ProjectAssignmentDao projectAssignmentDAO;
    private ReportAggregatedDao reportAggregatedDAO;
    private ProjectAssignmentStatusService statusService;

    @Before
    public void setUp() {
        projectAssignmentDAO = createMock(ProjectAssignmentDao.class);
        reportAggregatedDAO = createMock(ReportAggregatedDao.class);
        statusService = createMock(ProjectAssignmentStatusService.class);

        projectAssignmentService = new ProjectAssignmentServiceImpl(projectAssignmentDAO, statusService, reportAggregatedDAO);
    }

    @Test
    public void should_find_project_assignment() throws ObjectNotFoundException {
        ProjectAssignment pa = new ProjectAssignment();

        expect(projectAssignmentDAO.findById(1)).andReturn(pa);

        List<Serializable> ids = new ArrayList<Serializable>();
        ids.add(1);

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(ids))
                .andReturn(new ArrayList<AssignmentAggregateReportElement>());

        replay(projectAssignmentDAO);
        replay(reportAggregatedDAO);

        projectAssignmentService.getProjectAssignment(1);

        verify(projectAssignmentDAO);
        verify(reportAggregatedDAO);
    }

    @Test
    public void should_find_project_assignments_for_project_and_check_deletability() throws ObjectNotFoundException {
        Project project = ProjectObjectMother.createProject(1);

        ProjectAssignment deleteableAssignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        ProjectAssignment nondeleteableAssignment = ProjectAssignmentObjectMother.createProjectAssignment(2);

        expect(projectAssignmentDAO.findAllProjectAssignmentsForProject(project)).andReturn(Arrays.asList(deleteableAssignment, nondeleteableAssignment));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(Lists.newArrayList(deleteableAssignment.getPK(), nondeleteableAssignment.getPK())))
                .andReturn(Lists.newArrayList(new AssignmentAggregateReportElement(nondeleteableAssignment, 5)));

        replay(projectAssignmentDAO);
        replay(reportAggregatedDAO);

        List<ProjectAssignment> assignments = projectAssignmentService.getProjectAssignmentsAndCheckDeletability(project);
        assertTrue(assignments.get(0).isDeletable());
        assertFalse(assignments.get(1).isDeletable());

        verify(projectAssignmentDAO);
        verify(reportAggregatedDAO);
    }

}
