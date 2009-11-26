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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.admin.config.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;

/**
 * Created on Apr 21, 2009, 5:10:16 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class MiscConfigPanel extends AbstractConfigPanel
{
	private static final long serialVersionUID = 2158470911726912430L;

	/**
	 * @param id
	 * @param model
	 */
	public MiscConfigPanel(String id, IModel<MainConfigBackingBean> model)
	{
		super(id, model);
	}

	@Override
	protected void addFormComponents(Form<MainConfigBackingBean> form)
	{
		// show turnover checkbox
		form.add(new CheckBox("config.showTurnover"));		
		
		final MainConfigBackingBean configBackingBean = (MainConfigBackingBean)getDefaultModelObject();

		// working hours
		TextField<Float> workHours = new TextField<Float>("config.completeDayHours", Float.class);
		workHours.add(new ValidatingFormComponentAjaxBehavior());
		workHours.add(new MinimumValidator<Float>(0f));
		workHours.add(new MaximumValidator<Float>(24f));
		form.add(new AjaxFormComponentFeedbackIndicator("workHoursValidationError", workHours));
		form.add(workHours);
		
		// weeks start at
		
		DropDownChoice<Date> weekStartsAt = new DropDownChoice<Date>("firstWeekStart",
															DateUtil.createDateSequence(DateUtil.getDateRangeForWeek(new GregorianCalendar()), new EhourConfigStub()),
															new WeekDayRenderer(configBackingBean.getLocaleLanguage()));
		form.add(weekStartsAt);
		
	}
	
	private final class WeekDayRenderer extends ChoiceRenderer<Date>
	{
		private static final long serialVersionUID = -2044803875511515992L;
		SimpleDateFormat formatter;
		
		public WeekDayRenderer(Locale locale)
		{
			formatter = new SimpleDateFormat("EEEE", locale);
		}
    	@Override
        public Object getDisplayValue(Date date)
    	{
    		return formatter.format(date);
    	}
    	
    	@Override
    	public String getIdValue(Date date, int index)
    	{
    		Calendar cal = new GregorianCalendar();
    		cal.setTime(date);
    		return Integer.toString(cal.get(Calendar.DAY_OF_WEEK));
    	}    	
	}	

}
