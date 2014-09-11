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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
public class UserPreference extends DomainObject<String, UserPreference> {
	private static final long serialVersionUID = -5457250186090868408L;

	@Id
	@Column(name = "USER_PREFERENCE_KEY", nullable = false, length = 255)
	@NotNull
	private String userPreferenceKey;

	@Column(name = "USER_PREFERENCE_VALUE" , nullable = false, length = 255)
	@NotNull
	private String userPreferenceValue;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	public UserPreference(User user, UserPreferenceType userPreferenceType) {
		this.user = user;
		this.userPreferenceValue = userPreferenceType.getUserPreferenceValueType().name();
		this.userPreferenceKey = userPreferenceType.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rrm.ehour.domain.DomainObject#getPK()
	 */
	@Override
	public String getPK() {
		return userPreferenceKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(UserPreference object) {
		return new CompareToBuilder().append(this.getUserPreferenceKey(), object.getUserPreferenceKey())
				.append(this.getUser(), object.getUser()).toComparison();
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
			return new EqualsBuilder().append(this.getUserPreferenceKey(), ((UserPreference) other).getUserPreferenceKey())
					.append(this.getUser(), castOther.getUser()).isEquals();
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
		return new HashCodeBuilder().append(getUserPreferenceKey()).append(getUser()).toHashCode();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserPreferenceKey() {
		return userPreferenceKey;
	}

	public void setUserPreferenceKey(String userPreferenceKey) {
		this.userPreferenceKey = userPreferenceKey;
	}

	public String getUserPreferenceValue() {
		return userPreferenceValue;
	}

	public void setUserPreferenceValue(String userPreferenceValue) {
		this.userPreferenceValue = userPreferenceValue;
	}

}