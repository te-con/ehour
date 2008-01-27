/**
 * Created on Mar 29, 2007
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

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Project Assignment type
 **/

public class ProjectAssignmentType extends DomainObject<Integer, ProjectAssignmentType>
{
	// BAD BAD BAD! Copy of EHourConstants. Share it as soon as a util prj exists..
	private final static int ASSIGNMENT_DATE = 0;
	private final static int ASSIGNMENT_TIME_ALLOTTED_FIXED = 2;
	private final static int ASSIGNMENT_TIME_ALLOTTED_FLEX = 3;

	
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
	
	public boolean isDateType()
	{
		return assignmentTypeId.intValue() == ASSIGNMENT_DATE;
	}

	public boolean isFixedAllottedType()
	{
		return assignmentTypeId.intValue() == ASSIGNMENT_TIME_ALLOTTED_FIXED;
	}

	public boolean isFlexAllottedType()
	{
		return assignmentTypeId.intValue() == ASSIGNMENT_TIME_ALLOTTED_FLEX;
	}
	
	public boolean isAllottedType()
	{
		return isFixedAllottedType() || isFlexAllottedType();
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
}
