/**
 * Created on Mar 29, 2007
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

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * TODO 
 **/

public class ProjectAssignmentType extends DomainObject<Integer, ProjectAssignmentType>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4306635642163206242L;
	
	private	Integer	assignmentTypeId;
	private	String	assignmentType;
	
	public ProjectAssignmentType()
	{
		
	}
	
	public ProjectAssignmentType(Integer assignmentTypeId)
	{
		this.assignmentTypeId = assignmentTypeId;
	}
	/**
	 * @return the assignmentType
	 */
	public String getAssignmentType()
	{
		return assignmentType;
	}
	/**
	 * @param assignmentType the assignmentType to set
	 */
	public void setAssignmentType(String assignmentType)
	{
		this.assignmentType = assignmentType;
	}
	/**
	 * @return the assignmentTypeId
	 */
	public Integer getAssignmentTypeId()
	{
		return assignmentTypeId;
	}
	/**
	 * @param assignmentTypeId the assignmentTypeId to set
	 */
	public void setAssignmentTypeId(Integer assignmentTypeId)
	{
		this.assignmentTypeId = assignmentTypeId;
	}

	@Override
	public Integer getPK()
	{
		return getAssignmentTypeId();
	}
	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(ProjectAssignmentType type)
	{
		return new CompareToBuilder()
				.append(this.assignmentType, type.assignmentType)
				.append(this.assignmentTypeId, type.assignmentTypeId).toComparison();
	}
	
	public boolean isDefaultAssignmentType()
	{
		return this.assignmentTypeId.equals(ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT);
	}
}
