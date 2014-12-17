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

package net.rrm.ehour.ui.report.user;


import net.rrm.ehour.ui.report.AbstractReportPanelTest;
import net.rrm.ehour.ui.report.detailed.DetailedReportDataObjectMother;
import net.rrm.ehour.ui.report.model.TreeReportModel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created on Mar 17, 2009, 6:43:36 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class UserReportPanelTest extends AbstractReportPanelTest
{
	@Override
	protected Panel createReportPanel(String panelId, TreeReportModel reportModel)
	{
		return new UserReportPanel(panelId, reportModel);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.panel.aggregate.AbstractReportPanelTest#getAggregateReport()
	 */
	@Override
	protected TreeReportModel getAggregateReport()
	{
		return new UserAggregateReportModel(DetailedReportDataObjectMother.getReportCriteria());
	}
}