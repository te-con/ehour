package net.rrm.ehour.ui.common.report;

public enum ColumnType {
    STRING, DATE, RATE(true), HOUR(true), TURNOVER(true), COMMENT, LINK;

    private boolean numeric;

    private ColumnType() {
        this(false);
    }


    private ColumnType(boolean isNumeric) {
        numeric = isNumeric;
    }

    public boolean isNumeric() {
        return numeric;
    }
}
