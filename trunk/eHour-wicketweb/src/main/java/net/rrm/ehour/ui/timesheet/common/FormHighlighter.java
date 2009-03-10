/**
 * Created on Jul 10, 2007
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

package net.rrm.ehour.ui.timesheet.common;

import java.io.Serializable;

import net.rrm.ehour.ui.timesheet.panel.TimesheetTextField;

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

    	if (target != null)
    	{
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
			private static final long serialVersionUID = 1L;

			public Object getObject()
            {
                return "color: " + color;
            }
        });                        	
    }

}
