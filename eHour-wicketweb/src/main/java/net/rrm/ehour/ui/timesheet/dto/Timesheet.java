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

package net.rrm.ehour.ui.timesheet.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.domain.*;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Representation of a timesheet
 */

public class Timesheet implements Serializable {
    private static final long serialVersionUID = -547682050331580675L;
    private static final Integer PROJECTS_PER_PAGE = 20;

    private TimesheetProjects projects;
    private Date[] dateSequence;
    private Date weekStart;
    private Date weekEnd;
    private User user;
    private TimesheetComment comment;
    private float maxHoursPerDay;
    private List<Date> lockedDays;
    private Integer page = 0;
    private String filter;
    private transient List<Project> filteredProjects;
    private transient String filteredFor;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getMaxPages() {
        int size = filterProjects().size();
        double pages = (double) size / PROJECTS_PER_PAGE;
        return (int) Math.ceil(pages) - 1; // zero based
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public boolean isLocked(int seq) {
        return lockedDays.contains(dateSequence[seq]);
    }

    public void setLockedDays(List<Date> lockedDays) {
        this.lockedDays = lockedDays;
    }

    private String activityFilter;

    public void setActivityFilter(String activityFilter) {
        this.activityFilter = activityFilter;
    }

    /**
     * Update failed projects
     */
    public void updateFailedProjects(List<ActivityStatus> failedProjectStatusses) {
        clearAssignmentStatus();

        for (ActivityStatus activityStatus : failedProjectStatusses) {
            setAssignmentStatus(activityStatus);
        }
    }

    /**
     * Set assignment status
     *
     * @param status
     */
    private void setAssignmentStatus(ActivityStatus status)
    {
        for (Project project : projects.get())
        {
            for (TimesheetRow row : projects.getTimesheetRow(project))
            {
                if (row.getActivity().equals(status.getAggregate().getActivity()))
                {
                    row.setActivityStatus(status);
                    return;
                }
            }
        }
    }

    /**
     * Clear each assignment status
     */
    private void clearAssignmentStatus()
    {
        for (Project project : projects.get())
        {
            for (TimesheetRow row : projects.getTimesheetRow(project))
            {
                row.setActivityStatus(null);
            }
        }
    }


    /**
     * Get comment for persist
     *
     * @return
     */
    public TimesheetComment getCommentForPersist() {
        // check comment id
        if (getComment().getCommentId() == null) {
            TimesheetCommentId id = new TimesheetCommentId();
            id.setUserId(getUser().getUserId());
            id.setCommentDate(getWeekStart());

            getComment().setCommentId(id);
        }

        return getComment();
    }

    /**
     * Get the timesheet entries of this timesheet
     *
     * @return
     */
    public List<TimesheetEntry> getTimesheetEntries()
    {
        List<TimesheetEntry> timesheetEntries = new ArrayList<TimesheetEntry>();

        for (List<TimesheetRow> list : getProjects().getTimesheetRows()) {
            for (TimesheetRow timesheetRow : list)
            {
                timesheetEntries.addAll(timesheetRow.getTimesheetEntries());
            }
        }

        return timesheetEntries;
    }

    /**
     * Get total booked hours
     *
     * @return
     */
    public Float getTotalBookedHours()
    {
        float totalHours = 0;

        for (Project project : projects.get())
        {
            for (TimesheetRow row: projects.getTimesheetRow(project))
            {
                for (TimesheetCell cell : row.getTimesheetCells())
                {
                    if (cell != null
                            && cell.getTimesheetEntry() != null
                            && cell.getTimesheetEntry().getHours() != null)
                    {
                        totalHours += cell.getTimesheetEntry().getHours();
                    }
                }
            }
        }

        return totalHours;
    }


    /**
     * @return the weekStart
     */
    public Date getWeekStart() {
        return weekStart;
    }

    /**
     * @param weekStart the weekStart to set
     */
    public void setWeekStart(Date weekStart) {
        this.weekStart = weekStart;
    }

    /**
     * @return the comment
     */
    public TimesheetComment getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(TimesheetComment comment) {
        this.comment = comment;
    }

    /**
     * @return the customers
     */
    public TimesheetProjects getProjects()
    {
        return projects;
    }

    @SuppressWarnings("UnusedDeclaration")
    public List<Project> getProjectList() {
        int start = page * PROJECTS_PER_PAGE;

        List<Project> filteredProjects = filterProjects();

        if (start > filteredProjects.size()) {
            setPage(getMaxPages());
            return getProjectList();
        }

        int end = start + PROJECTS_PER_PAGE;

        if (end > filteredProjects.size()) {
            end = filteredProjects.size();
        }


        return filteredProjects.size() > PROJECTS_PER_PAGE ? filteredProjects.subList(start, end) : filteredProjects;
    }

    private synchronized List<Project> filterProjects() {
        if (filteredProjects == null ||
                (filter == null && null == filteredFor) ||
                (filter != null && !filter.equalsIgnoreCase(filteredFor))) {
            List<Project> projects = getProjects().get();

            if (StringUtils.isNotBlank(filter)) {
                String lowerCaseFilter = filter.toLowerCase();

                List<Project> matchingProjects = filterOnProjectNameAndCode(projects, lowerCaseFilter);

                projects.removeAll(matchingProjects);

                filterOnActivityNameAndCode(projects, lowerCaseFilter, matchingProjects);

                filteredProjects = matchingProjects;
            } else {
                filteredProjects = projects;
            }

            filteredFor = filter;
        }

        return filteredProjects;
    }

    private void filterOnActivityNameAndCode(List<Project> projects, String lowerCaseFilter, List<Project> matchingProjects) {
        for (Project project : projects) {
            Set<Activity> matchingActivities = Sets.newHashSet();

            for (Activity activity : project.getActivities()) {
                if (activity.getName().toLowerCase().contains(lowerCaseFilter) ||
                        activity.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    matchingActivities.add(activity);
                }
            }

            if (matchingActivities.size() > 0) {
                Project clonedProject = new Project(project);
                clonedProject.setActivities(matchingActivities);
                matchingProjects.add(clonedProject);
            }
        }
    }

    private List<Project> filterOnProjectNameAndCode(List<Project> projects, String lowerCaseFilter) {
        List<Project> filteredProjects = Lists.newArrayList();

        for (Project project : projects) {
            if (project.getName().toLowerCase().contains(lowerCaseFilter)
                    || project.getProjectCode().toLowerCase().contains(lowerCaseFilter)) {
                filteredProjects.add(project);
            }
        }

        return filteredProjects;
    }

    /**
     * @return the dateSequence
     */
    public Date[] getDateSequence() {
        return dateSequence;
    }

    /**
     * @param dateSequence the dateSequence to set
     */
    public void setDateSequence(Date[] dateSequence) {
        this.dateSequence = dateSequence.clone();
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    public float getMaxHoursPerDay() {
        return maxHoursPerDay;
    }

    public void setMaxHoursPerDay(float maxHoursPerDay) {
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public Date getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(Date weekEnd) {
        this.weekEnd = weekEnd;
    }

    /**
     *
     * @param project
     * @return
     */
    public List<TimesheetRow> getTimesheetRows(Project project)
    {
        return projects.getTimesheetRow(project);
    }


    /**
     * @param projects the customers to set
     */
    public void setProjects(TimesheetProjects projects)
    {
        this.projects = projects;
    }
}
