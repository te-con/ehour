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

import static org.junit.Assert.assertEquals;

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
        Activity activity = ActivityMother.createActivity(1);

        WeekOverview weekOverview = new WeekOverview(Collections.<TimesheetEntry>emptyList(), null, Arrays.asList(activity), RANGE, null, Lists.<Date>newArrayList());


        // when
        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

        // then
        SortedMap<Project,List<TimesheetRow>> projects = timesheet.getProjects();

        assertEquals(1, projects.keySet().size());
        assertEquals(1, projects.values().iterator().next().size());
    }

    @Test
    public void should_build_timesheet_with_multiple_assignments_on_same_project() {
        // given
        User user = UserObjectMother.createUser();
        Project project = ProjectObjectMother.createProject(1);

        Activity assignment01 = ActivityMother.createActivity(user, project);
        Activity assignment02 = ActivityMother.createActivity(user, project);
        assignment02.setId(2);

        WeekOverview weekOverview = new WeekOverview(Collections.<TimesheetEntry>emptyList(), null, Arrays.asList(assignment01, assignment02), RANGE, null, Lists.<Date>newArrayList());

        // when
        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

        // then
        SortedMap<Project,List<TimesheetRow>> projects = timesheet.getProjects();

        assertEquals(1, projects.keySet().size());
        assertEquals(2, projects.values().iterator().next().size());
    }

    @Test
    public void should_build_timesheet_with_sorted_projects() {
        // given
        User user = UserObjectMother.createUser();

        Activity assignment01 = ActivityMother.createActivity(user, ProjectObjectMother.createProject(1));
        assignment01.getProject().setName("b");
        Activity assignment02 = ActivityMother.createActivity(user, ProjectObjectMother.createProject(2));
        assignment02.getProject().setName("a");
        assignment02.setId(2);

        WeekOverview weekOverview = new WeekOverview(Collections.<TimesheetEntry>emptyList(), null, Arrays.asList(assignment01, assignment02), RANGE, null, Lists.<Date>newArrayList());

        // when
        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

        // then
        SortedMap<Project,List<TimesheetRow>> projectRows = timesheet.getProjects();

        Set<Project> customers = projectRows.keySet();
        assertEquals(1, customers.size());

        List<TimesheetRow> rows = projectRows.get(customers.iterator().next());

        assertEquals("a", rows.get(0).getActivity().getProject().getName());
        assertEquals("b", rows.get(1).getActivity().getProject().getName());
    }
}
