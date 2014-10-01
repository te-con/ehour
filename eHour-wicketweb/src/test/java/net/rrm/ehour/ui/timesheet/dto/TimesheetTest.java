package net.rrm.ehour.ui.timesheet.dto;

import com.google.common.collect.Lists;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ActivityMother;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectObjectMother;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TimesheetTest {
    @Test
    public void should_return_2_max_pages_for_35_projects() {
        Timesheet timesheet = createTimesheet(35);

        assertEquals(2, timesheet.getMaxPages());
    }

    @Test
    public void should_return_last_page_of_6_rows() {
        Timesheet timesheet = createTimesheet(26);
        timesheet.setPage(1);

        assertEquals(6, timesheet.getProjectList().size());
    }

    @Test
    public void should_filter_on_project_name() {
        Timesheet timesheet = createTimesheet(26);
        timesheet.setFilter("pn_A");

        List<Project> projectList = timesheet.getProjectList();
        assertEquals(1, projectList.size());
        assertEquals("pn_A", projectList.get(0).getName());
    }

    @Test
    public void should_filter_on_project_code() {
        Timesheet timesheet = createTimesheet(26);
        timesheet.setFilter("pc_A");

        List<Project> projectList = timesheet.getProjectList();
        assertEquals(1, projectList.size());
        assertEquals("pc_A", projectList.get(0).getProjectCode());
    }

    @Test
    public void should_filter_on_activity_name() {
        Timesheet timesheet = createTimesheet(26);
        timesheet.setFilter("an_A");

        List<Project> projectList = timesheet.getProjectList();
        assertEquals(1, projectList.size());
        assertEquals(1, projectList.get(0).getActivities().size());
        assertEquals("an_A", projectList.get(0).getActivities().iterator().next().getName());
    }

    @Test
    public void should_filter_on_activity_code() {
        Timesheet timesheet = createTimesheet(26);
        timesheet.setFilter("ac_A");

        List<Project> projectList = timesheet.getProjectList();
        assertEquals(1, projectList.size());
        assertEquals(1, projectList.get(0).getActivities().size());
        assertEquals("ac_A", projectList.get(0).getActivities().iterator().next().getCode());
    }

    @Test
    public void should_show_all_activites_for_project_when_project_matches() {
        EhourConfigStub config = new EhourConfigStub();

        Project project = ProjectObjectMother.createProject(1);
        project.setName("match_A");

        List<TimesheetRow> rows = Lists.newArrayList();

        for (int i = 65; i < 68; i++) {
            Activity activity = ActivityMother.createActivity(i);
            activity.setName(Character.toString((char) i));
            activity.setProject(project);
            activity.setName("an_" + Character.toString((char) i));
            activity.setCode("ac_" + Character.toString((char) i));

            TimesheetRow row = new TimesheetRow(config);
            row.setActivity(activity);

            rows.add(row);
        }

        Project otherProject = ProjectObjectMother.createProject(1);
        otherProject.setName("other");

        for (int i = 65; i < 67; i++) {
            Activity activity = ActivityMother.createActivity(i);
            activity.setName(Character.toString((char) i));
            activity.setProject(otherProject);
            activity.setName("match_" + Character.toString((char) i));
            activity.setCode("ac_" + Character.toString((char) i));

            TimesheetRow row = new TimesheetRow(config);
            row.setActivity(activity);

            rows.add(row);
        }

        TimesheetProjects projects = TimesheetProjects$.MODULE$.apply(rows);

        Timesheet timesheet = new Timesheet();
        timesheet.setProjects(projects);
        timesheet.setFilter("match_A");

        List<Project> projectList = timesheet.getProjectList();
        assertEquals(2, projectList.size());

        // hit on project will list all activities for that project
        assertEquals(3, projectList.get(0).getActivities().size());

        // hit on activity will list only that activity
        assertEquals(1, projectList.get(1).getActivities().size());
    }

    @Test
    public void filter_should_influence_max_pages() {
        Timesheet timesheet = createTimesheet(26);
        timesheet.setFilter("an_A");

        assertEquals(1, timesheet.getMaxPages());
    }

    @Test
    public void should_set_page_to_last_page_when_invalid_page_is_provided() {
        Timesheet timesheet = createTimesheet(26);
        timesheet.setPage(50);

        assertEquals(6, timesheet.getProjectList().size());
        assertEquals(1, timesheet.getPage().intValue());
    }

    private Timesheet createTimesheet(int rowCount) {
        EhourConfigStub config = new EhourConfigStub();

        List<TimesheetRow> rows = Lists.newArrayList();

        for (int i = 65; i < 65 + rowCount; i++) {
            Activity activity = ActivityMother.createActivity(i);
            activity.setName(Character.toString((char) i));
            activity.getProject().setName("pn_" + Character.toString((char) i));
            activity.getProject().setProjectCode("pc_" + Character.toString((char) i));
            activity.setName("an_" + Character.toString((char) i));
            activity.setCode("ac_" + Character.toString((char) i));

            TimesheetRow row = new TimesheetRow(config);
            row.setActivity(activity);

            rows.add(row);
        }

        TimesheetProjects projects = TimesheetProjects$.MODULE$.apply(rows);

        Timesheet timesheet = new Timesheet();
        timesheet.setProjects(projects);
        return timesheet;
    }

}