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

package net.rrm.ehour.ui.common.model;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TODO rewrite this, should be a renderer or a converter; not a model
 */

public class DateModel implements IModel<String> {
    public static final int DATESTYLE_LONG = 1;
    public static final int DATESTYLE_MONTHONLY = 2;
    public static final int DATESTYLE_TIMESHEET_DAYLONG = 3;
    public static final int DATESTYLE_TIMESHEET_DAYONLY = 4;
    public static final int DATESTYLE_DAYONLY = 5;
    public static final int DATESTYLE_FULL_SHORT = 6;
    public static final int DATESTYLE_WEEK = 7;
    public static final int DATESTYLE_DAYONLY_LONG = 8;
    public static final int DATESTYLE_DATE_TIME = 9;
    public static final int DATESTYLE_FULL = 10;

    private String nullString = "&infin;";

    private static final long serialVersionUID = 431440606497572025L;
    private IModel model;
    private DateFormat dateFormatter;
    private int dateStyle;

    public DateModel(int dateStyle, String nullString) {
        this.dateStyle = dateStyle;
        this.nullString = nullString;
    }

    public DateModel(Calendar calendar, EhourConfig config) {
        this(calendar.getTime(), config, DATESTYLE_LONG);
    }

    public DateModel(Date date, EhourConfig config) {
        this(date, config, DATESTYLE_LONG);
    }

    public DateModel(Date date, EhourConfig config, int dateStyle) {
        this(new Model<>(date), config, dateStyle);
    }

    public DateModel(Calendar calendar, EhourConfig config, int dateStyle) {
        this(calendar.getTime(), config, dateStyle);
    }

    public DateModel(IModel model, EhourConfig config, int dateStyle) {
        this(model, config.getFormattingLocale(), dateStyle);

    }

    public DateModel(IModel model, Locale locale, int dateStyle) {
        this(locale, dateStyle);
        this.model = model;
    }

    public DateModel(Locale locale, int dateStyle) {
        initFormatter(locale, dateStyle);
    }

    private void initFormatter(int dateStyle) {
        initFormatter(EhourWebSession.getEhourConfig().getFormattingLocale(), dateStyle);
    }

    private void initFormatter(Locale locale, int dateStyle) {
        switch (dateStyle) {
            case DATESTYLE_MONTHONLY:
                dateFormatter = new SimpleDateFormat("MMMM yyyy", locale);
                break;
            case DATESTYLE_TIMESHEET_DAYLONG:
                dateFormatter = new TimesheetLongFormatter("EEE d", locale);
                break;
            case DATESTYLE_TIMESHEET_DAYONLY:
                dateFormatter = new TimesheetLongFormatter("EEE", locale);
                break;
            case DATESTYLE_DAYONLY:
                dateFormatter = new TimesheetLongFormatter("dd", locale);
                break;
            case DATESTYLE_DAYONLY_LONG:
                dateFormatter = new TimesheetLongFormatter("EEEE", locale);
                break;
            case DATESTYLE_FULL_SHORT:
                dateFormatter = new TimesheetLongFormatter(DateUtil.getPatternForDateLocale(locale), locale);
                break;
            case DATESTYLE_WEEK:
                dateFormatter = new TimesheetLongFormatter("w", locale);
                break;
            case DATESTYLE_DATE_TIME:
                dateFormatter = new TimesheetLongFormatter("dd MMM yy HH:mm:ss", locale, false);
                break;
            case DATESTYLE_FULL:
                dateFormatter = new TimesheetLongFormatter("dd MMM yy", locale, false);
                break;
            case DATESTYLE_LONG:
            default:
                dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                break;
        }
    }

    @Override
    public String getObject() {
        if (dateFormatter == null) {
            initFormatter(dateStyle);
        }

        return (model == null || model.getObject() == null) ? nullString : dateFormatter.format((Date) model.getObject());
    }

    public void setObject(String value) {
        if (value != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            try {
                this.model = new Model<>(format.parse(value));
            } catch (ParseException e) {
                this.model = null;
            }
        }
    }

    private static class TimesheetLongFormatter extends SimpleDateFormat {
        private static final long serialVersionUID = 2697598002926018462L;
        private boolean breakSpaces = true;

        public TimesheetLongFormatter(String format, Locale locale) {
            this(format, locale, true);
        }

        public TimesheetLongFormatter(String format, Locale locale, boolean breakSpaces) {
            super(format, locale);
            this.breakSpaces = breakSpaces;
        }

        @Override
        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            StringBuffer sb = super.format(date, toAppendTo, fieldPosition);

            if (breakSpaces) {
                return new StringBuffer(sb.toString().replace(" ", "<br />"));
            } else {
                return sb;
            }
        }
    }

    public void detach() {
    }
}
