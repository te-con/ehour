package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.ui.common.converter.*;

import static net.rrm.ehour.ui.common.report.ReportColumn.DisplayType.*;

public enum DetailedReportConfig implements ReportConfig {
    // constructors like these might be a bit over the top..
    // take note, the columnResourceKey is used for serie creation with trend charts (pardon my English, it's late.. or early in the morning)
    DETAILED_REPORT_BY_DAY(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.detailed",
            new ReportColumn("userReport.report.date", ColumnType.DATE, new LockableDateConverter(), VISIBLE),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.dailyComment", ColumnType.COMMENT, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),
    DETAILED_REPORT_BY_WEEK(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.detailed",
            new ReportColumn("userReport.report.week", ColumnType.STRING, new DateToWeekConverter(), VISIBLE),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.weeklyComment", ColumnType.COMMENT, VISIBLE),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),
    DETAILED_REPORT_BY_MONTH(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.detailed",
            new ReportColumn("userReport.report.month", ColumnType.STRING, new DateToMonthConverter(), VISIBLE),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.comment", ColumnType.COMMENT, HIDDEN),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),
    DETAILED_REPORT_BY_QUARTER(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.detailed",
            new ReportColumn("userReport.report.quarter", ColumnType.STRING, new DateToQuarterConverter(), VISIBLE),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.comment", ColumnType.COMMENT, HIDDEN),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),
    DETAILED_REPORT_BY_YEAR(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.detailed",
            new ReportColumn("userReport.report.year", ColumnType.STRING, new DateToYearConverter(), VISIBLE),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.comment", ColumnType.COMMENT, HIDDEN),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED));


    private final String zeroBookingsMessageKey;
    private ReportColumn[] reportColumns;
    private Boolean showZeroBookings;

    private DetailedReportConfig(ReportType zeroBookings, String zeroBookingsMessageKey, ReportColumn... reportColumns) {
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
