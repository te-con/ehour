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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

// Generated Sep 26, 2006 11:58:17 PM by Hibernate Tools 3.2.0.beta7

@Entity
@Table(name = "USER_DEPARTMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserDepartment extends DomainObject<Integer, UserDepartment> {
    private static final long serialVersionUID = 7802944013593353L;

    @Transient
    private boolean deletable;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DEPARTMENT_ID")
    private Integer departmentId;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 512)
    private String name;

    @NotNull
    @Column(name = "CODE", nullable = false, length = 64)
    private String code;

    @ManyToOne(optional = true)
    @JoinColumn(name = "MANAGER_USER_ID")
    private User manager;

    @Column(name = "timezone", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTimeZoneAsString")
    private DateTimeZone timeZone;

    @ManyToOne(optional = true)
    @JoinColumn(name = "PARENT_DEPARTMENT_ID")
    private UserDepartment parentUserDepartment;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "parentUserDepartment", orphanRemoval = true)
    private Set<UserDepartment> childrenUserDepartments;

    @ManyToMany(mappedBy = "userDepartments")
    private Set<User> users;

    public UserDepartment() {
    }

    public UserDepartment(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public UserDepartment(Integer departmentId, String name, String code) {
        this.departmentId = departmentId;
        this.name = name;
        this.code = code;
    }

    public UserDepartment(Integer departmentId, String name, String code, Set<User> users) {
        this.departmentId = departmentId;
        this.name = name;
        this.code = code;
        this.users = users;
    }

    public boolean isRoot() {
        return parentUserDepartment == null;
    }

    public Integer getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getFullName() {
        return getCode() + " - " + getName();
    }

    @Override
    public Integer getPK() {
        return departmentId;
    }



    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserDepartment)) {
            return false;
        }

        UserDepartment o = (UserDepartment) object;

        return new EqualsBuilder()
                .append(getCode(), o.getCode())
                .append(getName(), o.getName())
                .append(getManager(), o.getManager())
                .append(getTimeZone(), o.getTimeZone())
                .append(getParentUserDepartment(), o.getParentUserDepartment())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getCode())
                .append(getName())
                .append(getManager())
                .append(getTimeZone())
                .append(getParentUserDepartment())
                .toHashCode();
    }

    @Override
    public int compareTo(UserDepartment o) {
        return new CompareToBuilder()
                .append(getCode(), o.getCode())
                .append(getName(), o.getName())
                .toComparison();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", code, name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public DateTimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(DateTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public UserDepartment getParentUserDepartment() {
        return parentUserDepartment;
    }

    public void setParentUserDepartment(UserDepartment parentUserDepartment) {
        this.parentUserDepartment = parentUserDepartment;
    }

    public Set<UserDepartment> getChildrenUserDepartments() {
        return childrenUserDepartments;
    }

    public void setChildrenUserDepartments(Set<UserDepartment> childrenUserDepartments) {
        this.childrenUserDepartments = childrenUserDepartments;
    }
}
