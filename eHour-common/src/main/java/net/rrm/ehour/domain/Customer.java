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
@Table(name = "CUSTOMER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer extends DomainObject<Integer, Customer> {
    private static final long serialVersionUID = 7179070624535327915L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "CODE", nullable = false, length = 32)
    @NotNull
    private String code;

    @Column(name = "NAME", nullable = false, length = 255)
    @NotNull
    private String name;

    @Column(name = "DESCRIPTION", length = 1024)
    private String description;

    @Column(name = "ACTIVE")
    @Type(type = "yes_no")
    private Boolean active = Boolean.TRUE;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "customer")
    private Set<Project> projects;

    @Transient
    private boolean deletable;

    // Constructors

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("customerId", customerId)
                .append("code", code)
                .append("name", name)
                .append("active", active)
                .toString();
    }

    /**
     * default constructor
     */
    public Customer() {
    }

    public Customer(Integer customerId) {
        this.customerId = customerId;
    }

    public Customer(Integer customerId, String code, String name) {
        this(customerId);
        this.code = code;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Customer(String code, String name, String description, boolean active) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    /**
     * full constructor
     */
    public Customer(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.code = customer.getCode();
        this.name = customer.getName();
        this.description = customer.getDescription();
        this.active = customer.isActive();
    }

    /*
     * (non-Javadoc)
     * @see net.rrm.ehour.domain.DomainObject#getFullName()
     */
    @Override
    public String getFullName() {
        return (getCode() != null) ? getCode() + " - " + getName() : getName();
    }

    // Property accessors
    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the projects
     */
    public Set<Project> getProjects() {
        return projects;
    }

    /**
     * Get only active projects
     *
     * @return
     */
    public Set<Project> getActiveProjects() {
        Set<Project> activeProjects = new HashSet<>();

        if (getProjects() != null) {
            for (Project project : getProjects()) {
                if (project.isActive()) {
                    activeProjects.add(project);
                }
            }
        }

        return activeProjects;

    }

    /**
     * @param projects the projects to set
     */
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
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
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(Customer object) {
        return new CompareToBuilder()
                .append(this.getNameLowerCase(), object.getNameLowerCase())
                .append(this.getCodeLowerCase(), object.getCodeLowerCase())
                .append(this.getCustomerId(), object.getCustomerId()).toComparison();
    }

    private String getNameLowerCase() {
        return getName() == null ? null : getName().toLowerCase();
    }

    private String getCodeLowerCase() {
        return getCode() == null ? null : getCode().toLowerCase();
    }


    @Override
    public Integer getPK() {
        return customerId;
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

    public void addProject(Project project) {
        if (projects == null) {
            projects = new HashSet<>();
        }

        projects.add(project);

        project.setCustomer(this);
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Customer))
            return false;
        Customer castOther = (Customer) other;
        return new EqualsBuilder().append(code, castOther.code).append(name, castOther.name).append(description, castOther.description).append(active, castOther.active).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).append(name).append(description).append(active).toHashCode();
    }

}
