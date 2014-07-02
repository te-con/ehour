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

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.PmPrivilege;
import net.rrm.ehour.ui.admin.config.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.RangeValidator;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on Apr 21, 2009, 5:10:16 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class MiscConfigPanel extends AbstractConfigPanel {
    private static final long serialVersionUID = 2158470911726912430L;

    public MiscConfigPanel(String id, IModel<MainConfigBackingBean> model) {
        super(id, model);
    }

    @Override
    protected void addFormComponents(Form<?> form) {
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

        // pm access rights
        form.add(new DropDownChoice<PmPrivilege>("config.pmPrivilege", Arrays.asList(PmPrivilege.values()), new EnumChoiceRenderer<PmPrivilege>()));

        // split admin role
        // TODO when disabling, convert all managers to admins or at least do something with them
        form.add(new CheckBox("config.splitAdminRole"));
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

}
