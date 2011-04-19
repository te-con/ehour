package net.rrm.ehour.ui.common.component.header.menu;

import net.rrm.ehour.ui.common.util.AuthUtil;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Serializable
{
	private static final long serialVersionUID = -706357123349443669L;

	private Class<? extends WebPage> responsePageClass;
	private PageParameters pageParameters;

	private List<MenuItem> subMenus = new ArrayList<MenuItem>();
	private String titleId;

	public MenuItem(String titleId)
	{
		this.titleId = titleId;
	}

	public MenuItem(String title, Class<? extends WebPage> destinationPage)
	{
		this(title, destinationPage, null);
	}

	public MenuItem(String title, Class<? extends WebPage> destinationPage, PageParameters pageParameters)
	{
		this(title);

		this.responsePageClass = destinationPage;
		this.pageParameters = pageParameters;
	}

	public boolean isVisibleForLoggedInUser()
	{
		boolean visible = isLink() && AuthUtil.isUserAuthorizedForPage(responsePageClass);

		for (MenuItem menu : subMenus)
		{
			visible |= menu.isVisibleForLoggedInUser();
		}

		return visible;
	}

	public boolean isLink()
	{
		return responsePageClass != null;
	}

	public void addSubMenu(MenuItem subMenuItem)
	{
		subMenus.add(subMenuItem);
	}

	public List<MenuItem> getSubMenus()
	{
		return subMenus;
	}

	public Class<? extends WebPage> getResponsePageClass()
	{
		return responsePageClass;
	}

	public PageParameters getPageParameters()
	{
		return pageParameters;
	}

	public String getTitleId()
	{
		return titleId;
	}
}