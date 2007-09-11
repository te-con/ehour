package net.rrm.ehour.ui.report.aggregate.value;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Thies
 * Date: Sep 11, 2007
 * Time: 6:52:18 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ReportNode implements Serializable
{
    protected Serializable[]    columnValues;
    private List<ReportNode>    reportNodes = new ArrayList<ReportNode>();
    protected Serializable        id;

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

        if (isProcessAggregate(aggregate))
        {
            if (!(processed = processChildNodes(aggregate, hierarchyLevel + 1, nodeFactory)))
            {
                ReportNode node = nodeFactory.createReportNode(aggregate, ++hierarchyLevel);

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
            if (node.processAggregate(aggregate, hierarchyLevel + 1, nodeFactory))
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
     * Process aggregate for this value? By default ignored, override when needed
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
    
}
