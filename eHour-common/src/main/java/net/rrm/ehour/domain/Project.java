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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PROJECT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project extends DomainObject<Integer, Project> {
    private static final long serialVersionUID = 6553709211219335091L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROJECT_ID")
    private Integer projectId;

    @Column(name = "PROJECT_CODE", length = 32, nullable = false)
    @NotNull
    private String projectCode;

    @Column(name = "CONTACT", length = 255)
    private String contact;

    @Column(name = "DESCRIPTION", length = 1024)
    private String description;

    @Column(name = "NAME", length = 255, nullable = false)
    @NotNull
    private String name;

    @Column(name = "DEFAULT_PROJECT")
    @Type(type = "yes_no")
    private Boolean defaultProject = Boolean.FALSE;

    @Column(name = "ACTIVE")
    @Type(type = "yes_no")
    private Boolean active = Boolean.TRUE;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", nullable = true)
    @NotNull
    private Customer customer;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "project")
    @Basic(fetch = FetchType.LAZY)
    private Set<ProjectAssignment> projectAssignments;

    @ManyToOne
    @JoinColumn(name = "PROJECT_MANAGER", nullable = true)
    private User projectManager;

    @Transient
    private boolean deletable;

    @Column(name = "BILLABLE")
    @Type(type = "yes_no")
    private Boolean billable = Boolean.TRUE;

    // Constructors

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    /**
     * default constructor
     */
    public Project() {
    }

    public Project(Integer projectId) {
        this.projectId = projectId;
    }

    public Project(Integer projectId, Customer customer) {
        this(projectId);
        this.customer = customer;
    }


    public String getFullName() {
        return (StringUtils.isBlank(projectCode)) ? name : projectCode + " - " + name;
    }

    public String getFullNameWithCustomer() {
        return getCustomer().getCode() + ": " + ((StringUtils.isBlank(projectCode)) ? name : projectCode + " - " + name);
    }

    /**
     * Get primary key
     */
    public Integer getPK() {
        return projectId;
    }

    // Property accessors

    public Integer getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isDefaultProject() {
        return this.defaultProject;
    }

    public void setDefaultProject(boolean defaultProject) {
        this.defaultProject = defaultProject;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the projectAssignments
     */
    public Set<ProjectAssignment> getProjectAssignments() {
        return projectAssignments;
    }

    /**
     * @param projectAssignments the projectAssignments to set
     */
    public void setProjectAssignments(Set<ProjectAssignment> projectAssignments) {
        this.projectAssignments = projectAssignments;
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(Project object) {
        return new CompareToBuilder()
                .append(this.getName(), object.getName())
                .append(this.getProjectCode(), object.getProjectCode())
                .append(this.getCustomer(), object.getCustomer())
                .append(this.getProjectId(), object.getProjectId()).toComparison();
    }

    /**
     * @return the projectManager
     */
    public User getProjectManager() {
        return projectManager;
    }

    /**
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("active", this.active)
                .append("PK", this.getPK())
                .append("defaultProject", this.defaultProject)
                .append("fullname", this.getFullName())
                .append("projectCode", this.getProjectCode())
                .append("name", this.getName())
                .append("projectId", this.getProjectId())
                .append("billable", this.isBillable())
                .append("pm", this.getProjectManager())
                .toString();
    }

    /**
     * @return the deletable
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * @param deletable the deletable to set
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public void addProjectAssignment(ProjectAssignment assignment) {
        if (projectAssignments == null) {
            projectAssignments = new HashSet<ProjectAssignment>();
        }

        projectAssignments.add(assignment);
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Project)) {
            return false;
        }
        Project castOther = (Project) other;
        return new EqualsBuilder().append(projectCode, castOther.projectCode).append(contact, castOther.contact).append(description, castOther.description).append(name, castOther.name).append(defaultProject, castOther.defaultProject).append(active, castOther.active).append(
                customer, castOther.customer).append(billable, castOther.billable).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(projectCode).append(contact).append(description).append(name).append(defaultProject).append(active).append(customer).append(billable).toHashCode();
    }

}
