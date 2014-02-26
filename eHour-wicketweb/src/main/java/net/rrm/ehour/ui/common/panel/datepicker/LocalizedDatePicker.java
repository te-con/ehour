package net.rrm.ehour.ui.common.panel.datepicker;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.model.IModel;

import java.util.Date;

public class LocalizedDatePicker extends DateTextField {
    public LocalizedDatePicker(String id, IModel<Date> dateModel) {
        super(id, dateModel, new DateTextFieldConfig().autoClose(true).withLanguage(EhourWebSession.getEhourConfig().getFormattingLocale().getLanguage()));
    }
}
