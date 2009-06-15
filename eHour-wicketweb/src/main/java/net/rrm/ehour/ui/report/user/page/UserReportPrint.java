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

package net.rrm.ehour.ui.report.user.page;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.panel.user.UserReportPanel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * Printed version of the report
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReportPrint extends WebPage
{
	private static final long serialVersionUID = -8492629550038561271L;

	/**
	 * 
	 * @param parameters
	 */
	public UserReportPrint(String reportId)
	{
		super();
		
		StringResourceModel pageTitle = new StringResourceModel("userReport.printTitle", 
				this, null, new Object[]{new Model(((EhourWebSession)getSession()).getUser().getUser().getFullName())});

		
		add(new Label("pageTitle", pageTitle)); 
		
		EhourWebSession session = EhourWebSession.getSession();
		CustomerAggregateReport report = (CustomerAggregateReport)session.getObjectCache().getObjectFromCache(reportId);

		add(new UserReportPanel("userReportPanel", report, false));
	}
}
