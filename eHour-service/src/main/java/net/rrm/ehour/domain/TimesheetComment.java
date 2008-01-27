/**
 * Created on Dec 28, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * TODO 
 **/

public class TimesheetComment extends DomainObject<TimesheetCommentId, TimesheetComment>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7067367393719500506L;
	private TimesheetCommentId	commentId;
	private	String				comment;
	
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
			.append(this.comment, object.comment)
			.append(this.commentId, object.commentId).toComparison();
	}

}
