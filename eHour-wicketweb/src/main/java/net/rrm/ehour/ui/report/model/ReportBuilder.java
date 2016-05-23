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

package net.rrm.ehour.ui.report.model;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * ReportBuilder
 * Converts a collection of report elements to a hierarchical reportNode tree.
 *
 * @author Thies
 */
public class ReportBuilder {
    protected static final Logger logger = Logger.getLogger(ReportBuilder.class);

    /**
     * Create report
     */
    @SuppressWarnings("unchecked")
    public List<ReportNode> createReport(ReportData reportData, ReportNodeFactory nodeFactory) {
        List<ReportNode> reportNodes = new ArrayList<>();

        for (ReportElement reportElement : reportData.getReportElements()) {
            if (!processElement(reportElement, nodeFactory, reportNodes)) {
                ReportNode node = nodeFactory.createReportNode(reportElement, 0);
                node.processElement(reportElement, 0, nodeFactory);
                reportNodes.add(node);
            }
        }

        return reportNodes;
    }

    private boolean processElement(ReportElement element, ReportNodeFactory factory, List<ReportNode> reportNodes) {
        boolean processed = false;

        // check for each reportNode whether 
        for (ReportNode reportNode : reportNodes) {
            if (reportNode.processElement(element, 0, factory)) {
                processed = true;
                break;
            }
        }

        return processed;
    }
}
