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

package net.rrm.ehour.ui.report.project;


import net.rrm.ehour.ui.report.AbstractReportPanelTest;
import net.rrm.ehour.ui.report.detailed.DetailedReportDataObjectMother;
import net.rrm.ehour.ui.report.model.TreeReportModel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created on Mar 17, 2009, 6:44:27 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class ProjectReportPanelTest extends AbstractReportPanelTest {
    @Override
    protected Panel createReportPanel(String panelId, TreeReportModel reportModel) {
        return new ProjectReportPanel(panelId, reportModel);
    }

    @Override
    protected TreeReportModel getAggregateReport() {
        return new ProjectAggregateReportModel(DetailedReportDataObjectMother.getReportCriteria());
    }
}