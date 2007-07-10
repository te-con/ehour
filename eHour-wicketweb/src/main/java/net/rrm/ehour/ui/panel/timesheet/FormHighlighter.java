/**
 * Created on Jul 10, 2007
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

package net.rrm.ehour.ui.panel.timesheet;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Marks invalid form entries red
 * @author Thies
 *
 */
public class FormHighlighter implements FormComponent.IVisitor, Serializable 
{
	private static final long serialVersionUID = 6905807838333630105L;
	private transient AjaxRequestTarget	target;

	public FormHighlighter(AjaxRequestTarget target)
	{
		this.target = target;
	}
	
    public Object formComponent(IFormVisitorParticipant visitor)
    {
    	FormComponent formComponent = (FormComponent)visitor;

    	// paint it red if invalid
        if (!formComponent.isValid())
        {
        	formComponent.add(getColorModifier("#ff0000"));

        	if (formComponent instanceof TimesheetTextField)
        	{
        		((TimesheetTextField)formComponent).setWasInvalid(true);
        	}
        	
            target.addComponent(formComponent);
        } 
        // reset color if it was invalid
        else if (formComponent instanceof TimesheetTextField)
        {
        	TimesheetTextField ttField = (TimesheetTextField)formComponent;
        	
        	if (ttField.isWasInvalid() || ttField.isChanged())
        	{
	        	formComponent.add(getColorModifier("#536e87;"));
	        	((TimesheetTextField)formComponent).setWasInvalid(false);
	        	target.addComponent(formComponent);
        	}
        }
        
        return formComponent;
    }
    
    /**
     * Get color modifier
     * @param color
     * @return
     */
    private AttributeModifier getColorModifier(final String color)
    {
    	return new AttributeModifier("style", true, new AbstractReadOnlyModel()
        {
            public Object getObject()
            {
                return "color: " + color;
            }
        });                        	
    }

}
