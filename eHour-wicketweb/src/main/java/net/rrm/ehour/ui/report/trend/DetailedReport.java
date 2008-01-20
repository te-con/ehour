/**
 * Created on Dec 31, 2007
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.report.trend;

import java.io.Serializable;
import java.util.Locale;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.panel.report.ReportConfig;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;
import net.rrm.ehour.ui.report.trend.node.FlatCustomerNode;
import net.rrm.ehour.ui.report.trend.node.FlatDateNode;
import net.rrm.ehour.ui.report.trend.node.FlatEntryEndNode;
import net.rrm.ehour.ui.report.trend.node.FlatProjectNode;
import net.rrm.ehour.ui.report.trend.node.FlatUserNode;

/**
 * Detailed report
 **/

public class DetailedReport extends TreeReport<FlatReportElement>
{
	private static final long serialVersionUID = -21703820501429504L;
	private Locale locale;
	
	/**
	 * 
	 * @param reportData
	 */
	public DetailedReport(ReportData<FlatReportElement> reportData, Locale locale)
	{
		super();
		
		this.locale = locale;
		
		this.initializeReport(reportData, ReportConfig.DETAILED_REPORT);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.TreeReport#getReportNodeFactory()
	 */
	@Override
	public ReportNodeFactory getReportNodeFactory()
	{
    	return new ReportNodeFactory()
	    {
	        @Override
	        public ReportNode createReportNode(ReportElement element, int hierarchyLevel)
	        {
	        	FlatReportElement flatElement = (FlatReportElement)element;
	        	
	            switch (hierarchyLevel)
	            {
	                case 0:
	                	return new FlatCustomerNode(flatElement, hierarchyLevel);
	                case 1:
	                    return new FlatProjectNode(flatElement, hierarchyLevel);
	                case 2:
	                    return new FlatDateNode(flatElement, hierarchyLevel, locale);
	                case 3:
	                	return new FlatUserNode(flatElement, hierarchyLevel);
	                case 4:
	                	return new FlatEntryEndNode(flatElement, hierarchyLevel);
	            }
	
	            throw new RuntimeException("Hierarchy level too deep");
	        }
	
	        /**
	         * Only needed for the root node, customer
	         * @param aggregate
	         * @return
	         */
	        public Serializable getElementId(ReportElement element)
	        {
	        	FlatReportElement flatElement = (FlatReportElement)element;
	            return flatElement.getCustomerId();
	        }
	    };	
    }
}
