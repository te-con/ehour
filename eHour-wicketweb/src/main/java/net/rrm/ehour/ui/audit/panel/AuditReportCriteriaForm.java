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

package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.audit.AuditConstants;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.DynamicAttributeModifier;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.validator.ConditionalRequiredValidator;
import net.rrm.ehour.ui.common.validator.DateOverlapValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import java.util.Date;

/**
 *
 * @author thies
 *
 */
public class AuditReportCriteriaForm extends Form<ReportCriteria>
{
	private static final long serialVersionUID = -4033279032707727816L;

	public enum Events implements AjaxEventType
	{
		FORM_SUBMIT
    }

	public AuditReportCriteriaForm(String id, IModel<ReportCriteria> model)
	{
		super(id, model);

		addDates(model);

		AjaxButton submitButton = new AjaxButton(AuditConstants.PATH_FORM_SUBMIT, this)
		{
			private static final long serialVersionUID = -627058322154455051L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				AjaxEvent event = new AjaxEvent(Events.FORM_SUBMIT);
				EventPublisher.publishAjaxEvent(AuditReportCriteriaForm.this, event);
			}
		};

		add(submitButton);

		TextField<String> nameField = new TextField<String>(AuditConstants.PATH_FORM_NAME, new PropertyModel<String>(getModel(), "userCriteria.name"));
		add(nameField);

		TextField<String> actionField = new TextField<String>(AuditConstants.PATH_FORM_ACTION, new PropertyModel<String>(getModel(), "userCriteria.action"));
		add(actionField);
	}

	/**
	 * Add start & end dates
	 *
	 * @param form
	 * @param model
	 */
	private void addDates(final IModel<ReportCriteria> model)
	{
		PropertyModel<Boolean> infiniteStartDateModel = new PropertyModel<Boolean>(model, "userCriteria.infiniteStartDate");
		PropertyModel<Boolean> infiniteEndDateModel = new PropertyModel<Boolean>(model, "userCriteria.infiniteEndDate");

		// start date
		DateTextField dateStart = new DateTextField("dateStart", new PropertyModel<Date>(model, "userCriteria.reportRange.dateStart"), new StyleDateConverter("S-", true));

		dateStart.add(new ConditionalRequiredValidator<Date>(infiniteStartDateModel));
		dateStart.add(new ValidatingFormComponentAjaxBehavior());
		dateStart.setLabel(new ResourceModel("admin.assignment.dateStart"));
		dateStart.add(new DatePicker());

		// container for hiding
		WebMarkupContainer startDateHider = new WebMarkupContainer(AuditConstants.PATH_FORM_START_DATE_HIDER);
		startDateHider.setOutputMarkupId(true);

		// indicator for validation issues
		startDateHider.add(new AjaxFormComponentFeedbackIndicator("dateStartValidationError", dateStart));

		// the inner hider is just there to hide the <br /> as well
		WebMarkupContainer innerStartDateHider = new WebMarkupContainer("innerStartDateHider");
		innerStartDateHider.setOutputMarkupId(true);
		innerStartDateHider.add(dateStart);
		innerStartDateHider.add(new DynamicAttributeModifier("style", true, new Model<String>("display: none;"), infiniteStartDateModel, true));

		startDateHider.add(innerStartDateHider);

		add(startDateHider);

		// infinite start date toggle
		AjaxCheckBox infiniteStart = new AjaxCheckBox("infiniteStartDate", infiniteStartDateModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(AuditReportCriteriaForm.this.get(AuditConstants.PATH_FORM_START_DATE_HIDER));
			}
		};

		startDateHider.add(infiniteStart);

		// end date
		DateTextField dateEnd = new DateTextField("dateEnd", new PropertyModel<Date>(model, "userCriteria.reportRange.dateEnd"), new StyleDateConverter("S-", false));
		dateEnd.add(new DatePicker());
		// container for hiding

		dateEnd.add(new ValidatingFormComponentAjaxBehavior());
		dateEnd.add(new ConditionalRequiredValidator<Date>(infiniteEndDateModel));
		dateEnd.setLabel(new ResourceModel("admin.assignment.dateEnd"));

		WebMarkupContainer endDateHider = new WebMarkupContainer(AuditConstants.PATH_FORM_END_DATE_HIDER);
		endDateHider.setOutputMarkupId(true);

		// indicator for validation issues
		endDateHider.add(new AjaxFormComponentFeedbackIndicator("dateEndValidationError", dateEnd));

		// the inner hider is just there to hide the <br /> as well
		WebMarkupContainer innerEndDateHider = new WebMarkupContainer("innerEndDateHider");
		innerEndDateHider.setOutputMarkupId(true);
		innerEndDateHider.add(dateEnd);
		innerEndDateHider.add(new DynamicAttributeModifier("style", true, new Model<String>("display: none;"), infiniteEndDateModel, true));
		endDateHider.add(innerEndDateHider);
		add(endDateHider);

		// infinite end date toggle
		AjaxCheckBox infiniteEnd = new AjaxCheckBox("infiniteEndDate", infiniteEndDateModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(AuditReportCriteriaForm.this.get(AuditConstants.PATH_FORM_END_DATE_HIDER));
			}
		};

		endDateHider.add(infiniteEnd);

		add(new DateOverlapValidator("auditStartEndDate", dateStart, dateEnd));
	}
}
