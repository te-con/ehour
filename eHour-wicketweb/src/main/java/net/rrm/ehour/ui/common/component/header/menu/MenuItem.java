package net.rrm.ehour.ui.common.component.header.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.ui.common.util.AuthUtil;

import org.apache.wicket.markup.html.WebPage;

public class MenuItem implements Serializable
{
	private static final long serialVersionUID = -706357123349443669L;

	private Class<? extends WebPage> responsePageClass;
	private List<MenuItem> subMenus = new ArrayList<MenuItem>();
	private String titleId;
	
	public MenuItem(String titleId)
	{
		this.titleId = titleId;
	}

	public MenuItem(String title, Class<? extends WebPage> destinationPage)
	{
		this(title);
		
		this.responsePageClass = destinationPage;
	}
	
	public boolean isVisibleForLoggedInUser()
	{
		boolean visible = true;
		
		visible = isLink() ? AuthUtil.userAuthorizedForPage(responsePageClass) : false;
		
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
	
	public String getTitleId()
	{
		return titleId;
	}
}