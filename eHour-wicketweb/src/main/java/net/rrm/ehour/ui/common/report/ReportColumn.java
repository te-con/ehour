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

import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.util.convert.IConverter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Report column
 * <p/>
 * The converterArgs may look iffy, cloning a model could as well be accomplished
 * with a clone() however that also means that for each model the constructor args should
 * be stored globally in the object. Now reflection is cpu wise more costly while storing
 * constructor args is more memory costly. May matter with large reports.
 */

public class ReportColumn implements Serializable {
    public enum DisplayType {HIDDEN, VISIBLE, ALLOW_DUPLICATES, CHART_SERIES_COLUMN, IS_RATE_RELATED}

    private static final long serialVersionUID = -6736366461333244457L;

    private List<DisplayType> displayTypes;
    private String columnHeaderResourceKey;
    private IConverter converter;

    private ColumnType columnType = ColumnType.STRING;

    public ReportColumn(String columnHeaderResourceKey) {
        this(columnHeaderResourceKey, ColumnType.STRING, DisplayType.VISIBLE, DisplayType.ALLOW_DUPLICATES, DisplayType.CHART_SERIES_COLUMN);
    }

    public ReportColumn(String columnHeaderResourceKey, ColumnType columnType, DisplayType... displayTypes) {
        this.columnHeaderResourceKey = columnHeaderResourceKey;
        this.displayTypes = Arrays.asList(displayTypes);
        this.columnType = columnType;
    }

    public ReportColumn(String columnHeaderResourceKey, ColumnType columnType, IConverter converter, DisplayType... displayTypes) {
        this(columnHeaderResourceKey, columnType, displayTypes);
        this.converter = converter;
    }

    public boolean isVisible() {
        if (displayTypes.contains(DisplayType.HIDDEN) || !displayTypes.contains(DisplayType.VISIBLE)) {
            return false;
        }

        boolean isVisibleAndAuthorized = true;

        if (displayTypes.contains(DisplayType.IS_RATE_RELATED)) {

            boolean showTurnover = EhourWebSession.getEhourConfig().isShowTurnover();

            if (!showTurnover) {
                isVisibleAndAuthorized = EhourWebSession.getSession().isReporter();
            }
        }

        return isVisibleAndAuthorized;
    }

    public String getColumnHeaderResourceKey() {
        return columnHeaderResourceKey;
    }

    public IConverter getConverter() {
        return converter;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public boolean isAllowDuplicates() {
        return displayTypes.contains(DisplayType.ALLOW_DUPLICATES);
    }

}
