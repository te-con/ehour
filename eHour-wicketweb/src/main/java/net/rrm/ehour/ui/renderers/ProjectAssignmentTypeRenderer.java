/**
 * Created on Aug 27, 2007
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

package net.rrm.ehour.ui.renderers;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.ui.util.CommonWebUtil;

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
			
			String	key = CommonWebUtil.getResourceKeyForProjectAssignmentType(pat);
			
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
