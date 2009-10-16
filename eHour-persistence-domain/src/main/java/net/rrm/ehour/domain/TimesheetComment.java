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

public class TimesheetComment extends DomainObject<TimesheetCommentId, TimesheetComment>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7067367393719500506L;
	private TimesheetCommentId	commentId;
	private	String				comment;
	private Boolean				newComment = Boolean.FALSE; // not persisted
	
	/**
	 * @return the newComment
	 */
	public Boolean getNewComment()
	{
		return newComment;
	}

	/**
	 * @param newComment the newComment to set
	 */
	public void setNewComment(Boolean newComment)
	{
		this.newComment = newComment;
	}

	public TimesheetComment()
	{
		
	}
	
	public TimesheetComment(TimesheetCommentId id, String comment)
	{
		setComment(comment);
		setCommentId(id);
	}
	/**
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	/**
	 * @return the commentId
	 */
	public TimesheetCommentId getCommentId()
	{
		return commentId;
	}
	/**
	 * @param commentId the commentId to set
	 */
	public void setCommentId(TimesheetCommentId commentId)
	{
		this.commentId = commentId;
	}
	
	public String toString()
	{
		return new ToStringBuilder(this).append("commentId", getCommentId())
										.append("comment", getComment()).toString();
	}

	public boolean equals(Object other)
	{
		if ((this == other))
		{
			return true;
		}
		
		if (!(other instanceof TimesheetComment))
		{
			return false;
		}
		
		TimesheetComment castOther = (TimesheetComment) other;

		return new EqualsBuilder().append(this.getCommentId(),castOther.getCommentId())
									.append(this.getComment(), castOther.getComment()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getCommentId()).append(getComment()).toHashCode();
	}

	@Override
	public TimesheetCommentId getPK()
	{
		return commentId;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(TimesheetComment object)
	{
		return new CompareToBuilder()
			.append(this.getComment(), object.getComment())
			.append(this.getCommentId(), object.getCommentId()).toComparison();
	}

}
