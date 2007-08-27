/**
 * Created on Aug 21, 2007
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

package net.rrm.ehour.ui.panel.admin.common;

import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.util.CommonStaticData;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;

/**
 * Common form stuff
 **/
@SuppressWarnings("serial")
public class FormUtil
{
	/**
	 * Set submit actions for form
	 * @param form
	 */
	public static void setSubmitActions(final Form form, boolean includeDelete)
	{
		Button submitButton = new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				((BasePage)getPage()).ajaxRequestReceived(target, CommonStaticData.AJAX_FORM_SUBMIT, form.getModel());
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}
			
			@Override
            protected void onError(AjaxRequestTarget target, Form form)
			{
				target.addComponent(form);
            }
			
        };
        
        submitButton.setModel(new ResourceModel("general.save"));
		// default submit
		form.add(submitButton);

		AjaxLink deleteButton = new AjaxLink("deleteButton", new ResourceModel("general.delete"))
        {
			@Override
            public void onClick(AjaxRequestTarget target)
			{
				((BasePage)getPage()).ajaxRequestReceived(target, CommonStaticData.AJAX_DELETE, form.getModel());
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}		
        };
        
        deleteButton.add(new JavaScriptConfirmation("onclick", new ResourceModel("deleteConfirmation")));
        
        deleteButton.setModel(new ResourceModel("general.delete"));
        deleteButton.setVisible(includeDelete);
        form.add(deleteButton);
        
	}	
}
