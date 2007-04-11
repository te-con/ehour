/**
 * Created on Apr 7, 2007
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

package net.rrm.ehour.mail.dao;

import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.mail.domain.MailLogAssignment;

import org.junit.Test;

/**
 * TODO 
 **/

public class MailLogDAOTest extends BaseDAOTest
{
	private MailLogDAO	mailLogDAO;
	
	@Test
	public final void testGetMailLogAssignment()
	{
		List<MailLogAssignment> mla = mailLogDAO.findMailLogOnAssignmentIds(new Integer[]{2});
		
		assertEquals(1, mla.size());
		assertEquals(9, mla.get(0).getMailLogId().intValue());
	}

	/**
	 * @param mailLogDAO the mailLogDAO to set
	 */
	public void setMailLogDAO(MailLogDAO mailLogDAO)
	{
		this.mailLogDAO = mailLogDAO;
	}

}
