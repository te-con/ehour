/**
 * Created on Mar 10, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.error;

import java.io.Serializable;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;

/**
 * Error info 
 **/

public class ErrorInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum ErrorCode
	{
		OVER_ALLOTTED_FIXED,
		OVER_OVERRUN_FLEX,
		OVER_DEADLINE_TIME,
		IN_OVERRUN,
		BEFORE_START,
		AFTER_DEADLINE;
	}
	
	private ErrorCode			errorCode;
	private ProjectAssignment	assignment;
	private TimesheetEntry		timesheetEntry;
	private Object[]			parameters;

	/**
	 * 
	 * @param errorCode
	 */
	public ErrorInfo(ErrorCode errorCode)
	{
		this(errorCode, null);
	}	
	
	/**
	 * 
	 * @param errorCode
	 * @param parameters
	 */
	public ErrorInfo(ErrorCode errorCode, Object[] parameters)
	{
		this.errorCode = errorCode;
		this.parameters = parameters;
	}

	/**
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode()
	{
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(ErrorCode errorCode)
	{
		this.errorCode = errorCode;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters()
	{
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Object[] parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * @return the assignment
	 */
	public ProjectAssignment getAssignment()
	{
		return assignment;
	}

	/**
	 * @param assignment the assignment to set
	 */
	public void setAssignment(ProjectAssignment assignment)
	{
		this.assignment = assignment;
	}

	/**
	 * @return the timesheetEntry
	 */
	public TimesheetEntry getTimesheetEntry()
	{
		return timesheetEntry;
	}

	/**
	 * @param timesheetEntry the timesheetEntry to set
	 */
	public void setTimesheetEntry(TimesheetEntry timesheetEntry)
	{
		this.timesheetEntry = timesheetEntry;
	}
	
	 
}
