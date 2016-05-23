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

package net.rrm.ehour.ui.admin.audit;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.panel.datepicker.DatePickerPanel;
import net.rrm.ehour.ui.common.validator.DateOverlapValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

public class AuditReportCriteriaForm extends Form<ReportCriteria> {
    private static final long serialVersionUID = -4033279032707727816L;

    public enum Events implements AjaxEventType {
        FORM_SUBMIT
    }

    public AuditReportCriteriaForm(String id, IModel<ReportCriteria> model) {
        super(id, model);

        addDates(model);

        AjaxButton submitButton = new AjaxButton(AuditConstants.PATH_FORM_SUBMIT, this) {
            private static final long serialVersionUID = -627058322154455051L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                AjaxEvent event = new AjaxEvent(Events.FORM_SUBMIT);
                EventPublisher.publishAjaxEvent(AuditReportCriteriaForm.this, event);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        };

        add(submitButton);

        TextField<String> nameField = new TextField<>(AuditConstants.PATH_FORM_NAME, new PropertyModel<String>(getModel(), "userSelectedCriteria.name"));
        add(nameField);

        TextField<String> actionField = new TextField<>(AuditConstants.PATH_FORM_ACTION, new PropertyModel<String>(getModel(), "userSelectedCriteria.action"));
        add(actionField);
    }

    private void addDates(final IModel<ReportCriteria> model) {
        DatePickerPanel dateStart = new DatePickerPanel("dateStart", new PropertyModel<Date>(model, "userSelectedCriteria.reportRange.dateStart"), new PropertyModel<Boolean>(model, "userSelectedCriteria.infiniteStartDate"));
        DatePickerPanel dateEnd = new DatePickerPanel("dateEnd", new PropertyModel<Date>(model, "userSelectedCriteria.reportRange.dateEnd"), new PropertyModel<Boolean>(model, "userSelectedCriteria.infiniteEndDate"));

        add(dateStart);
        add(dateEnd);

        add(new DateOverlapValidator("dateStartDateEnd", dateStart.getDateInputFormComponent(), dateEnd.getDateInputFormComponent()));
    }
}
