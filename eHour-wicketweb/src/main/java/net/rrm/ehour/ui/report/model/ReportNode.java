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

import net.rrm.ehour.report.reports.element.ReportElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree structure of abstract nodes for reporting purposes.
 * Each node can have multiple reportnode children, for example customer -> projects -> users
 */
public abstract class ReportNode implements Serializable {
    private static final long serialVersionUID = 8722465589611086312L;

    protected Serializable[] columnValues;
    private List<ReportNode> reportNodes = new ArrayList<>();
    private final Serializable id;
    private final boolean empty;

    protected ReportNode(Serializable id) {
        this(id, false);
    }

    protected ReportNode(Serializable id, boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    /**
     * Create node matrix flattening the whole tree.
     */
    public List<TreeReportElement> getNodeMatrix(int matrixWidth) {
        List<TreeReportElement> matrix = new ArrayList<>();

        createNodeMatrix(0, new Serializable[matrixWidth], matrix);

        return matrix;
    }

    private Serializable[] createNodeMatrix(int currentColumn, Serializable[] columns, List<TreeReportElement> matrix) {
        if (isLeaf()) {
            Serializable[] returnCols = columns.clone();

            setMatrixColumns(columns, currentColumn);

            matrix.add(new TreeReportElement(columns, isEmpty()));
            return returnCols;
        } else {
            currentColumn = setMatrixColumns(columns, currentColumn);

            for (ReportNode reportNode : reportNodes) {
                columns = reportNode.createNodeMatrix(currentColumn, columns, matrix);
            }
        }

        return columns;
    }

    /**
     * @param columns
     * @param currentColumn
     */
    private int setMatrixColumns(Serializable[] columns, int currentColumn) {
        for (Serializable columnValue : columnValues) {
            columns[currentColumn++] = columnValue;
        }

        return currentColumn;
    }


    /**
     * Process aggregate
     *
     * @param aggregate
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean processElement(ReportElement reportElement,
                                  int hierarchyLevel,
                                  ReportNodeFactory nodeFactory) {
        boolean processed = false;

        // first check if we need to add the aggregate to his node
        if (shouldProcessElement(reportElement) && !(processed = processChildNodes(reportElement, hierarchyLevel + 1, nodeFactory))) {
            // if not make a new child node for this aggregate
            ReportNode node = nodeFactory.createReportNode(reportElement, ++hierarchyLevel);

            // if the new node is not the last child, check whether one
            // of it's subschildren can process it
            if (!node.isLeaf()) {
                node.processElement(reportElement, hierarchyLevel, nodeFactory);
            }

            reportNodes.add(node);
            processed = true;
        }

        return processed;
    }

    /**
     * Is the aggregate processed by the childnodes?
     *
     * @param element
     * @return
     */
    private boolean processChildNodes(ReportElement element,
                                      int hierarchyLevel,
                                      ReportNodeFactory nodeFactory) {
        boolean processed = false;

        for (ReportNode node : reportNodes) {
            // if the children are last nodes don't bother checking
            if (node.isLeaf()) {
                break;
            }

            if (node.processElement(element, hierarchyLevel, nodeFactory)) {
                processed = true;
                break;
            }
        }

        return processed;
    }

    /**
     * Get id for importer
     *
     * @param element
     * @return
     */
    protected abstract Serializable getElementId(ReportElement element);

    /**
     * Process aggregate for this value? Only process aggregates that got the same id
     *
     * @param aggregate
     * @param forId
     * @return
     */
    private boolean shouldProcessElement(ReportElement reportElement) {
        return getId().equals(getElementId(reportElement));
    }

    /**
     * Is last node in the hierarchy ? Override for leaf
     *
     * @return
     */
    protected boolean isLeaf() {
        return false;
    }

    /**
     * Get hours
     *
     * @return
     */
    public Number getHours() {
        float totalHours = 0;

        for (ReportNode reportNode : reportNodes) {
            Number hours = reportNode.getHours();

            if (hours != null) {
                totalHours += hours.floatValue();
            }
        }

        return totalHours;
    }

    /**
     * Get turnover
     *
     * @return
     */
    public Number getTurnover() {
        float totalTurnover = 0;

        for (ReportNode reportNode : reportNodes) {
            Number turnOver = reportNode.getTurnover();

            if (turnOver != null) {
                totalTurnover += turnOver.floatValue();
            }
        }

        return totalTurnover;
    }

    /**
     * @return
     */
    public Serializable getId() {
        return id;
    }

}
