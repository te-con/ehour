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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * User Preferences domain object
 **/
@Entity
@Table(name = "USER_PREFERENCES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserPreference extends DomainObject<Integer, UserPreference> {
	private static final long serialVersionUID = -5457250186090868408L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_PREFERENCE_ID")
	private Integer userPreferenceId;

	@ManyToOne
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "USER_PREFERENCE_TYPE", length = 32)
	private UserPreferenceType userPreferenceType;

	public UserPreference(User user, UserPreferenceType userPreferenceType) {
		this.user = user;
		this.userPreferenceType = userPreferenceType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rrm.ehour.domain.DomainObject#getPK()
	 */
	@Override
	public Integer getPK() {
		return userPreferenceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(UserPreference object) {
		return new CompareToBuilder().append(this.getUser(), object.getUser()).toComparison();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rrm.ehour.domain.DomainObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		UserPreference castOther;

		if (other instanceof UserPreference) {
			castOther = (UserPreference) other;
			return new EqualsBuilder().append(this.getUser(), castOther.getUser()).isEquals();
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rrm.ehour.domain.DomainObject#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getUser()).toHashCode();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUserPreferenceId() {
		return userPreferenceId;
	}

	public void setUserPreferenceId(Integer userPreferenceId) {
		this.userPreferenceId = userPreferenceId;
	}

	public UserPreferenceType getUserPreferenceType() {
		return userPreferenceType;
	}

	public void setUserPreferenceType(UserPreferenceType userPreferenceType) {
		this.userPreferenceType = userPreferenceType;
	}

}
