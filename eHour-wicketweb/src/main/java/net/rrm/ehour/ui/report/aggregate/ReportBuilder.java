package net.rrm.ehour.ui.report.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNodeFactory;

import org.apache.log4j.Logger;

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
public class ReportBuilder
{
	protected transient Logger logger = Logger.getLogger(this.getClass());


	/**
	 * Initialize the webreport for a specific id
	 * @param reportDataAggregate
	 * @param forId the ID to generate the report for (null to ignore)
	 */
	public List<ReportNode> createReport(ReportDataAggregate reportDataAggregate, ReportNodeFactory nodeFactory)
	{
		Date profileStart = new Date();

        List<ReportNode> reportNodes = new ArrayList<ReportNode>();
        
        if (reportDataAggregate != null)
        {
	        for (ProjectAssignmentAggregate aggregate : reportDataAggregate.getProjectAssignmentAggregates())
	        {
	            if (!processAggregate(aggregate, nodeFactory, reportNodes))
	            {
	                ReportNode node = nodeFactory.createReportNode(aggregate, 0);
	                node.processAggregate(aggregate, 0, nodeFactory);
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
    private boolean processAggregate(ProjectAssignmentAggregate aggregate, ReportNodeFactory factory, List<ReportNode> reportNodes)
    {
        boolean processed = false;

        for(ReportNode reportNode : reportNodes)
        {
            if (reportNode.processAggregate(aggregate, 0, factory))
            {
                processed = true;
                break;
            }
        }

        return processed;
    }
}
