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

import net.rrm.ehour.ui.common.report.AbstractExcelReport;
import net.rrm.ehour.ui.common.report.ReportConfig;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * User report
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReportExcel extends AbstractExcelReport
{
	private static final long serialVersionUID = 1427524857733863613L;

	public UserReportExcel()
	{
		super(ReportConfig.AGGREGATE_CUSTOMER_SINGLE_USER);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.panel.AbstractAggregateExcelReport#getExcelReportName()
	 */
	@Override
	protected IModel getExcelReportName()
	{
		return new ResourceModel("report.user.name");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.panel.AbstractAggregateExcelReport#getHeaderReportName()
	 */
	@Override
	protected IModel getHeaderReportName()
	{
		return new ResourceModel("report.user.name");
	}

	public static String getId()
	{
		return "userReportExcel";
	}
}
