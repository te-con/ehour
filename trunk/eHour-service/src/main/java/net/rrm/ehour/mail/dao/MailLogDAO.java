/**
 * Created on Apr 7, 2007
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

package net.rrm.ehour.mail.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.domain.MailLogAssignment;

/**
 * DAO for MailLog db operations 
 **/

public interface MailLogDAO  extends GenericDAO<MailLog, Integer>
{
	/**
	 * Get mail log on project assignment id's
	 * @param projectAssignmentId
	 * @return
	 */
	public List<MailLogAssignment> findMailLogOnAssignmentIds(Integer[] projectAssignmentId);	
}
