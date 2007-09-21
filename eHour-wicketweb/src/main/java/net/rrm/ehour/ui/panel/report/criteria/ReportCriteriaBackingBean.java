/**
 * Created on Sep 22, 2007
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

package net.rrm.ehour.ui.panel.report.criteria;

import java.io.Serializable;

import net.rrm.ehour.report.criteria.UserCriteria;

/**
 * TODO 
 **/

public class ReportCriteriaBackingBean implements Serializable
{
	private static final long serialVersionUID = 4417220135092280759L;

	private UserCriteria	userCriteria;
	private QuickWeek		quickWeek;
	
	public ReportCriteriaBackingBean(UserCriteria userCriteria)
	{
		this.userCriteria = userCriteria;
	}

	/**
	 * @return the userCriteria
	 */
	public UserCriteria getUserCriteria()
	{
		return userCriteria;
	}

	/**
	 * @return the quickWeek
	 */
	public QuickWeek getQuickWeek()
	{
		return quickWeek;
	}

	/**
	 * @param quickWeek the quickWeek to set
	 */
	public void setQuickWeek(QuickWeek quickWeek)
	{
		this.quickWeek = quickWeek;
	}
}
