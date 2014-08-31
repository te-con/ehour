package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.ui.common.converter.CurrencyConverter;
import net.rrm.ehour.ui.common.converter.DateConverter;

import static net.rrm.ehour.ui.common.report.ReportColumn.DisplayType.*;

public enum AggregatedReportConfig implements ReportConfig {
    // constructors like these might be a bit over the top..
    // take note, the columnResourceKey is used for serie creation with trend charts (pardon my English, it's late.. or early in the morning)
    AGGREGATE_CUSTOMER(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.customer",
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),

    AGGREGATE_PROJECT(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.project",
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),

    AGGREGATE_USER(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.user",
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),

    AUDIT_REPORT(new ReportColumn("audit.report.column.date", ColumnType.DATE, new DateConverter(), VISIBLE),
            new ReportColumn("audit.report.column.lastName"),
            new ReportColumn("audit.report.column.action"),
            new ReportColumn("audit.report.column.type"));

    private final String zeroBookingsMessageKey;
    private ReportColumn[] reportColumns;
    private Boolean showZeroBookings;

    private AggregatedReportConfig(ReportColumn... reportColumns) {
        this(ReportType.NO_ZERO_BOOKINGS, "", reportColumns);
    }

    private AggregatedReportConfig(ReportType zeroBookings, String zeroBookingsMessageKey, ReportColumn... reportColumns) {
        this.zeroBookingsMessageKey = zeroBookingsMessageKey;
        this.reportColumns = reportColumns;
        this.showZeroBookings = zeroBookings == ReportType.SHOW_ZERO_BOOKINGS;
    }

    public ReportColumn[] getReportColumns() {
        return reportColumns;
    }

    public Boolean isShowZeroBookings() {
        return showZeroBookings;
    }

    public String getZeroBookingsMessageKey() {
        return zeroBookingsMessageKey;
    }
}
