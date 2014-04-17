package net.rrm.ehour.report.criteria;

public enum AggregateBy {
    DAY(24 * 3600 * 1000),
    WEEK(DAY.interval * 7),
    MONTH(WEEK.interval * 4), // fixme
    QUARTER(MONTH.interval * 3),
    YEAR(QUARTER.interval * 4);

    public final long interval;

    AggregateBy(long interval) {
        this.interval = interval;
    }
}
