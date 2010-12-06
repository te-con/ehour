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

package net.rrm.ehour.ui.report;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ReportBuilder
 * Converts a collection of report elements to a hierarchical reportNode tree.
 * The
 * 
 * @author Thies
 *
 */
public class ReportBuilder
{
	protected final static Logger logger = Logger.getLogger(ReportBuilder.class);

	/**
	 * Create report
	 * @param reportData
	 * @param nodeFactory
	 * @return
	 */
	public List<ReportNode> createReport(ReportData reportData, ReportNodeFactory nodeFactory)
	{
		Date profileStart = new Date();

        List<ReportNode> reportNodes = new ArrayList<ReportNode>();
        
        for (ReportElement reportElement : reportData.getReportElements() )
        {
            if (!processElement(reportElement, nodeFactory, reportNodes))
            {
                ReportNode node = nodeFactory.createReportNode(reportElement, 0);
                node.processElement(reportElement, 0, nodeFactory);
                reportNodes.add(node);
            }
        }
        
		logger.debug("Report took " + (new Date().getTime() - profileStart.getTime()) + "ms to create");

        return reportNodes;
    }

    /**
     * Process report importer
     * @param element
     * @param factory
     * @return
     */
    private boolean processElement(ReportElement element, ReportNodeFactory factory, List<ReportNode> reportNodes)
    {
        boolean processed = false;

        // check for each reportNode whether 
        for (ReportNode reportNode : reportNodes)
        {
            if (reportNode.processElement(element, 0, factory))
            {
                processed = true;
                break;
            }
        }

        return processed;
    }
    
    
}
