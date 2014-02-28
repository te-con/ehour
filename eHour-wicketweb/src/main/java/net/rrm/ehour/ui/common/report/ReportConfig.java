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

package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.ui.common.converter.CurrencyConverter;
import net.rrm.ehour.ui.common.converter.DateConverter;
import net.rrm.ehour.ui.common.converter.LockableDateConverter;

import static net.rrm.ehour.ui.common.report.ReportColumn.DisplayType.*;

/**
 * All report definitions
 */

public enum ReportConfig {
    // constructors like these might be a bit over the top..
    // take note, the columnResourceKey is used for serie creation with trend charts (pardon my English, it's late.. or early in the morning)
    DETAILED_REPORT(ReportType.SHOW_ZERO_BOOKINGS, "report.criteria.zerobookings.detailed",
            new ReportColumn("userReport.report.date", ColumnType.DATE, new LockableDateConverter(), VISIBLE),
            new ReportColumn("userReport.report.customer"),
            new ReportColumn("userReport.report.project"),
            new ReportColumn("userReport.report.projectCode"),
            new ReportColumn("userReport.report.user"),
            new ReportColumn("userReport.report.role"),
            new ReportColumn("userReport.report.comment", ColumnType.COMMENT, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.rate", ColumnType.RATE, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED),
            new ReportColumn("userReport.report.hours", ColumnType.HOUR, VISIBLE, ALLOW_DUPLICATES),
            new ReportColumn("userReport.report.turnover", ColumnType.TURNOVER, CurrencyConverter.getInstance(), VISIBLE, ALLOW_DUPLICATES, IS_RATE_RELATED)),

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

    private ReportConfig(ReportColumn... reportColumns) {
        this(ReportType.NO_ZERO_BOOKINGS, "", reportColumns);
    }

    private ReportConfig(ReportType zeroBookings, String zeroBookingsMessageKey, ReportColumn... reportColumns) {
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

enum ReportType {
    SHOW_ZERO_BOOKINGS,
    NO_ZERO_BOOKINGS
}
