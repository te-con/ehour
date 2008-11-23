/**
 * Created on Mar 9, 2008
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

package net.rrm.ehour.ui.common.renderers;

import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.util.CommonWebUtil;

/**
 * TODO 
 **/

public class UserRoleRenderer  extends LocalizedResourceRenderer
{
	private static final long serialVersionUID = -4020935210828625185L;
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.renderers.LocalizedResourceRenderer#getResourceKey(java.lang.Object)
	 */
	@Override
	protected String getResourceKey(Object o)
	{
		UserRole role = (UserRole)o;
		
		return CommonWebUtil.getResourceKeyForUserRole(role);
	}

}
