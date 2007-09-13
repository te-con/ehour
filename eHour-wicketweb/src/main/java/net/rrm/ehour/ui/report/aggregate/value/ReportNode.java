package net.rrm.ehour.ui.report.aggregate.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;

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
 *
 
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
    public Serializable[][] getNodeMatrix(int matrixWidth)
    {
    	Serializable[][] matrix = initMatrix(matrixWidth);
    	
    	createNodeMatrix(0, 0, matrix);
    	
    	return matrix;
    }
    
    /**
     * 
     * @param currentColumn
     * @param currentRow
     * @param matrix
     * @return
     */
    private int createNodeMatrix(int currentColumn, int currentRow, Serializable[][] matrix)
    {
    	for (Serializable columnValue : columnValues)
		{
        	if (logger.isDebugEnabled())
        	{
        		logger.debug("Setting " + columnValue + " in matrix pos " 
        						+ currentRow + "x" + currentColumn + " from node " + this.getClass());
        	}
    		
    		matrix[currentRow][currentColumn++] = columnValue;
		}
    	
    	if (isLastNode())
    	{
    		currentRow++;
    	}
    	else
    	{
    		for (ReportNode reportNode : reportNodes)
			{
    			currentRow = reportNode.createNodeMatrix(currentColumn, currentRow, matrix);
			}
    	}
    	
    	return currentRow;
    }
    
    /**
     * Initialize matrix if null
     * @param matrixWidth
     * @param matrix
     * @return
     */
    private Serializable[][] initMatrix(int matrixWidth)
    {
		int matrixHeight = getEndNodeCount();
		Serializable[][] matrix = new Serializable[matrixHeight][matrixWidth];
    	
    	if (logger.isDebugEnabled())
    	{
    		logger.debug("Initializing ReportNode matrix: " + matrixHeight + "x" + matrixWidth);
    	}
    	
    	return matrix;
    }
    
    
    /**
     * Get end node count
     * @return
     */
    private int getEndNodeCount()
    {
    	int	childCount = 0;
    	
    	for (ReportNode node : reportNodes)
		{
    		if (node.isLastNode())
    		{
    			childCount++;
    		}
    		else
    		{
    			childCount += node.getEndNodeCount();
    		}
		}
    	
    	return childCount;
    }
    
    /**
     * Process aggregate
     * @param aggregate
     * @return
     */
    public boolean processAggregate(ProjectAssignmentAggregate aggregate,
                                    int hierarchyLevel,
                                    ReportNodeFactory nodeFactory)
    {
        boolean processed = false;

        // first check if we need to add the aggregate to his node
        if (isProcessAggregate(aggregate))
        {
        	// was it added to one of the child nodes ?
            if (!(processed = processChildNodes(aggregate, hierarchyLevel + 1, nodeFactory)))
            {
            	// if not make a new child node for this aggregate
                ReportNode node = nodeFactory.createReportNode(aggregate, ++hierarchyLevel);

                // if the new node is not the last child, check whether one
                // of it's subschildren can process it
                if (!node.isLastNode())
                {
                    node.processAggregate(aggregate, hierarchyLevel, nodeFactory);
                }
                
                reportNodes.add(node);
                processed = true;
            }
        }

        return processed;
    }

    /**
     * Is the aggregate processed by the childnodes?
     * @param aggregate
     * @return
     */
    private boolean processChildNodes(ProjectAssignmentAggregate aggregate,
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
        	
            if (node.processAggregate(aggregate, hierarchyLevel, nodeFactory))
            {
                processed = true;
                break;
            }
        }

        return processed;
    }

    /**
     *
     * @param aggregate
     * @return
     */
    protected abstract Serializable getAggregateId(ProjectAssignmentAggregate aggregate);

    /**
     * Process aggregate for this value? Only process aggregates that got the same id
     * @param aggregate
     * @param forId
     * @return
     */
    private boolean isProcessAggregate(ProjectAssignmentAggregate aggregate)
    {
        return  this.id.equals(getAggregateId(aggregate));
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
