package net.rrm.ehour.ui.timesheet.dto;

import com.google.common.collect.Lists;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TimesheetFactoryTest {
    private static final DateRange RANGE = DateUtil.getDateRangeForWeek(new GregorianCalendar(2012, Calendar.OCTOBER, 8));
    private EhourConfigStub config;

    @Before
    public void set_up() {
        config = new EhourConfigStub();
        config.setCompleteDayHours(8);
    }

    @Test
    public void should_build_timesheet_with_1_customer_with_1_assignment() {
        // given
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);

        WeekOverview weekOverview = new WeekOverview(Collections.<TimesheetEntry>emptyList(), null, Arrays.asList(assignment), RANGE, null, Lists.<Date>newArrayList());


        // when
        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

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

        WeekOverview weekOverview = new WeekOverview(Collections.<TimesheetEntry>emptyList(), null, Arrays.asList(assignment01, assignment02), RANGE, null, Lists.<Date>newArrayList());

        // when
        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

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

        WeekOverview weekOverview = new WeekOverview(Collections.<TimesheetEntry>emptyList(), null, Arrays.asList(assignment01, assignment02), RANGE, null, Lists.<Date>newArrayList());

        // when
        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

        // then
        SortedMap<Customer,List<TimesheetRow>> customerRows = timesheet.getCustomers();

        Set<Customer> customers = customerRows.keySet();
        assertEquals(1, customers.size());

        List<TimesheetRow> rows = customerRows.get(customers.iterator().next());

        assertEquals("a", rows.get(0).getProjectAssignment().getProject().getName());
        assertEquals("b", rows.get(1).getProjectAssignment().getProject().getName());
    }

    @Test
    public void should_mark_entry_as_locked_when_project_is_inactive() {
        // given
        User user = UserObjectMother.createUser();

        ProjectAssignment assignment01 = ProjectAssignmentObjectMother.createProjectAssignment(user, ProjectObjectMother.createProject(1));
        assignment01.getProject().setName("a");

        ProjectAssignment assignment02 = ProjectAssignmentObjectMother.createProjectAssignment(user, ProjectObjectMother.createProject(2));
        assignment02.getProject().setName("b");
        assignment02.getProject().setActive(false);

        TimesheetEntry entry = new TimesheetEntry(new TimesheetEntryId(new Date(), assignment02), 5f);
        WeekOverview weekOverview = new WeekOverview(Collections.singletonList(entry), null, Collections.singletonList(assignment01), RANGE, null, Lists.<Date>newArrayList());

        // when
        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

        // then
        SortedMap<Customer,List<TimesheetRow>> customerRows = timesheet.getCustomers();
        Set<Customer> customers = customerRows.keySet();

        List<TimesheetRow> rows = customerRows.get(customers.iterator().next());
        assertFalse(rows.get(0).getTimesheetCells()[0].isLocked());
        assertTrue(rows.get(1).getTimesheetCells()[0].isLocked());
    }
}
