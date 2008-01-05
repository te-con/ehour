/**
 * User: Thies
 * Date: Sep 11, 2007
 * Time: 6:52:18 PM
 *
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

package net.rrm.ehour.ui.report.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.reports.element.ReportElement;

import org.apache.log4j.Logger;

/**
 * Tree structure of abstract nodes for reporting purposes.
 * Each node can have multiple reportnode children, for example customer -> projects -> users
 */
public abstract class ReportNode implements Serializable
{
	private transient Logger logger = Logger.getLogger(ReportNode.class);
	
    protected Serializable[]    columnValues;
    private List<ReportNode>    reportNodes = new ArrayList<ReportNode>();
    protected Serializable      id;
    protected int				hierarchyLevel;
    
    /**
     * Create node matrix flattening the whole tree. Repeating fields are null 
     * @return
     */
    public List<Serializable[]> getNodeMatrix(int matrixWidth)
    {
    	List<Serializable[]> matrix = new ArrayList<Serializable[]>();
    	
    	createNodeMatrix(0, new Serializable[matrixWidth], matrix, matrixWidth);
    	
    	return matrix;
    }
    
    /**
     * 
     * @param currentColumn
     * @param currentRow
     * @param matrix
     * @return
     */
    private Serializable[] createNodeMatrix(int currentColumn, Serializable[] columns, List<Serializable[]> matrix, int matrixWidth)
    {
    	for (Serializable columnValue : columnValues)
		{
        	if (logger.isDebugEnabled())
        	{
        		logger.debug("Setting " + columnValue + " on column " + currentColumn + " from node " + this.getClass());
        	}
        	
        	columns[currentColumn++] = columnValue;
		}
    	
    	if (isLastNode())
    	{
    		matrix.add(columns);
    		columns = new Serializable[matrixWidth];
    	}
    	else
    	{
    		for (ReportNode reportNode : reportNodes)
			{
    			columns = reportNode.createNodeMatrix(currentColumn, columns, matrix, matrixWidth);
			}
    	}
    	
    	return columns;
    }
    
    /**
     * Process aggregate
     * @param aggregate
     * @return
     */
    public boolean processElement(ReportElement reportElement,
                                    int hierarchyLevel,
                                    ReportNodeFactory nodeFactory)
    {
        boolean processed = false;

        // first check if we need to add the aggregate to his node
        if (isProcessElement(reportElement))
        {
        	// was it added to one of the child nodes ?
            if (!(processed = processChildNodes(reportElement, hierarchyLevel + 1, nodeFactory)))
            {
            	// if not make a new child node for this aggregate
                ReportNode node = nodeFactory.createReportNode(reportElement, ++hierarchyLevel);

                // if the new node is not the last child, check whether one
                // of it's subschildren can process it
                if (!node.isLastNode())
                {
                    node.processElement(reportElement, hierarchyLevel, nodeFactory);
                }
                
                reportNodes.add(node);
                processed = true;
            }
        }

        return processed;
    }

    /**
     * Is the aggregate processed by the childnodes?
     * @param element
     * @return
     */
    private boolean processChildNodes(ReportElement element,
                                      int hierarchyLevel,
                                      ReportNodeFactory nodeFactory)
    {
        boolean processed = false;

        for (ReportNode node : reportNodes)
        {
        	// if the children are last nodes don't bother checking
        	if (node.isLastNode())
        	{
        		break;
        	}
        	
            if (node.processElement(element, hierarchyLevel, nodeFactory))
            {
                processed = true;
                break;
            }
        }

        return processed;
    }

    /**
     * Get id for element
     * @param element
     * @return
     */
    protected abstract Serializable getElementId(ReportElement element);

    /**
     * Process aggregate for this value? Only process aggregates that got the same id
     * @param aggregate
     * @param forId
     * @return
     */
    private boolean isProcessElement(ReportElement reportElement)
    {
        return  this.id.equals(getElementId(reportElement));
    }

    /**
     * Is last node in the hierarchy ? Override for end node
     * @return
     */
    protected boolean isLastNode()
    {
        return false;
    }

    /**
     * Get hours
     * @return
     */
    public Number getHours()
    {
        float totalHours = 0;

        for (ReportNode reportNode : reportNodes)
        {
            Number hours = reportNode.getHours();

            if (hours != null)
            {
                totalHours += hours.floatValue();
            }
        }

        return totalHours;
    }

    /**
     * Get turnover
     * @return
     */
    public Number getTurnover()
    {
        float totalTurnover = 0;

        for (ReportNode reportNode : reportNodes)
        {
            Number turnOver = reportNode.getTurnover();

            if (turnOver != null)
            {
                totalTurnover += turnOver.floatValue();
            }
        }

        return totalTurnover;
    }

    /**
     * 
     * @return
     */
    public Serializable[] getColumnValues()
    {
        return columnValues;
    }


    /**
     * @return
     */
    public List<ReportNode> getReportNodes()
    {
        return reportNodes;
    }

    /**
     *
     * @param reportNodes
     */
    public void setReportNodes(List<ReportNode> reportNodes)
    {
        this.reportNodes = reportNodes;
    }

    /**
     *
     * @return
     */
    public Serializable getId()
    {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Serializable id)
    {
        this.id = id;
    }

	/**
	 * @return the hierarchyLevel
	 */
	public int getHierarchyLevel()
	{
		return hierarchyLevel;
	}
}
