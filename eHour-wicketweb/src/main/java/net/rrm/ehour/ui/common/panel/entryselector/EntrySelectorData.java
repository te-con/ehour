package net.rrm.ehour.ui.common.panel.entryselector;

import com.google.common.collect.Lists;
import org.apache.wicket.util.io.IClusterable;

import java.io.Serializable;
import java.util.List;

public class EntrySelectorData implements IClusterable {
    private final List<Header> columnHeaders;
    private final List<EntrySelectorRow> rows;

    private final Boolean withChildren;

    public EntrySelectorData(List<Header> columnHeaders, List<EntrySelectorRow> rows) {
        this.columnHeaders = columnHeaders;
        this.rows = rows;

        boolean hasChildren = false;

        for (EntrySelectorRow row : rows) {
            hasChildren = !row.getChildren().isEmpty();

            if (hasChildren)
                break;
        }

        this.withChildren = hasChildren;
    }

    public Boolean isWithChildren() {
        return withChildren;
    }

    public List<Header> getColumnHeaders() {
        return columnHeaders;
    }

    public List<EntrySelectorRow> getRows() {
        return rows;
    }

    public static class EntrySelectorRow implements IClusterable {
        private List<? extends Serializable> cells;
        private Serializable id;
        private final boolean active;
        private List<EntrySelectorRow> children;

        public EntrySelectorRow(List<? extends Serializable> cells, Serializable id) {
            this(cells, id, true);
        }

        public EntrySelectorRow(List<? extends Serializable> cells, Serializable id, boolean active) {
            this(cells, id, active, Lists.<EntrySelectorRow>newArrayList());
        }

        public EntrySelectorRow(List<? extends Serializable> cells, Serializable id, List<EntrySelectorRow> children) {
            this(cells, id, true, Lists.<EntrySelectorRow>newArrayList());
        }

        public EntrySelectorRow(List<? extends Serializable> cells, Serializable id, boolean active, List<EntrySelectorRow> children) {
            this.cells = cells;
            this.id = id;
            this.active = active;
            this.children = children;
        }

        public List<? extends Serializable> getCells() {
            return cells;
        }

        public boolean isActive() {
            return active;
        }

        public Serializable getId() {
            return id;
        }

        public List<EntrySelectorRow> getChildren() {
            return children;
        }
    }


    public static class Header implements IClusterable {
        private final ColumnType columnType;
        private final String resourceLabel;

        public Header(String resourceLabel) {
            this(resourceLabel, ColumnType.TEXT);
        }

        public Header(String resourceLabel, ColumnType columnType) {
            this.columnType = columnType;
            this.resourceLabel = resourceLabel;
        }

        public ColumnType getColumnType() {
            return columnType;
        }

        public String getResourceLabel() {
            return resourceLabel;
        }
    }

    public static enum ColumnType {
        TEXT,
        NUMERIC
    }
}
