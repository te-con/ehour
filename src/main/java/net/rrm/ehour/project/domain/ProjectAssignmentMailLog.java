/**
 * Created on Apr 6, 2007
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

package net.rrm.ehour.project.domain;

import java.util.Date;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.mail.domain.MailType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Project assignment mail log 
 **/

public class ProjectAssignmentMailLog extends DomainObject<Integer, ProjectAssignmentMailLog>
{

	private Integer				mailLogId;
	private MailType			mailType;
	private	ProjectAssignment	projectAssignment;
	private	Date				timestamp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4113062080723933724L;

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getPK()
	 */
	@Override
	public Integer getPK()
	{
		return mailLogId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ProjectAssignmentMailLog o)
	{
		return timestamp.compareTo(o.getTimestamp());
	}

	/**
	 * @return the mailLogId
	 */
	public Integer getMailLogId()
	{
		return mailLogId;
	}

	/**
	 * @param mailLogId the mailLogId to set
	 */
	public void setMailLogId(Integer mailLogId)
	{
		this.mailLogId = mailLogId;
	}

	/**
	 * @return the mailType
	 */
	public MailType getMailType()
	{
		return mailType;
	}

	/**
	 * @param mailType the mailType to set
	 */
	public void setMailType(MailType mailType)
	{
		this.mailType = mailType;
	}

	/**
	 * @return the projectAssignment
	 */
	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}

	/**
	 * @param projectAssignment the projectAssignment to set
	 */
	public void setProjectAssignment(ProjectAssignment projectAssignment)
	{
		this.projectAssignment = projectAssignment;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof ProjectAssignmentMailLog))
		{
			return false;
		}
		ProjectAssignmentMailLog rhs = (ProjectAssignmentMailLog) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.timestamp, rhs.timestamp).append(this.projectAssignment, rhs.projectAssignment).append(this.mailLogId, rhs.mailLogId).append(this.mailType, rhs.mailType).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(-1991450885, -924740335).appendSuper(super.hashCode()).append(this.timestamp).append(this.projectAssignment).append(this.mailLogId).append(this.mailType).toHashCode();
	}

}
