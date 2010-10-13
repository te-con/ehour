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

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import net.rrm.ehour.ui.admin.config.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.model.DateModel;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Apr 21, 2009, 4:01:41 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class LocaleConfigPanel extends AbstractConfigPanel
{
	private static final long serialVersionUID = 3411339351917181309L;

	public LocaleConfigPanel(String id, IModel model)
	{
		super(id, model);
	}

	@SuppressWarnings("serial")
	@Override
	protected void addFormComponents(Form configForm)
	{
		final DropDownChoice	localeDropDownChoice;
		final DropDownChoice	languageDropDownChoice;
		final AjaxCheckBox		onlyTranslationsBox;
		final DropDownChoice	currencyDropDownChoice;
		final Label				dateFormat;
		
		final MainConfigBackingBean configBackingBean = (MainConfigBackingBean)getModelObject();
		
		configForm.setOutputMarkupId(true);

		// currency dropdown
		currencyDropDownChoice = new DropDownChoice("config.currency",
											new PropertyModel(configBackingBean, "availableCurrencies"),
											new LocaleChoiceRenderer(2));
		currencyDropDownChoice.setOutputMarkupId(true);
		configForm.add(currencyDropDownChoice);
		
		// date format example
		dateFormat = new Label("dateFormat", 
							new DateModel(new Model(new Date()), configBackingBean.getLocaleCountry(), DateModel.DATESTYLE_LONG ) );
		dateFormat.setOutputMarkupId(true);
		configForm.add(dateFormat);

		// language selection
		languageDropDownChoice = new DropDownChoice("localeLanguage",
													new PropertyModel(configBackingBean, "localeLanguage"),
													new PropertyModel(configBackingBean, "availableLanguages"),
													new LocaleChoiceRenderer(1)); 

		// locale selection
		localeDropDownChoice = new DropDownChoice("localeCountry",
													new PropertyModel(configBackingBean, "availableLocales"),
													new LocaleChoiceRenderer(0)); 
		localeDropDownChoice.setOutputMarkupId(true);
		localeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// update the date format example
				dateFormat.setModel(new DateModel(new Model(new Date()), configBackingBean.getLocaleCountry(), DateModel.DATESTYLE_LONG ) );
				target.addComponent(dateFormat);
				
				// refresh langugae
				target.addComponent(languageDropDownChoice);
				
				// and currency
				if (configBackingBean.getLocaleCountry().getCountry() != null)
				{
					configBackingBean.setCurrency(configBackingBean.getLocaleCountry());
					target.addComponent(currencyDropDownChoice);
				}
			}
		});
		
		configForm.add(localeDropDownChoice);

		// behaviour for language selection
		languageDropDownChoice.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
		languageDropDownChoice.setOutputMarkupId(true);
		languageDropDownChoice.setRequired(true);
		languageDropDownChoice.setLabel(new ResourceModel("admin.config.locale.languageLabel"));
		
		configForm.add(new AjaxFormComponentFeedbackIndicator("localeLanguageValidationError", localeDropDownChoice));
		configForm.add(languageDropDownChoice);		
		
		// only translations
		onlyTranslationsBox = new AjaxCheckBox("onlyTranslations", new PropertyModel(configBackingBean, "translationsOnly"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(languageDropDownChoice);
			}
		};
		onlyTranslationsBox.setOutputMarkupId(true);
		onlyTranslationsBox.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
		configForm.add(onlyTranslationsBox);
		
		// don't force locale checkbox
		configForm.add(new AjaxCheckBox("config.dontForceLanguage")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				languageDropDownChoice.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
				target.addComponent(languageDropDownChoice);
				
				onlyTranslationsBox.setEnabled(!configBackingBean.getConfig().isDontForceLanguage());
				target.addComponent(onlyTranslationsBox);
			}
		});				
	}
	
    /**
     * Choice for a locale.
     */
	@SuppressWarnings("serial")
    private final class LocaleChoiceRenderer extends ChoiceRenderer
    {
		int type;
		
		// type == 0 -> country, 1 => language, 2=> currency
		public LocaleChoiceRenderer(int type)
		{
			this.type = type;
		}
        /**
         * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(Object)
         */
    	@Override
        public Object getDisplayValue(Object object)
        {
            Locale locale = (Locale)object;
            String display;
            
            if (type == 0)
            {
	            display = locale.getDisplayCountry();
	            
	            display += " (" + locale.getDisplayLanguage();
	
	            if (!StringUtils.isBlank(locale.getVariant()))
	    		{
	            	display += ", " + locale.getDisplayVariant();
	    		}
	            
	            display += ")";
            }
            else if (type == 1)
            {
	            display = locale.getDisplayLanguage();
            }
            else
            {
            	Currency curr = Currency.getInstance(locale);
            	display = locale.getDisplayCountry() + ": " +  curr.getSymbol(locale);
            }

            return display;
        }

    	/*
    	 * (non-Javadoc)
    	 * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getIdValue(java.lang.Object, int)
    	 */
    	@Override
    	public String getIdValue(Object o, int index)
    	{
    		Locale locale = (Locale)o;
    		
    		if (type == 0)
    		{
    			return locale.getCountry() + "_" + locale.getLanguage();
    		}
    		else if (type == 1)
    		{
    			return locale.getLanguage();
    		}
    		else
    		{
    			return locale.getCountry();
    		}
    	}
    }
	
}
