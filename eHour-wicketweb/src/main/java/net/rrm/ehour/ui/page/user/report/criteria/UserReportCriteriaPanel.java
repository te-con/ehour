/**
 * Created on Jul 9, 2007
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

package net.rrm.ehour.ui.page.user.report.criteria;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.ProjectChoiceRender;
import net.rrm.ehour.ui.panel.sidepanel.SidePanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;
import org.wicketstuff.dojo.toggle.DojoFadeToggle;

/**
 * Date range selection
 **/

public class UserReportCriteriaPanel extends SidePanel
{
	private static final long serialVersionUID = -6129389795390181179L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public UserReportCriteriaPanel(String id, ReportCriteria reportCriteria)
	{
		super(id);

		Form form = new Form("criteriaForm");
		
		DropDownChoice projectDropDown;
		projectDropDown = new DropDownChoice("project", 
											new PropertyModel(reportCriteria, "userCriteria"), 
											reportCriteria.getAvailableCriteria().getProjects(),
											new ProjectChoiceRender());
		form.add(projectDropDown);
		
		addDatePickers(form, reportCriteria);
		
		this.add(new FeedbackPanel("feedback"));

		this.add(form);
	}

	/**
	 * Add date pickters
	 * @param parent
	 * @param reportCriteria
	 */
	private void addDatePickers(WebMarkupContainer parent, ReportCriteria reportCriteria)
	{
		DojoDatePicker dateStart = new DojoDatePicker("dateStart", 
														new PropertyModel(reportCriteria, "userCriteria.reportRange.dateStart"),
														"dd/MM/yyyy");
		dateStart.setToggle(new DojoFadeToggle(200));
		parent.add(dateStart);

		DojoDatePicker dateEnd = new DojoDatePicker("dateEnd", 
													new PropertyModel(reportCriteria, "userCriteria.reportRange.dateEnd"),
													"dd/MM/yyyy");
		dateEnd.setToggle(new DojoFadeToggle(600));
		parent.add(dateEnd);
	}
}
