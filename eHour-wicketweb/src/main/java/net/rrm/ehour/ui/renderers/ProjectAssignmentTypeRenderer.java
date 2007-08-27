/**
 * Created on Aug 27, 2007
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

package net.rrm.ehour.ui.renderers;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.util.EhourConstants;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * ProjectAssignment renderer
 **/

public class ProjectAssignmentTypeRenderer implements IChoiceRenderer
{
	private static final long serialVersionUID = -4020935210828625185L;
	private	Component	comp;
	
	/**
	 * TODO sure a reference to a component is wanted?
	 * @param comp
	 */
	public ProjectAssignmentTypeRenderer(Component comp)
	{
		this.comp = comp;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(Object object)
	{
		if  (object instanceof ProjectAssignmentType)
		{
			ProjectAssignmentType	pat = (ProjectAssignmentType)object;
			
			String	key;
			
			switch (pat.getAssignmentTypeId().intValue())
			{
				case EhourConstants.ASSIGNMENT_DATE:
					key = "assignment.allotted";
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
			
			Localizer localizer = Application.get().getResourceSettings().getLocalizer();
			
			return localizer.getString(key, comp);
		}
		else
		{
			return object.toString();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
	 */
	public String getIdValue(Object object, int index)
	{
		if  (object instanceof ProjectAssignment)
		{
			return ((ProjectAssignment) object).getAssignmentId().toString();
		}

		return Integer.toString(index);	}

}
