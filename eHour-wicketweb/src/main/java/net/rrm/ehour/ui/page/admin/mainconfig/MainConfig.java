/**
 * Created on Jul 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.admin.mainconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.page.admin.BaseAdminPage;
import net.rrm.ehour.ui.page.admin.mainconfig.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.panel.timesheet.FormHighlighter;
import net.rrm.ehour.ui.sort.LocaleComparator;
import net.rrm.ehour.ui.util.CommonStaticData;

import org.apache.commons.lang.ArrayUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Main config page
 **/
public class MainConfig extends BaseAdminPage
{
	private static final long serialVersionUID = 8613594529875207988L;

	@SpringBean
	private ConfigurationService	configService;

	private	final MainConfigBackingBean	configBackingBean = new MainConfigBackingBean();
	
	/**
	 * 
	 */
	public MainConfig()
	{
		super(new ResourceModel("admin.config.title"), null);
		
		EhourConfig	dbConfig;
		
		dbConfig = getDbConfig();
		updateBackingBean(dbConfig, false);
		
		setUpPage(dbConfig);
	}
	
	/**
	 * 
	 * @param config
	 */
	private void updateBackingBean(EhourConfig config, boolean translationsOnly)
	{
		configBackingBean.setDontForceLocale(config.getLocaleLanguage() == null || config.getLocaleLanguage().equals("noForce"));
		configBackingBean.setTranslationsOnly(translationsOnly);
		configBackingBean.setAvailableLanguages(getAvailableLanguages(config));
		configBackingBean.setLocale(config.getLocaleLanguage());
	}
	
	/**
	 * Setup page
	 */
	private void setUpPage(EhourConfig dbConfig)
	{
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("configFrame", new ResourceModel("admin.config.title"));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		greyBorder.add(blueBorder);
		add(greyBorder);

		createForm(blueBorder, dbConfig);
	}

	/**
	 * Create form
	 * 
	 * @param parent
	 */
	private void createForm(WebMarkupContainer parent, final EhourConfig dbConfig)
	{
		Form configForm = new Form("configForm", new CompoundPropertyModel(dbConfig));

		addLocaleSelections(configForm, dbConfig);
		
		// currency dropdown
		configForm.add(new DropDownChoice("currency",
											new ArrayList<String>(CommonStaticData.getCurrencies().keySet())));
		
		// show turnover checkbox
		configForm.add(new CheckBox("showTurnover"));
		
		// reply sender
		configForm.add(new RequiredTextField("mailFrom"));
		configForm.add(new RequiredTextField("mailSmtp"));
		
		setSubmitButton(configForm, dbConfig);
		
		parent.add(configForm);
	}
	
	/**
	 * Set ajax submit button
	 * @param form
	 * @param dbConfig
	 */
	private void setSubmitButton(Form form, final EhourConfig dbConfig)
	{
		form.add(new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				((EhourConfigStub)dbConfig).setLocaleLanguage(configBackingBean.isDontForceLocale() ? "noForce" : configBackingBean.getLocale().getLanguage());
				configService.persistConfiguration(dbConfig);
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}			
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form)
			{
                form.visitFormComponents(new FormHighlighter(target));
            }
        });		
	}
	
	/**
	 * Add locale selections
	 * @param configForm
	 * @param dbConfig
	 */
	
	private void addLocaleSelections(Form configForm, final EhourConfig dbConfig)
	{
		final DropDownChoice	localeDropDownChoice;
		final AjaxCheckBox		onlyTranslationsBox;
		
		configForm.setOutputMarkupId(true);
		
		localeDropDownChoice = new DropDownChoice("localeSelection",
													new PropertyModel(configBackingBean, "locale"),
													new PropertyModel(configBackingBean, "availableLanguages"),
													new LocaleChoiceRenderer()); 
		localeDropDownChoice.setEnabled(!configBackingBean.isDontForceLocale());
		localeDropDownChoice.setOutputMarkupId(true);
		configForm.add(localeDropDownChoice);
		
        setOutputMarkupId(true);

		// only translations
		onlyTranslationsBox = new AjaxCheckBox("onlyTranslations", new PropertyModel(configBackingBean, "translationsOnly"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				configBackingBean.setAvailableLanguages(getAvailableLanguages(dbConfig));
				target.addComponent(localeDropDownChoice);
			}
		};
		onlyTranslationsBox.setOutputMarkupId(true);
		onlyTranslationsBox.setEnabled(!configBackingBean.isDontForceLocale());
		configForm.add(onlyTranslationsBox);
		
		// don't force locale checkbox
		configForm.add(new AjaxCheckBox("dontForceLocale", new PropertyModel(configBackingBean, "dontForceLocale"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				localeDropDownChoice.setEnabled(!configBackingBean.isDontForceLocale());
				target.addComponent(localeDropDownChoice);
				
				onlyTranslationsBox.setEnabled(!configBackingBean.isDontForceLocale());
				target.addComponent(onlyTranslationsBox);
			}
		});				
	}
    
    /**
     * Choice for a locale.
     */
    private final class LocaleChoiceRenderer extends ChoiceRenderer
    {
        /**
         * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(Object)
         */
    	@Override
        public Object getDisplayValue(Object object)
        {
            Locale locale = (Locale)object;
            String display = locale.getDisplayName(getLocale());
            return display;
        }
    }    
	
	/**
	 * Get available languages
	 * @param config
	 * @param onlyAvailable
	 * @return
	 */
	private List<Locale> getAvailableLanguages(EhourConfig config)
	{
		Locale[]			locales = Locale.getAvailableLocales();
		Map<String, Locale>	localeMap = new HashMap<String, Locale>();
		
		// remove all variants
		for (Locale locale : locales)
		{
			if (configBackingBean.isTranslationsOnly()
					&& !ArrayUtils.contains(config.getAvailableTranslations(), locale.getLanguage()))
			{
				continue;
			}
			
			if (localeMap.containsKey(locale.getLanguage()))
			{
				if (locale.getDisplayName().indexOf('(') != -1)
				{
					continue;
				}
			}

			localeMap.put(locale.getLanguage(), locale);
		}
		
		
		SortedSet<Locale>	localeSet = new TreeSet<Locale>(new LocaleComparator());
		
		for (Locale locale : localeMap.values())
		{
			localeSet.add(locale);
		}
		
		
		return new ArrayList<Locale>(localeSet);
	}	
	
	/**
	 * 
	 * @return
	 */
	private EhourConfig getDbConfig()
	{
		return configService.getConfiguration();
	}
}
