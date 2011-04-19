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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class TimesheetCommentId implements Serializable, Comparable<TimesheetCommentId>
{
	private static final long serialVersionUID = 1L;

    @Column(name = "USER_ID", nullable = false)
    @NotNull
	private	Integer	userId;

    @Column(name = "COMMENT_DATE", nullable = false)
    @NotNull
	private	Date	commentDate;
	
	public TimesheetCommentId()
	{
		
	}
	
	public TimesheetCommentId(Integer userId, Date commentDate)
	{
		setUserId(userId);
		setCommentDate(commentDate);
	}
	
	
	/**
	 * @return the commentDate
	 */
	public Date getCommentDate()
	{
		return commentDate;
	}
	/**
	 * @param commentDate the commentDate to set
	 */
	public final void setCommentDate(Date commentDate)
	{
		this.commentDate = commentDate;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public final void setUserId(Integer userId)
	{
		this.userId = userId;
	}
	
	public String toString()
	{
		return new ToStringBuilder(this).append("userId", getUserId())
										.append("commentDate", getCommentDate()).toString();
	}

	public boolean equals(Object other)
	{
		if ((this == other))
		{
			return true;
		}
		
		if (!(other instanceof TimesheetCommentId))
		{
			return false;
		}
		
		TimesheetCommentId castOther = (TimesheetCommentId) other;

		return new EqualsBuilder().append(this.getUserId(), castOther.getUserId()).append(this.getCommentDate(), castOther.getCommentDate()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getUserId()).append(getCommentDate()).toHashCode();
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(TimesheetCommentId object)
	{
		return new CompareToBuilder()
			.append(this.getCommentDate(), object.getCommentDate())
			.append(this.getUserId(), object.getUserId()).toComparison();
	}

	
}
