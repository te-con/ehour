/**
 * Created on Dec 28, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.timesheet.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import net.rrm.ehour.domain.DomainObject;

/**
 * TODO 
 **/

public class TimesheetComment extends DomainObject<TimesheetCommentId>
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
}
