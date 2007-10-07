/**
 * Created on Jul 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
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
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.FadeLabel;
import net.rrm.ehour.ui.page.admin.BaseAdminPage;
import net.rrm.ehour.ui.page.admin.mainconfig.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.sort.LocaleComparator;
import net.rrm.ehour.ui.util.CommonUIStaticData;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

/**
 * Main config page
 **/
public class MainConfig extends BaseAdminPage
{
	private static final long serialVersionUID = 8613594529875207988L;
	private static final Logger logger = Logger.getLogger(MainConfig.class);
	
	@SpringBean
	private ConfigurationService	configService;

	private	final MainConfigBackingBean	configBackingBean = new MainConfigBackingBean();
	private Label	serverMessage;
	
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
		configForm.setOutputMarkupId(true);
		
		addLocaleSelections(configForm, dbConfig);
		
		// currency dropdown
		configForm.add(new DropDownChoice("currency",
											new ArrayList<String>(CommonUIStaticData.getCurrencies().keySet())));
		
		// show turnover checkbox
		configForm.add(new CheckBox("showTurnover"));
		
		// reply sender
		RequiredTextField mailFrom = new RequiredTextField("mailFrom");
		mailFrom.add(EmailAddressValidator.getInstance());
		configForm.add(mailFrom);
		configForm.add(new AjaxFormComponentFeedbackIndicator("mailFromError", mailFrom));
		
		configForm.add(new RequiredTextField("mailSmtp"));
		
		setSubmitButton(configForm, dbConfig);
		
		parent.add(configForm);
		
		serverMessage = new FadeLabel("serverMessage", "&nbsp;");
		serverMessage.setEscapeModelStrings(false);
		serverMessage.setOutputMarkupId(true);
		configForm.add(serverMessage);
	}
	
	/**
	 * Set ajax submit button
	 * @param form
	 * @param dbConfig
	 */
	@SuppressWarnings("serial")
	private void setSubmitButton(Form form, final EhourConfig dbConfig)
	{
		form.add(new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				IModel msgModel;
				
				((EhourConfigStub)dbConfig).setLocaleLanguage(configBackingBean.isDontForceLocale() ? "noForce" : configBackingBean.getLocale().getLanguage());
				
				try
				{
					configService.persistConfiguration(dbConfig);
					msgModel = new ResourceModel("dataSaved");
				}
				catch (Throwable t)
				{
					logger.error("While saving config", t);
					msgModel = new ResourceModel("saveError");
				}
				
				getEhourWebSession().reloadConfig();
				
				serverMessage.setModel(msgModel);
				target.addComponent(serverMessage);
            }		

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}			
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form)
			{
				target.addComponent(form);
            }
        });		
	}
	
	/**
	 * Add locale selections
	 * @param configForm
	 * @param dbConfig
	 */
	@SuppressWarnings("serial")
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
		localeDropDownChoice.setRequired(true);
		localeDropDownChoice.setLabel(new ResourceModel("admin.config.locale.languageLabel"));
		configForm.add(new AjaxFormComponentFeedbackIndicator("localeValidationError", localeDropDownChoice));
		configForm.add(localeDropDownChoice);
		
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
	@SuppressWarnings("serial")
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
