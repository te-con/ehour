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

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * TODO 
 **/

public class TimesheetCommentId implements Serializable, Comparable<TimesheetCommentId>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	Integer	userId;
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
