/**
 * Created by Thies Edeling
 * Created on Dec 9, 2007
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.common.panel.customer.form;

import net.rrm.ehour.ui.common.ajax.AjaxEventType;

/**
 * Ajax event types
 **/

public enum CustomerAjaxEventType implements AjaxEventType
{
	CUSTOMER_NEW_PANEL_REQUEST,
	CUSTOMER_UPDATED,
	CUSTOMER_DELETED;
}
