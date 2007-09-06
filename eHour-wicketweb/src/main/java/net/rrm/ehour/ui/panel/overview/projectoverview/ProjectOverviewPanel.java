/**
 * Created on May 22, 2007
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

package net.rrm.ehour.ui.panel.overview.projectoverview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.CurrencyModel;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

/**
 * Panel showing overview
 * TODO move to table layout
 */

public class ProjectOverviewPanel extends Panel implements IHeaderContributor
{
	private static final long serialVersionUID = -5935376941518756941L;
	
	/**
	 * 
	 * @param id
	 * @param projectStatus
	 */
	public ProjectOverviewPanel(String id, Collection<UserProjectStatus> projectStatusSet)
	{
		super(id);
		
		this.setOutputMarkupId(true);
		
		EhourWebSession session = (EhourWebSession)getSession();
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("greyBorder", new ResourceModel("projectoverview.aggregatedPerMonth"));

		addTotals(greyBorder, projectStatusSet, session.getEhourConfig());
		addColumnLabels(greyBorder, session.getEhourConfig());
		addTableData(greyBorder, projectStatusSet, session.getEhourConfig());

		addJavascript(greyBorder);
		add(new StyleSheetReference("aggregateStyle", new CompressedResourceReference(ProjectOverviewPanel.class, "style/aggregate.css")));

		add(greyBorder);
	}
	
	/**
	 * Add javascript with replaced images
	 * @param container
	 */
	private void addJavascript(WebMarkupContainer container)
	{
		CharSequence iconUpOn = getRequest().getRelativePathPrefixToContextRoot() + "img/icon_up_on.gif";
		CharSequence iconUpOff = getRequest().getRelativePathPrefixToContextRoot() + "img/icon_up_off.gif";
		CharSequence iconDownOn = getRequest().getRelativePathPrefixToContextRoot() + "img/icon_down_on.gif";
		CharSequence iconDownOff = getRequest().getRelativePathPrefixToContextRoot() + "img/icon_down_off.gif";
		
		PackagedTextTemplate js = new PackagedTextTemplate(ProjectOverviewPanel.class, "js/aggregate.js");

		Map<String, CharSequence> map = new HashMap<String, CharSequence>();
		map.put("iconUpOn", iconUpOn);
		map.put("iconUpOff", iconUpOff);
		map.put("iconDownOn", iconDownOn);
		map.put("iconDownOff", iconDownOff);
		
		add(TextTemplateHeaderContributor.forJavaScript(js, new Model((Serializable)map)));
	}
	
	/**
	 * Add grand totals to the mix
	 * @param container
	 * @param projectStatusSet
	 * @param config
	 */
	private void addTotals(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet, EhourConfig config)
	{
		float	totalHours = 0;
		float	totalTurnover = 0;
		Label	label;

		for (UserProjectStatus status : projectStatusSet)
		{
			totalHours += (status.getHours() != null) ?  status.getHours().floatValue() : 0;
			totalTurnover += (status.getTurnOver() != null) ? status.getTurnOver().floatValue() : 0;
		}
		
		container.add(new Label("grandTotalHours", new FloatModel(totalHours, config)));
		
		label = new Label("grandTotalTurnover", new CurrencyModel(totalTurnover, config));
		label.setVisible(config.isShowTurnover());
		container.add(label);
	}
	
	/**
	 * Add column labels
	 * @param container
	 * @param config
	 */
	private void addColumnLabels(WebMarkupContainer container, EhourConfig config)
	{
		Label	label;
		
		container.add(new Label("projectLabel", "Project"));
		container.add(new Label("projectCodeLabel", "Project code"));
		container.add(new Label("customerLabel", "Customer"));
		
		label = new Label("rateLabel", "Rate");
		label.setVisible(config.isShowTurnover());
		container.add(label);
		
		container.add(new Label("bookedHoursLabel", "Hours"));

		label = new Label("turnoverLabel", "Turnover");
		label.setVisible(config.isShowTurnover());
		container.add(label);
	}
	
	/**
	 * Add table data
	 * @param container
	 * @param projectStatusSet
	 * @param config
	 */
	@SuppressWarnings("serial")
	private void addTableData(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet, EhourConfig config)
	{
		ListView view = new ListView("projectStatus", new ArrayList<UserProjectStatus>(projectStatusSet))
		{
			private static final long serialVersionUID = -2544424604230082804L;
			EhourWebSession session = (EhourWebSession)getSession();
			
			public void populateItem(final ListItem item)
			{
				UserProjectStatus projectStatus = (UserProjectStatus) item.getModelObject();
				// add id to AggreagteRow
				item.add(new AttributeModifier("id", true, new AbstractReadOnlyModel()
				{
					public Object getObject()
					{
						return "" + item.getIndex();
					}
				}));				
				
				// set relative URL to image and set id
				ContextImage img = new ContextImage("foldImg", new Model("img/icon_down_off.gif"));
				img.add(new AttributeModifier("id", true, new AbstractReadOnlyModel()
				{
					public Object getObject()
					{
						return "foldImg_" + item.getIndex();
					}
				}));				
				
				item.add(img);
				
				item.add(new Label("projectName", projectStatus.getProjectAssignment().getProject().getName()));
				item.add(new Label("projectCode", projectStatus.getProjectAssignment().getProject().getProjectCode()));
				
				Label label = new Label("customerName", projectStatus.getProjectAssignment().getProject().getCustomer().getName());
				item.add(label);
				
				label = new Label("rate", new CurrencyModel(projectStatus.getProjectAssignment().getHourlyRate(), session.getEhourConfig()));
				label.setVisible(session.getEhourConfig().isShowTurnover());
				item.add(label);

				label = new Label("monthHours", new FloatModel(projectStatus.getHours(), session.getEhourConfig()));
				item.add(label);

				label = new Label("turnover", new CurrencyModel(projectStatus.getTurnOver(), session.getEhourConfig()));
				label.setVisible(session.getEhourConfig().isShowTurnover());
				item.add(label);
				
				// SummaryRow placeholder
				WebMarkupContainer summaryRow = new WebMarkupContainer("summaryRow");
				summaryRow.add(new AttributeModifier("id", true, new AbstractReadOnlyModel()
				{
					public Object getObject()
					{
						return "summaryRow_" + item.getIndex();
					}
				}));
				
				summaryRow.add(new SimpleAttributeModifier("style", "display: none")); 

				label = new Label("validStart", new DateModel(projectStatus.getProjectAssignment().getDateStart(),
						session.getEhourConfig()));
				label.setEscapeModelStrings(false);
				summaryRow.add(label);

				label = new Label("validEnd", new DateModel(projectStatus.getProjectAssignment().getDateEnd(),
						session.getEhourConfig()));
				label.setEscapeModelStrings(false);
				summaryRow.add(label);
				
				WebMarkupContainer cont = new WebMarkupContainer("remainingHoursLabel");
				// only shown for allotted types
				cont.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());

				label = new Label("totalHours", new FloatModel(projectStatus.getTotalBookedHours(), session.getEhourConfig()));
				cont.add(label);

				label = new Label("remainingHours", new FloatModel(projectStatus.getHoursRemaining(), session.getEhourConfig()));
				label.setVersioned(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());
				cont.add(label);
				summaryRow.add(cont);

				item.add(summaryRow);
			}
		};
		
		container.add(view);
	}

	/**
	 * Make sure the images are preloaded
	 */
	public void renderHead(IHeaderResponse response)
	{
		 response.renderOnLoadJavascript("init();");		
	}
}
