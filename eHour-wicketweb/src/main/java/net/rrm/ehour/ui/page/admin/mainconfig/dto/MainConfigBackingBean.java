/**
 * Created on Jul 18, 2007
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

package net.rrm.ehour.ui.page.admin.mainconfig.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * TODO 
 **/

public class MainConfigBackingBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -682573285773646807L;
	private	boolean			translationsOnly;
	private	List<Locale>	availableLanguages;
	private	boolean			dontForceLocale;
	private	String			locale;
	
	public boolean isTranslationsOnly()
	{
		return translationsOnly;
	}
	public void setTranslationsOnly(boolean translationsOnly)
	{
		this.translationsOnly = translationsOnly;
	}
	public List<Locale> getAvailableLanguages()
	{
		return availableLanguages;
	}
	public void setAvailableLanguages(List<Locale> availableLanguages)
	{
		this.availableLanguages = availableLanguages;
	}
	public boolean isDontForceLocale()
	{
		return dontForceLocale;
	}
	public void setDontForceLocale(boolean dontForceLocale)
	{
		this.dontForceLocale = dontForceLocale;
	}
	public Locale getLocale()
	{
		return locale != null ? new Locale(locale) : Locale.getDefault();
	}
	public void setLocale(String locale)
	{
		this.locale = locale;
	}
	public void setLocale(Locale locale)
	{
		this.locale = locale.getLanguage();
	}	
	
}
