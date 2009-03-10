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

package net.rrm.ehour.exception;

import net.rrm.ehour.project.status.ProjectAssignmentStatus;

/**
 * Over budget exception
 **/

public class OverBudgetException extends BusinessException
{
	private static final long serialVersionUID = 1L;
	private ProjectAssignmentStatus status;
	

	public OverBudgetException()
	{
		super();
	}
	
	public OverBudgetException(ProjectAssignmentStatus status)
	{
		super();
		
		this.status = status;
	}


	/**
	 * @return the status
	 */
	public ProjectAssignmentStatus getStatus()
	{
		return status;
	}

}
