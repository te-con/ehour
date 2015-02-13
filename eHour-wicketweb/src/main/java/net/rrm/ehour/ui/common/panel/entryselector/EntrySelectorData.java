package net.rrm.ehour.ui.common.panel.entryselector;

import org.apache.wicket.util.io.IClusterable;

import java.io.Serializable;
import java.util.List;

public class EntrySelectorData implements IClusterable {
    private final List<Header> columnHeaders;
    private final List<EntrySelectorRow> rows;

    public EntrySelectorData(List<Header> columnHeaders, List<EntrySelectorRow> rows) {
        this.columnHeaders = columnHeaders;
        this.rows = rows;
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

        public EntrySelectorRow(List<? extends Serializable> cells, Serializable id) {
            this(cells, id, true);
        }

        public EntrySelectorRow(List<? extends Serializable> cells, Serializable id, boolean active) {
            this.cells = cells;
            this.id = id;
            this.active = active;
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
        NUMERIC,
        DATE,
        HTML
    }
}
