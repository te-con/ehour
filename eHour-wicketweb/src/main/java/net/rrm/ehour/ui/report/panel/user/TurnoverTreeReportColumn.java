/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.panel.user;

import net.rrm.ehour.ui.common.report.ReportColumn;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.model.IModel;

/**
 * ReportColumn with visibility depending on whether to show turnover or not 
 **/

public class TurnoverTreeReportColumn extends ReportColumn
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
