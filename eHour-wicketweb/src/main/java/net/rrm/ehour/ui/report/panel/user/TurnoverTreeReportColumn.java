/**
 * Created on Jan 2, 2008
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

package net.rrm.ehour.ui.report.panel.user;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.panel.TreeReportColumn;

import org.apache.wicket.model.IModel;

/**
 * TreeReportColumn with visibility depending on whether to show turnover or not 
 **/

public class TurnoverTreeReportColumn extends TreeReportColumn
{
	private static final long serialVersionUID = 1L;

	public TurnoverTreeReportColumn(String columnHeaderResourceKey, 
									Class<? extends IModel> conversionModel)
	{
		super(columnHeaderResourceKey, conversionModel, true, ColumnType.TURNOVER);
	}
	
	/**
	 * @return the visible
	 */
	@Override
	public boolean isVisible()
	{
		return EhourWebSession.getSession().getEhourConfig().isShowTurnover();
	}	
}
