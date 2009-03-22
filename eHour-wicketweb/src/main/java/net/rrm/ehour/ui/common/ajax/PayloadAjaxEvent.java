/**
 * Created on Dec 9, 2007
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.common.ajax;

/**
 * AjaxEvent with payload
 **/

public class PayloadAjaxEvent<PL> extends AjaxEvent
{
	private static final long serialVersionUID = 4671730483379447278L;
	private PL payload;


	public PayloadAjaxEvent(AjaxEventType eventType, PL payload)
	{
		super(eventType);
		
		this.payload = payload;
	}

	/**
	 * @return the payload
	 */
	public PL getPayload()
	{
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(PL payload)
	{
		this.payload = payload;
	}
}
