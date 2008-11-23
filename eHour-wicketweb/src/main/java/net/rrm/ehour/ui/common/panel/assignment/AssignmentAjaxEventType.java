/**
 * Created on May 28, 2008
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

package net.rrm.ehour.ui.common.panel.assignment;

import net.rrm.ehour.ui.common.ajax.AjaxEventType;

/**
 * Assignment ajax event type
 **/

public enum AssignmentAjaxEventType implements AjaxEventType
{
	ASSIGNMENT_UPDATED,
	ASSIGNMENT_DELETED,
	ASSIGNMENT_LIST_CHANGE;
}
