package net.rrm.ehour.ui.timesheet.dto;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TimesheetFactoryTest {

    private TimesheetFactory builder;
    private WeekOverview weekOverview;

    @Before
    public void set_up() {
        EhourConfigStub config = new EhourConfigStub();
        config.setCompleteDayHours(8);
        builder = new TimesheetFactory(config);

        weekOverview = new WeekOverview();
        weekOverview.setWeekRange(DateUtil.getDateRangeForWeek(new GregorianCalendar(2012, Calendar.OCTOBER, 8)));
    }

    @Test
    public void should_build_timesheet_with_1_customer_with_1_assignment() {
        // given
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);

        weekOverview.setProjectAssignments(Arrays.asList(assignment));
        weekOverview.setTimesheetEntries(Collections.<TimesheetEntry>emptyList());

        // when
        Timesheet timesheet = builder.createTimesheet(weekOverview);

        // then
        SortedMap<Customer,List<TimesheetRow>> customers = timesheet.getCustomers();

        assertEquals(1, customers.keySet().size());
        assertEquals(1, customers.values().iterator().next().size());
    }

    @Test
    public void should_build_timesheet_with_multiple_assignments_on_same_project() {
        // given
        User user = UserObjectMother.createUser();
        Project project = ProjectObjectMother.createProject(1);

        ProjectAssignment assignment01 = ProjectAssignmentObjectMother.createProjectAssignment(user, project);
        ProjectAssignment assignment02 = ProjectAssignmentObjectMother.createProjectAssignment(user, project);
        assignment02.setAssignmentId(2);
        assignment02.setRole("role");

        weekOverview.setProjectAssignments(Arrays.asList(assignment01, assignment02));
        weekOverview.setTimesheetEntries(Collections.<TimesheetEntry>emptyList());

        // when
        Timesheet timesheet = builder.createTimesheet(weekOverview);

        // then
        SortedMap<Customer,List<TimesheetRow>> customers = timesheet.getCustomers();

        assertEquals(1, customers.keySet().size());
        assertEquals(2, customers.values().iterator().next().size());
    }

    @Test
    public void should_build_timesheet_with_sorted_projects() {
        // given
        User user = UserObjectMother.createUser();

        ProjectAssignment assignment01 = ProjectAssignmentObjectMother.createProjectAssignment(user, ProjectObjectMother.createProject(1));
        assignment01.getProject().setName("b");
        ProjectAssignment assignment02 = ProjectAssignmentObjectMother.createProjectAssignment(user, ProjectObjectMother.createProject(2));
        assignment02.getProject().setName("a");
        assignment02.setAssignmentId(2);
        assignment02.setRole("role");

        weekOverview.setProjectAssignments(Arrays.asList(assignment01, assignment02));
        weekOverview.setTimesheetEntries(Collections.<TimesheetEntry>emptyList());

        // when
        Timesheet timesheet = builder.createTimesheet(weekOverview);

        // then
        SortedMap<Customer,List<TimesheetRow>> customerRows = timesheet.getCustomers();

        Set<Customer> customers = customerRows.keySet();
        assertEquals(1, customers.size());

        List<TimesheetRow> rows = customerRows.get(customers.iterator().next());

        assertEquals("a", rows.get(0).getProjectAssignment().getProject().getName());
        assertEquals("b", rows.get(1).getProjectAssignment().getProject().getName());
    }
}
