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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

// Generated Sep 26, 2006 11:58:17 PM by Hibernate Tools 3.2.0.beta7

@Entity
@Table(name = "USER_DEPARTMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserDepartment extends DomainObject<Integer, UserDepartment>
{
	private static final long serialVersionUID = 7802944013593352L;

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

    @OneToMany(mappedBy = "userDepartment")
	private Set<User>		users;

	// Constructors

	/** default constructor */
	public UserDepartment()
	{
	}
	
	public UserDepartment(Integer departmentId)
	{
		this.departmentId = departmentId;	
	}

	/** minimal constructor */
	public UserDepartment(Integer departmentId, String name, String code)
	{
		this.departmentId = departmentId;
		this.name = name;
		this.code = code;
	}

	/** full constructor */
	public UserDepartment(Integer departmentId, String name, String code, Set<User> users)
	{
		this.departmentId = departmentId;
		this.name = name;
		this.code = code;
		this.users = users;
	}

	public Integer getDepartmentId()
	{
		return this.departmentId;
	}

	public void setDepartmentId(Integer departmentId)
	{
		this.departmentId = departmentId;
	}

	/**
	 * @return  the name
	 * @uml.property  name="name"
	 */
	public String getName()
	{
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getFullName()
	 */
	@Override
	public String getFullName()
	{
		return getName();
	}

	/**
	 * @param name  the name to set
	 * @uml.property  name="name"
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return  the code
	 * @uml.property  name="code"
	 */
	public String getCode()
	{
		return this.code;
	}

	/**
	 * @param code  the code to set
	 * @uml.property  name="code"
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	public Set<User> getUsers()
	{
		return users;
	}

	public void setUsers(Set<User> users)
	{
		this.users = users;
	}

	@Override
	public Integer getPK()
	{
		return departmentId;
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
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(UserDepartment object)
	{
		return new CompareToBuilder()
			.append(this.getName(), object.getName())
			.append(this.getCode(), object.getCode())
			.append(this.getDepartmentId(), object.getDepartmentId()).toComparison();
	}

	@Override
	public boolean equals(final Object other)
	{
		if (!(other instanceof UserDepartment))
			return false;
		UserDepartment castOther = (UserDepartment) other;
		return new EqualsBuilder().append(name, castOther.name).append(code, castOther.code).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(name).append(code).toHashCode();
	}
	
}
