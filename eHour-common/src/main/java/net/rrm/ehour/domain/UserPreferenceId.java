package net.rrm.ehour.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class UserPreferenceId implements Serializable, Comparable<UserPreferenceId> {

	private static final long serialVersionUID = -5269531216633225612L;
	
	@Column(name = "USER_PREFERENCE_KEY", nullable = false, length = 255)
	@NotNull
	private String userPreferenceKey;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	@NotNull
	private User user;
	
	public UserPreferenceId(String userPreferenceKey, User user) {
		this.userPreferenceKey = userPreferenceKey;
		this.user = user;
	}
	
	public UserPreferenceId() {
		
	}
	
	@Override
	public boolean equals(Object other) {
		if ((this == other))
        {
            return true;
        }
        if (!(other instanceof UserPreferenceId))
        {
            return false;
        }
        
        UserPreferenceId castOther = (UserPreferenceId) other;
        return new EqualsBuilder().append(this.getUserPreferenceKey(), castOther.getUserPreferenceKey())
                .append(this.getUser(), castOther.getUser()).isEquals();
	}
	
	/**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(UserPreferenceId object)
    {
        return new CompareToBuilder()
                .append(this.getUserPreferenceKey(), object.getUserPreferenceKey())
                .append(this.getUser(), object.getUser())
                .toComparison();
    }
	
	
	@Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getUserPreferenceKey())
                .append(getUser()).toHashCode();
    }
	
	

	public String getUserPreferenceKey() {
		return userPreferenceKey;
	}

	public void setUserPreferenceKey(String userPreferenceKey) {
		this.userPreferenceKey = userPreferenceKey;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
