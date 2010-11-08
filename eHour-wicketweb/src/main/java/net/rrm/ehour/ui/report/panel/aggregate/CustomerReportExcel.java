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

package net.rrm.ehour.ui.report.panel.aggregate;

import net.rrm.ehour.ui.common.report.AbstractExcelReport;
import net.rrm.ehour.ui.common.report.ReportConfig;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

@AuthorizeInstantiation("ROLE_REPORT")
public class CustomerReportExcel extends AbstractExcelReport
{
	private static final long serialVersionUID = 7211392869328367507L;
	
	public CustomerReportExcel()
	{
		super(ReportConfig.AGGREGATE_CUSTOMER);
	}
	
	@Override
	protected IModel<String> getExcelReportName()
	{
		return new ResourceModel("report.title.customer");
	}

	@Override
	protected IModel<String> getHeaderReportName()
	{
		return new ResourceModel("report.title.customer");
	}

	public static String getId()
	{
		return "customerReportExcel";
	}
}
