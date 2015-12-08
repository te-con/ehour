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

package net.rrm.ehour.domain;

import net.rrm.ehour.util.EhourConstants;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "PROJECT_ASSIGNMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectAssignment extends DomainObject<Integer, ProjectAssignment> {
    private static final long serialVersionUID = -2396783805401137165L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ASSIGNMENT_ID")
    private Integer assignmentId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    @NotNull
    private Project project;

    @Column(name = "HOURLY_RATE")
    private Float hourlyRate;

    @Column(name = "DATE_START")
    private Date dateStart;

    @Column(name = "DATE_END")
    private Date dateEnd;

    @Column(name = "ROLE", length = 255)
    private String role;

    @ManyToOne
    @JoinColumn(name = "ASSIGNMENT_TYPE_ID")
    @NotNull
    private ProjectAssignmentType assignmentType;

    @Column(name = "ALLOTTED_HOURS")
    private Float allottedHours;

    @Column(name = "ALLOTTED_HOURS_OVERRUN")
    private Float allowedOverrun;

    @Column(name = "NOTIFY_PM_ON_OVERRUN")
    @Type(type = "yes_no")
    private Boolean notifyPm = Boolean.FALSE;

    @Column(name = "ACTIVE", nullable = false)
    @Type(type = "yes_no")
    private Boolean active;

    @Transient
    private boolean deletable;

    // Constructors

    /**
     * default constructor
     */
    public ProjectAssignment() {
    }

    public ProjectAssignment(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    /**
     * minimal constructor
     */
    public ProjectAssignment(User user, Project project) {
        this.user = user;
        this.project = project;
    }

    public ProjectAssignment(User user, Project project, Float hourlyRate) {
        this(user, project);
        this.hourlyRate = hourlyRate;
    }


    /**
     * Create a project assignment with default values (date assignment, no start/end date, active)s
     *
     * @param project
     * @param user
     * @return
     */
    public static ProjectAssignment createProjectAssignment(Project project, User user) {
        ProjectAssignment assignment = new ProjectAssignment();
        assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
        assignment.setProject(project);
        assignment.setUser(user);
        assignment.setActive(true);

        return assignment;
    }

    /**
     * Create a copy of the provided template assignment, replacing the user with the given user
     */
    public static ProjectAssignment createProjectAssignment(ProjectAssignment templateAssignment, User user) {
        ProjectAssignment assignment = createProjectAssignment(templateAssignment);
        assignment.setUser(user);

        return assignment;
    }

    /**
     * Create a copy of the provided template assignment, replacing the project with the given project
     */
    public static ProjectAssignment createProjectAssignment(ProjectAssignment templateAssignment, Project project) {
        ProjectAssignment assignment = createProjectAssignment(templateAssignment);
        assignment.setProject(project);

        return assignment;
    }

    private static ProjectAssignment createProjectAssignment(ProjectAssignment templateAssignment) {
        ProjectAssignment assignment = new ProjectAssignment();
        assignment.setActive(templateAssignment.isActive());
        assignment.setAllottedHours(templateAssignment.getAllottedHours());
        assignment.setAllowedOverrun(templateAssignment.getAllowedOverrun());
        assignment.setAssignmentType(templateAssignment.getAssignmentType());
        assignment.setDateEnd(templateAssignment.getDateEnd());
        assignment.setDateStart(templateAssignment.getDateStart());
        assignment.setHourlyRate(templateAssignment.getHourlyRate());
        assignment.setNotifyPm(templateAssignment.isNotifyPm());
        assignment.setProject(templateAssignment.getProject());
        assignment.setRole(templateAssignment.getRole());
        assignment.setUser(templateAssignment.getUser());
        return assignment;
    }

    public boolean isNew() {
        return getPK() == null;
    }

    @Override
    public String getFullName() {
        return getProject().getFullName();
    }


    public Integer getAssignmentId() {
        return this.assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Float getHourlyRate() {
        return this.hourlyRate;
    }

    public void setHourlyRate(Float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Date getDateStart() {
        return this.dateStart;
    }


    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return this.dateEnd;
    }


    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String description) {
        this.role = description;
    }


    public String toString() {
        return new ToStringBuilder(this).append("assignmentId", getAssignmentId())
                .append("active", isActive())
                .append("project", getProject())
                .append("user", getUser())
//										.append("type", getAssignmentType())
                .append("dateStart", getDateStart())
                .append("dateEnd", getDateEnd())
                .toString();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(ProjectAssignment object) {
        return new CompareToBuilder()
                //sort by column order as displayed in ProjectOverviewPanel
                .append(this.getProject().getCustomer().getName(), object.getProject().getCustomer().getName())
                .append(this.getProject().getProjectCode(), object.getProject().getProjectCode())
                .append(this.getProject().getName(), object.getProject().getName())
                        //here we should already have a sort. Anyway let's continue with some default comparison
                .append(this.getProject(), object.getProject())
                .append(this.getDateEnd(), object.getDateEnd())
                .append(this.getDateStart(), object.getDateStart())
                .append(this.getUser(), object.getUser())
                .append(this.getAssignmentId(), object.getAssignmentId())
                .toComparison();
    }


    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active != null && active;
    }

    /**
     * Whether assignment/project/customer are all active
     *
     * @return
     */
    public boolean isBookable() {
        return isActive() &&
                (getProject() == null || getProject().isActive()) &&
                (getProject() == null || getProject().getCustomer() == null || getProject().getCustomer().isActive());
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Integer getPK() {
        return assignmentId;
    }

    /**
     * @return the allottedHours
     */
    public Float getAllottedHours() {
        return allottedHours;
    }

    /**
     * @param allottedHours the allottedHours to set
     */
    public void setAllottedHours(Float allottedHours) {
        this.allottedHours = allottedHours;
    }

    /**
     * @return the assignmentType
     */
    public ProjectAssignmentType getAssignmentType() {
        return assignmentType;
    }

    /**
     * @param assignmentType the assignmentType to set
     */
    public void setAssignmentType(ProjectAssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    /**
     * @return the allowedOverrun
     */
    public Float getAllowedOverrun() {
        return allowedOverrun;
    }

    /**
     * @param allowedOverrun the allowedOverrun to set
     */
    public void setAllowedOverrun(Float allowedOverrun) {
        this.allowedOverrun = allowedOverrun;
    }

    /**
     * @return the notifyPm
     */
    public boolean isNotifyPm() {
        return notifyPm;
    }

    /**
     * @param notifyPm the notifyPm to set
     */
    public void setNotifyPm(boolean notifyPm) {
        this.notifyPm = notifyPm;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ProjectAssignment)) {
            return false;
        }

        ProjectAssignment castOther = (ProjectAssignment) other;

        return new EqualsBuilder()
                .append(user, castOther.user)
                .append(project, castOther.project)
                .append(hourlyRate, castOther.hourlyRate)
                .append(dateStart, castOther.dateStart)
                .append(dateEnd, castOther.dateEnd)
                .append(role, castOther.role)
                .append(assignmentType, castOther.assignmentType)
                .append(allottedHours, castOther.allottedHours)
                .append(allowedOverrun, castOther.allowedOverrun)
                .append(active, castOther.active)
                .append(getPK(), castOther.getPK())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(user)
                .append(project)
                .append(hourlyRate)
                .append(dateStart)
                .append(dateEnd)
                .append(role)
                .append(assignmentType)
                .append(allottedHours)
                .append(allowedOverrun)
                .append(active)
                .append(assignmentId)
                .toHashCode();
    }

}
