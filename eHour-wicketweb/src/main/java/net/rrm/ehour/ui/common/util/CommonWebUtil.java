/**
 * Created on Jun 19, 2007
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

package net.rrm.ehour.ui.common.util;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.util.EhourConstants;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;

/**
 * Commons 
 **/

public class CommonWebUtil
{
//	public final static int	AJAX_CALENDARPANEL_MONTH_CHANGE = 1;
//	public final static int	AJAX_CALENDARPANEL_WEEK_CLICK = 2;
//	public final static int	AJAX_ENTRYSELECTOR_FILTER_CHANGE = 3;
//	public final static int	AJAX_FORM_SUBMIT = 4;
//	public final static int	AJAX_DELETE = 5;
//	public final static int AJAX_LIST_CHANGE = 6;
//	public final static int	AJAX_CALENDARPANEL_WEEK_NAV = 7;
//	public final static int AJAX_CUSTOMER_ADD_TAB_REQUEST = 8;
	
	public final static String ROLE_CONSULTANT = "ROLE_CONSULTANT";
	public final static String ROLE_ADMIN = "ROLE_ADMIN";
	public final static String ROLE_REPORT = "ROLE_REPORT";
	public final static String ROLE_PM = "ROLE_PROJECTMANAGER";
	
	public final static int GREYFRAME_WIDTH = 730;
	
//	public final static String[] weekDays = new String[]{"sunday", "monday", "tuesday", 
//															"wednesday", "thursday", "friday", "saturday"};
	
	/**
	 * Inject beans into @SpringBean annotated properties
	 */
	public static void springInjection(Object injectionTarget)
	{
		InjectorHolder.getInjector().inject(injectionTarget);
	}
	
	/**
	 * Get a list of the components in the parent's children that match the classToFind
	 * @param parent
	 * @param classToFind
	 * @return
	 */
	public static <CO extends Component> List<CO> findComponent(MarkupContainer parent, final Class<CO> classToFind)
	{
		final List<CO> components = new ArrayList<CO>();
		
		parent.visitChildren(new Component.IVisitor()
		{

			@SuppressWarnings("unchecked")
			public Object component(Component component)
			{
				if (component.getClass() == classToFind)
				{
					components.add((CO)component);
				}
				
				return IVisitor.CONTINUE_TRAVERSAL;
			}
			
		});
		
		return components;
	}
	
	/**
	 * Get the content of a resource model as a string
	 * @param model
	 * @return
	 */
	public static String getResourceModelString(IModel model)
	{
		return (String)model.getObject();
	}
	
	/**
	 * Get resource key for project assignment type
	 * @param type
	 * @return
	 */
	public static String getResourceKeyForProjectAssignmentType(ProjectAssignmentType type)
	{
		String	key;
		switch (type.getAssignmentTypeId().intValue())
		{
			case EhourConstants.ASSIGNMENT_DATE:
				key = "assignment.dateRange";
				break;
			case EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED:
				key = "assignment.allottedFixed";
				break;
			case EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX:
				key = "assignment.allottedFlex";
				break;
			default:
				key = "assignment.allotted";
				break;
		}
		
		return key;
	}
	
	/**
	 * Get resource key for user role
	 * @param role
	 * @return
	 */
	public static String getResourceKeyForUserRole(UserRole role)
	{
		String key = null;
		
		if (role.getRole().equals(EhourConstants.ROLE_CONSULTANT))
		{
			key = "role.ROLE_CONSULTANT";
		} 
		else if (role.getRole().equals(EhourConstants.ROLE_REPORT))
		{
			key = "role.ROLE_REPORT";
		}
		else if (role.getRole().equals(EhourConstants.ROLE_PROJECTMANAGER))
		{
			key = "role.ROLE_PROJECTMANAGER";
		}
		else if (role.getRole().equals(EhourConstants.ROLE_ADMIN))
		{
			key = "role.ROLE_ADMIN";
		}
		return key;		
	}
}
