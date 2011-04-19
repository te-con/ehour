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

package net.rrm.ehour.ui.common.panel.contexthelp;

import net.rrm.ehour.ui.EhourWebApplication;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Contextual help panel, the one that appears most of the time in the left bottom
 **/

public class ContextualHelpPanel extends Panel
{
	private static final long serialVersionUID = 8054029544833333835L;
	
	public ContextualHelpPanel(String id, String headerResourceId, String bodyResourceId)
	{
		this(id, headerResourceId, bodyResourceId, null);
	}
	
	public ContextualHelpPanel(String id, String headerResourceId, String bodyResourceId, String wikiPageTitle)
	{
		super(id);
		
		add(new Label("header", new ResourceModel(headerResourceId)));
		Label body = new Label("body", new ResourceModel(bodyResourceId));
		body.setEscapeModelStrings(false);
		add(body);
		add(createReadMoreLink(wikiPageTitle));
		setOutputMarkupId(true);
	}
	
	@SuppressWarnings("serial")
	private Link<Void> createReadMoreLink(final String pageTitle)
	{
		Link<Void> readMoreLink = new Link<Void>("readMoreLink")
		{
			@Override
			public void onClick()
			{
				StringBuilder url = new StringBuilder(EhourWebApplication.get().getWikiBaseUrl());
				url.append("/");
				
				if (!StringUtils.isBlank(pageTitle))
				{
					url.append(pageTitle);
				}
				
				setResponsePage(new RedirectPage(url.toString()));
			}
		};
		
		readMoreLink.setVisible(!StringUtils.isBlank(pageTitle));
		
		return readMoreLink;
	}

}
