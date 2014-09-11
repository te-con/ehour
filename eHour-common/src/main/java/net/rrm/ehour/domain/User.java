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

/**
 * @author Thies
 */
@Entity
@Table(name = "USERS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends DomainObject<Integer, User> {
    private static final long serialVersionUID = 2546435367535412269L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Integer userId;

    @NotNull
    @Column(name = "USERNAME", length = 64)
    private String username;

    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 128)
    private String password;

    @Column(name = "FIRST_NAME", length = 64)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 64)
    private String lastName;

    @Column(name = "EMAIL", length = 128)
    private String email;

    @Column(name = "ACTIVE")
    @Type(type = "yes_no")
    private Boolean active = Boolean.TRUE;

    @Column(name = "SALT")
    private Integer salt;

    @Transient
    private String updatedPassword;

    @ManyToMany(targetEntity = UserRole.class,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
            )
    @JoinTable(name = "USER_TO_USERROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE"))
    private Set<UserRole> userRoles = new HashSet<UserRole>();

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    @NotNull
    private UserDepartment userDepartment;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<ProjectAssignment> projectAssignments;

	@ManyToMany(targetEntity = Customer.class, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable(name = "CUSTOMER_REVIEWERS", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID"))
	private Set<Customer> customers = new HashSet<Customer>();

	@Transient
	private Set<ProjectAssignment> inactiveProjectAssignments;

	@Transient
	private boolean deletable;

	// Constructors
    public User() {
    }

    public User(UserDepartment userDepartment) {
        this.userDepartment = userDepartment;
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Integer userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String username, String password, String firstName, String lastName, String email, boolean active, Set<UserRole> userRoles, UserDepartment userDepartment) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.userRoles = userRoles;
        this.userDepartment = userDepartment;
    }

    public User addUserRole(UserRole role) {
        if (userRoles == null) {
            userRoles = new HashSet<UserRole>();
        }
        userRoles.add(role);

        return this;
    }

    public User deleteUserRole(UserRole role) {
        if (userRoles != null) {
            userRoles.remove(role);
        }
        return this;
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();

        if (!StringUtils.isBlank(lastName)) {
            fullName.append(lastName);

            if (!StringUtils.isBlank(firstName)) {
                fullName.append(", ");
            }
        }

        if (!StringUtils.isBlank(firstName)) {
            fullName.append(firstName);
        }

        return fullName.toString();
    }

    // Property accessors

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Set<UserRole> getUserRoles()
    {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles)
    {
        this.userRoles = userRoles;
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
     * @param userDepartment the userDepartment to set
     */
    public void setUserDepartment(UserDepartment userDepartment) {
        this.userDepartment = userDepartment;
    }
	public void addCustomer(Customer customer) {
		if (customers == null) {
			customers = new HashSet<Customer>();
		}
		customers.add(customer);
	}
	
	public Set<Customer> getCustomers() {
		return customers;
	}

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
	/**
	 * @return the userDepartment
	 */
	public UserDepartment getUserDepartment() {
		return userDepartment;
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
     * @return the inactiveProjectAssignments
     */
    public Set<ProjectAssignment> getInactiveProjectAssignments() {
        return inactiveProjectAssignments;
    }

    public void addProjectAssignment(ProjectAssignment projectAssignment) {
        if (projectAssignments == null) {
            projectAssignments = new HashSet<ProjectAssignment>();
        }

        projectAssignments.add(projectAssignment);
    }

    /**
     * @param inactiveProjectAssignments the inactiveProjectAssignments to set
     */
    public void setInactiveProjectAssignments(Set<ProjectAssignment> inactiveProjectAssignments) {
        this.inactiveProjectAssignments = inactiveProjectAssignments;
    }


    @Override
    public Integer getPK() {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", getUserId())
                .append("username", getUsername())
                .append("lastName", getLastName())
                .append("firstName", getFirstName())
                .toString();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(User object) {
        return new CompareToBuilder()
                .append(this.getLastName(), object.getLastName())
                .append(this.getFirstName(), object.getFirstName())
                .append(this.getUserDepartment(), object.getUserDepartment())
                .append(this.getUserId(), object.getUserId())
                .toComparison();
    }

    /**
     * @return the deletable
     */
    public boolean isDeletable()
    {
        return deletable;
    }

    /**
     * @param deletable the deletable to set
     */
    public void setDeletable(boolean deletable)
    {
        this.deletable = deletable;
    }

    /**
     * @return the salt
     */
    public Integer getSalt()
    {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(Integer salt)
    {
        this.salt = salt;
    }

    /**
     * @return the updatedPassword
     */
    public String getUpdatedPassword()
    {
        return updatedPassword;
    }

    /**
     * @param updatedPassword the updatedPassword to set
     */
    public void setUpdatedPassword(String updatedPassword)
    {
        this.updatedPassword = updatedPassword;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof User)) {
            return false;
        }

        User castOther = (User) other;
        return new EqualsBuilder()
                .append(username, castOther.username)
                .append(firstName, castOther.firstName)
                .append(lastName, castOther.lastName)
                .append(email, castOther.email)
                .append(active, castOther.active)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(username).append(firstName).append(lastName).append(email).append(active).toHashCode();
    }
}
