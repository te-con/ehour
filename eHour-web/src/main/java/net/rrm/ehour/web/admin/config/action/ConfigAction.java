/**
 * Created on Mar 17, 2007
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

package net.rrm.ehour.web.admin.config.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.web.admin.config.form.ConfigForm;
import net.rrm.ehour.web.admin.config.util.LocaleComparator;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class ConfigAction extends BaseConfigAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) 
	{
		ActionForward	fwd;
		ConfigForm		configForm = (ConfigForm)form;
		EhourConfig		dbConfig;
		
		dbConfig = configService.getConfiguration();
		request.setAttribute("config", dbConfig);
		
		if (!configForm.isFromForm())
		{
			configForm.setNoForce(dbConfig.getLocaleLanguage().equals("noForce"));
			configForm.setCurrency(dbConfig.getCurrency());
			configForm.setShowTurnOver(dbConfig.isShowTurnover());
			configForm.setMailFrom(dbConfig.getMailFrom());
			configForm.setMailSmtp(dbConfig.getMailSmtp());
			configForm.setRememberMeAvailable(dbConfig.isRememberMeAvailable());
		}

		request.setAttribute("form", configForm);
		
		setCurrencies(request);
		setAvailableLanguages(request, configForm.isShowTranslationsOnly());

		fwd = mapping.findForward("success");
		
		return fwd;
	}

	/**
	 * Set (sorted) currencies
	 * @param request
	 */

	private void setCurrencies(HttpServletRequest request)
	{
		SortedMap<String, String>	currencies = new TreeMap<String,String>(WebConstants.getCurrencies());
		
		request.setAttribute("currencies", currencies);
	}
	
	/**
	 * Set available languages
	 * @param request
	 */
	private void setAvailableLanguages(HttpServletRequest request, boolean onlyAvailable)
	{
		SortedSet<Locale>	localeSet = new TreeSet<Locale>(new LocaleComparator());
		Locale[]			locales = Locale.getAvailableLocales();
		Map<String, Locale>	localeMap = new HashMap<String, Locale>();
		
		// remove all variants
		for (Locale locale : locales)
		{
			if (onlyAvailable && !ArrayUtils.contains(config.getAvailableTranslations(), locale.getLanguage()))
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
		
		for (Locale locale : localeMap.values())
		{
			localeSet.add(locale);
		}
		
		request.setAttribute("languages", localeSet);
	}

}
