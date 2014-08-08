/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.admin.config.panel;

import com.google.common.collect.Lists;
import net.rrm.ehour.appconfig.EhourSystemConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.PmPrivilege;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.config.MainConfigBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.renderers.UserRoleRenderer;
import net.rrm.ehour.ui.common.wicket.Container;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.WordUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on Apr 21, 2009, 5:10:16 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class MiscConfigPanel extends AbstractConfigPanel {
    private static final long serialVersionUID = 2158470911726912430L;

    @SpringBean
    private UserService userService;

    @SpringBean
    private EhourSystemConfig ehourSystemConfig;

    public MiscConfigPanel(String id, IModel<MainConfigBackingBean> model) {
        super(id, model);
    }

    @Override
    protected void addFormComponents(Form<?> form) {
        addMiscComponents(form);

        addReminderComponents(form);
    }

    private void addMiscComponents(Form<?> form) {
        // show turnover checkbox
        form.add(new CheckBox("config.showTurnover"));

        final MainConfigBackingBean configBackingBean = (MainConfigBackingBean) getDefaultModelObject();

        // working hours
        TextField<Float> workHours = new TextField<Float>("config.completeDayHours", Float.class);
        workHours.setLabel(new ResourceModel("admin.config.workHours"));
        workHours.add(new ValidatingFormComponentAjaxBehavior());
        workHours.add(RangeValidator.minimum(0f));
        workHours.add(RangeValidator.maximum(24f));
        workHours.setRequired(true);
        form.add(new AjaxFormComponentFeedbackIndicator("workHoursValidationError", workHours));
        form.add(workHours);

        // weeks start at
        DropDownChoice<Date> weekStartsAt = new DropDownChoice<Date>("firstWeekStart",
                DateUtil.createDateSequence(DateUtil.getDateRangeForWeek(new GregorianCalendar()), new EhourConfigStub()),
                new WeekDayRenderer(configBackingBean.getLocaleLanguage()));
        form.add(weekStartsAt);

        // Timezone
        System.out.println(getPanelModelObject().getConfig().getTimeZone());


        DropDownChoice<String> timezone = new DropDownChoice<String>("config.timeZone", Lists.newArrayList(DateTimeZone.getAvailableIDs()));
        form.add(timezone);

        // pm access rights
        form.add(new DropDownChoice<PmPrivilege>("config.pmPrivilege", Arrays.asList(PmPrivilege.values()), new EnumChoiceRenderer<PmPrivilege>()));

        // split admin role
        final Container convertManagersContainer = new Container("convertManagers");
        DropDownChoice<UserRole> convertManagersTo = new DropDownChoice<UserRole>("convertManagersTo", Lists.newArrayList(UserRole.ADMIN, UserRole.USER), new UserRoleRenderer());
        convertManagersContainer.add(convertManagersTo);
        convertManagersContainer.setVisible(false);
        form.add(convertManagersContainer);

        AjaxCheckBox withManagerCheckbox = new AjaxCheckBox("config.splitAdminRole") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Boolean managersEnabled = this.getModelObject();

                boolean showConvert = !managersEnabled && userService.getUsers(UserRole.MANAGER).size() > 0;

                if (convertManagersContainer.isVisible() != showConvert) {
                    convertManagersContainer.setVisible(showConvert);
                    target.add(convertManagersContainer);
                }
            }
        };

        form.add(withManagerCheckbox);
    }

    private void addReminderComponents(Form<?> form) {
        Container disabled = new Container("mailDisabled");
        disabled.setVisible(!ehourSystemConfig.isEnableMail());
        form.add(disabled);

        // minimum hours
        final ReminderContainer reminderMinHoursContainer = new ReminderContainer("reminderMinHoursContainer");
        form.add(reminderMinHoursContainer);

        final TextField<Integer> reminderMinHours = new TextField<Integer>("config.reminderMinimalHours");
        reminderMinHours.add(new UpdateBehavior());
        reminderMinHours.add(new ValidatingFormComponentAjaxBehavior());
        reminderMinHoursContainer.add(new AjaxFormComponentFeedbackIndicator("minHoursValidationError", reminderMinHours));
        reminderMinHours.add(RangeValidator.minimum(0));
        reminderMinHoursContainer.add(reminderMinHours);

        final Container reminderMinHoursHelpContainer = new ReminderContainer("reminderMinHoursHelpContainer");
        form.add(reminderMinHoursHelpContainer);

        // carbon copy
        final ReminderContainer reminderCcContainer = new ReminderContainer("reminderCcContainer");
        form.add(reminderCcContainer);

        final TextField<String> reminderCc = new TextField<String>("config.reminderCC");
        reminderCc.add(new UpdateBehavior());
        reminderCcContainer.add(reminderCc);

        final Container reminderCcHelpContainer = new ReminderContainer("reminderCcHelpContainer");
        form.add(reminderCcHelpContainer);

        // body
        final ReminderContainer reminderBodyContainer = new ReminderContainer("reminderBodyContainer");
        form.add(reminderBodyContainer);

        final TextArea<String> reminderBody = new TextArea<String>("config.reminderBody");
        reminderBody.add(new ValidatingFormComponentAjaxBehavior());
        reminderBody.add(StringValidator.maximumLength(4095));
        reminderBodyContainer.add(new AjaxFormComponentFeedbackIndicator("bodyValidationError", reminderBody));
        reminderBody.setLabel(new ResourceModel("admin.config.reminder.body"));
        reminderBody.add(new UpdateBehavior());
        reminderBodyContainer.add(reminderBody);

        // subject
        final ReminderContainer reminderSubjectContainer = new ReminderContainer("reminderSubjectContainer");
        form.add(reminderSubjectContainer);

        final TextField<String> reminderSubject = new TextField<String>("config.reminderSubject");
        reminderSubject.add(new ValidatingFormComponentAjaxBehavior());
        reminderSubject.add(StringValidator.maximumLength(4095));
        reminderSubjectContainer.add(new AjaxFormComponentFeedbackIndicator("subjectValidationError", reminderBody));
        reminderSubject.setLabel(new ResourceModel("admin.config.reminder.subject"));
        reminderSubject.add(new UpdateBehavior());
        reminderSubjectContainer.add(reminderSubject);

        // reminder time
        final Container reminderTimeContainer = new ReminderContainer("reminderTimeContainer");
        form.add(reminderTimeContainer);

        reminderTimeContainer.add(new DropDownChoice<String>("reminderDay", MainConfigBackingBean.VALID_REMINDER_DAYS, new IChoiceRenderer<String>() {
            @Override
            public Object getDisplayValue(String object) {
                return WordUtils.capitalizeFully(object);
            }

            @Override
            public String getIdValue(String object, int index) {
                return Integer.toString(index);
            }
        }));
        reminderTimeContainer.add(new DropDownChoice<Integer>("reminderHour", integerListTo(23)));
        reminderTimeContainer.add(new DropDownChoice<Integer>("reminderMinute", integerListTo(59)));

        // enable flag
        AjaxCheckBox reminderEnabledCheckbox = new AjaxCheckBox("config.reminderEnabled") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(reminderTimeContainer, reminderSubjectContainer, reminderBodyContainer, reminderCcContainer, reminderCcHelpContainer, reminderMinHoursContainer, reminderMinHoursHelpContainer);
            }
        };

        reminderEnabledCheckbox.setEnabled(ehourSystemConfig.isEnableMail());
        form.add(reminderEnabledCheckbox);
    }

    private List<Integer> integerListTo(int max) {
        List<Integer> xs = Lists.newArrayList();

        for (int i = 0; i < max; i++) {
            xs.add(i);
        }
        return xs;
    }

    private class ReminderContainer extends Container {
        public ReminderContainer(String id) {
            super(id);

            setOutputMarkupPlaceholderTag(true);
        }

        @Override
        public boolean isVisible() {
            return ehourSystemConfig.isEnableMail() && getPanelModelObject().getConfig().isReminderEnabled();
        }
    }

    private static final class WeekDayRenderer extends ChoiceRenderer<Date> {
        private static final long serialVersionUID = -2044803875511515992L;
        SimpleDateFormat formatter;

        public WeekDayRenderer(Locale locale) {
            formatter = new SimpleDateFormat("EEEE", locale);
        }

        @Override
        public Object getDisplayValue(Date date) {
            return formatter.format(date);
        }

        @Override
        public String getIdValue(Date date, int index) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            return Integer.toString(cal.get(Calendar.DAY_OF_WEEK));
        }
    }


    private static class UpdateBehavior extends AjaxFormComponentUpdatingBehavior {
        private UpdateBehavior() {
            super("change");
        }

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            // it's just the update of the model that's needed
        }
    }
}
