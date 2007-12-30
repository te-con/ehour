/**
 * User: Thies
 * Date: Sep 11, 2007
 * Time: 7:54:51 PM
 * Copyright (C) 2005-2007 TE-CON, All Rights Reserved.
 * <p/>
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is
 * available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for
 * commercial use or open source, is subject to obtaining the prior express authorization of TE-CON.
 * <p/>
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 */

package net.rrm.ehour.ui.report.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNodeFactory;

import org.apache.log4j.Logger;

/**
 * ReportBuilder
 * @author Thies
 *
 */
public class ReportBuilder
{
	protected transient Logger logger = Logger.getLogger(this.getClass());


	/**
	 * Initialize the webreport for a specific id
	 * @param reportData
	 * @param forId the ID to generate the report for (null to ignore)
	 */
	public List<ReportNode> createReport(ReportData reportData, ReportNodeFactory nodeFactory)
	{
		Date profileStart = new Date();

        List<ReportNode> reportNodes = new ArrayList<ReportNode>();
        
        if (reportData != null)
        {
	        for (ReportElement reportElement : reportData.getReportElements() )
	        {
	            if (!processElement(reportElement, nodeFactory, reportNodes))
	            {
	                ReportNode node = nodeFactory.createReportNode(reportElement, 0);
	                node.processElement(reportElement, 0, nodeFactory);
	                reportNodes.add(node);
	            }
	        }
        }
		logger.debug("Report took " + (new Date().getTime() - profileStart.getTime()) + "ms to create");

        return reportNodes;
    }

    /**
     * Process aggregate
     * @param aggregate
     * @param factory
     * @return
     */
    private boolean processElement(ReportElement aggregate, ReportNodeFactory factory, List<ReportNode> reportNodes)
    {
        boolean processed = false;

        for(ReportNode reportNode : reportNodes)
        {
            if (reportNode.processElement(aggregate, 0, factory))
            {
                processed = true;
                break;
            }
        }

        return processed;
    }
}
