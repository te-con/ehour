package net.rrm.ehour.ui.common.panel.datepicker;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;

public class DateInputField extends TextField<Date> implements IHeaderContributor {

    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forStyle("S-");

    public DateInputField(String id, IModel<Date> dateIModel) {
        super(id, dateIModel);
    }

    @Override
    public IConverter getConverter(Class<?> type) {
        return new IConverter() {
            @Override
            public Object convertToObject(String value, Locale locale) {
                return FORMATTER.parseDateTime(value).toDate();
            }

            @Override
            public String convertToString(Object value, Locale locale) {
                return FORMATTER.print(new DateTime(value));
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        if (isVisible()) {
            response.renderOnDomReadyJavascript(enableDatePickerJavascript());
        }
    }

    public String enableDatePickerJavascript() {
        return String.format("$('#%s').datepicker({changeMonth:true,changeYear:true})", getMarkupId());
    }
}
