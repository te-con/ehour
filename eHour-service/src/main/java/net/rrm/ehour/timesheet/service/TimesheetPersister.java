/**
 * Created on Mar 14, 2008
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

package net.rrm.ehour.timesheet.service;

import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.exception.BusinessException;

/**
 * Timesheet persister & validator
 **/

public interface TimesheetPersister
{
	/**
	 * Validate, persist and notify list of timesheet entries
	 * @param assignment
	 * @param entries
	 * @throws BusinessException
	 */
	public void persistAndNotify(ProjectAssignment assignment, List<TimesheetEntry> entries) throws BusinessException;

}
