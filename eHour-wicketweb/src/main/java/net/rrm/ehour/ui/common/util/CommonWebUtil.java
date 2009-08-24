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

package net.rrm.ehour.ui.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.EhourConstants;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;

/**
 * Common functionality
 **/

public class CommonWebUtil
{
	public final static String ROLE_CONSULTANT = "ROLE_CONSULTANT";
	public final static String ROLE_ADMIN = "ROLE_ADMIN";
	public final static String ROLE_REPORT = "ROLE_REPORT";
	public final static String ROLE_PM = "ROLE_PROJECTMANAGER";
	
	/**
	 * 
	 */
	public static String formatDate(String format, Date date)
	{
		Locale locale = EhourWebSession.getSession().getEhourConfig().getLocale();
		
		SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
		
		return formatter.format(date);
	}

	
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
