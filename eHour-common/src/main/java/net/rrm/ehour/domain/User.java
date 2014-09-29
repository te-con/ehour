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

    @Column(name = "ACTIVE")
    @Type(type = "yes_no")
    private Boolean active = Boolean.TRUE;

    @Column(name = "DN")
    private String dn;

    @Column(name = "FULLNAME")
    private String name;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "assignedUser")
    private Set<Activity> activities;

    @ManyToMany(targetEntity = UserRole.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_TO_USERROLE", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE"))
    private Set<UserRole> userRoles = new HashSet<UserRole>();

    @Transient
    private String email;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(String username) {
        this.username = username;
    }

    public User(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
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

    @Override
    public String getFullName() {
        return getName();
    }


    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }
    // Property accessors

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public Integer getPK() {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", getUserId()).append("username", getUsername()).append("fullName", getFullName()).toString();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(User object) {
        return new CompareToBuilder()
                .append(this.getFullName(), object.getFullName())
                .append(this.getUserId(), object.getUserId())
                .toComparison();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof User)) {
            return false;
        }

        User castOther = (User) other;
        return new EqualsBuilder().append(username, castOther.username)
                .append(name, castOther.name)
                .append(dn, castOther.dn).append(
                        active, castOther.active).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(username).append(name).append(dn).append(active).toHashCode();
    }
}
