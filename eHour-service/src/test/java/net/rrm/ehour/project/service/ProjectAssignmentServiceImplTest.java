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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectAssignmentServiceImplTest {
    @Mock
    private ProjectAssignmentServiceImpl projectAssignmentService;

    @Mock
    private ProjectAssignmentDao projectAssignmentDAO;

    @Mock
    private ReportAggregatedDao reportAggregatedDAO;

    @Mock
    private ProjectAssignmentStatusService statusService;

    @Before
    public void setUp() {
        projectAssignmentService = new ProjectAssignmentServiceImpl(projectAssignmentDAO, statusService, reportAggregatedDAO);
    }

    @Test
    public void should_find_project_assignment() throws ObjectNotFoundException {
        ProjectAssignment pa = ProjectAssignmentObjectMother.createProjectAssignment(1);

        when(projectAssignmentDAO.findById(1)).thenReturn(pa);

        List<Integer> ids = Lists.newArrayList();
        ids.add(1);

        when(reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(ids)).thenReturn(new ArrayList<AssignmentAggregateReportElement>());

        ProjectAssignment assignment = projectAssignmentService.getProjectAssignment(1);

        assertEquals(pa, assignment);
    }

    @Test
    public void should_find_project_assignments_for_project_and_check_deletability() throws ObjectNotFoundException {
        Project project = ProjectObjectMother.createProject(1);

        ProjectAssignment deleteableAssignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        ProjectAssignment nondeleteableAssignment = ProjectAssignmentObjectMother.createProjectAssignment(2);

        when(projectAssignmentDAO.findAllProjectAssignmentsForProject(project)).thenReturn(Arrays.asList(deleteableAssignment, nondeleteableAssignment));

        when(reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(Lists.newArrayList(deleteableAssignment.getPK(), nondeleteableAssignment.getPK())))
                .thenReturn(Lists.newArrayList(new AssignmentAggregateReportElement(nondeleteableAssignment, 5)));

        List<ProjectAssignment> assignments = projectAssignmentService.getProjectAssignmentsAndCheckDeletability(project);

        assertTrue(assignments.get(0).isDeletable());
        assertFalse(assignments.get(1).isDeletable());
    }

}
