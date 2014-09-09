package net.rrm.ehour.ui.common.panel.entryselector;

import org.apache.wicket.util.io.IClusterable;

import java.io.Serializable;
import java.util.List;

public class EntrySelectorData<T> implements IClusterable {
    private final List<Header> columnHeaders;
    private final List<EntrySelectorRow<T>> rows;

    public EntrySelectorData(List<Header> columnHeaders, List<EntrySelectorRow<T>> rows) {
        this.columnHeaders = columnHeaders;
        this.rows = rows;
    }

    public List<Header> getColumnHeaders() {
        return columnHeaders;
    }

    public List<EntrySelectorRow<T>> getRows() {
        return rows;
    }

    public static class EntrySelectorRow<T> implements IClusterable {
        private List<? extends Serializable> cells;
        private T id;
        private final boolean active;

        public EntrySelectorRow(List<? extends Serializable> cells, T id) {
            this(cells, id, true);
        }

        public EntrySelectorRow(List<? extends Serializable> cells, T id, boolean active) {
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

        public T getId() {
            return id;
        }
    }

    public static class Header implements IClusterable {
        private final ColumnType columnType;
        private final String resourceLabel;

        public Header(String resourceLabel) {
            this(ColumnType.TEXT, resourceLabel);
        }

        public Header(ColumnType columnType, String resourceLabel) {
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
