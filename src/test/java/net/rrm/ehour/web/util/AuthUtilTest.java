/**
 * Created on Nov 12, 2006
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

package net.rrm.ehour.web.util;

import junit.framework.TestCase;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
/**
 * TODO 
 **/

public class AuthUtilTest extends TestCase
{
	public void testHasRole()
	{
		String	role = WebConstants.ROLE_CONSULTANT;
		GrantedAuthority[] ga = new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_ADMIN"),
				new GrantedAuthorityImpl("ROLE_CONSULTANT")};
		
		boolean t = AuthUtil.hasRole(role, ga);
		
		assertTrue(t);
		
	}

}
