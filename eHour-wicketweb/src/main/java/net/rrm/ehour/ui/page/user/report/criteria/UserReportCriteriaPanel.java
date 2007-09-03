/**
 * Created on Jul 9, 2007
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

package net.rrm.ehour.ui.page.user.report.criteria;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.panel.sidepanel.SidePanel;
import net.rrm.ehour.ui.renderers.ProjectChoiceRender;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
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
		
		ListMultipleChoice projectDropDown;
		projectDropDown = new ListMultipleChoice("project", 
											new PropertyModel(reportCriteria, "userCriteria.projects"), 
											reportCriteria.getAvailableCriteria().getProjects(),
											new ProjectChoiceRender());
		form.add(projectDropDown);
		
		addDatePickers(form, reportCriteria);
		
		this.add(new FeedbackPanel("feedback"));

		this.add(form);
	}

	/**
	 * Add date pickers
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
