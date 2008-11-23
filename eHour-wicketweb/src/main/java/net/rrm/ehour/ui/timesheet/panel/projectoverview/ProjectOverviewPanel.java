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

package net.rrm.ehour.ui.timesheet.panel.projectoverview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.model.CurrencyModel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.common.util.HtmlUtil;

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
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

/**
 * Panel showing overview
 */

public class ProjectOverviewPanel extends Panel implements IHeaderContributor
{
	private static final long serialVersionUID = -5935376941518756941L;
	
	/**
	 * 
	 * @param id
	 * @param projectStatus
	 */
	public ProjectOverviewPanel(String id, Calendar overviewFor, Collection<UserProjectStatus> projectStatusSet)
	{
		super(id);
		
		this.setOutputMarkupId(true);
		
		EhourWebSession session = (EhourWebSession)getSession();

		// this should be easier..
		Label label = new Label("title", new StringResourceModel("projectoverview.aggregatedPerMonth", 
																	this,  null,
																	new Object[]{new DateModel(overviewFor, ((EhourWebSession)getSession()).getEhourConfig(), DateModel.DATESTYLE_MONTHONLY)}));
		
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("greyBorder", label, CommonWebUtil.GREYFRAME_WIDTH); 

		addTotals(greyBorder, projectStatusSet, session.getEhourConfig());
		addColumnLabels(greyBorder, session.getEhourConfig());
		addTableData(greyBorder, projectStatusSet, session.getEhourConfig());

		addJavascript(greyBorder);

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

		if (projectStatusSet != null)
		{
			for (UserProjectStatus status : projectStatusSet)
			{
				totalHours += (status.getHours() != null) ?  status.getHours().floatValue() : 0;
				totalTurnover += (status.getTurnOver() != null) ? status.getTurnOver().floatValue() : 0;
			}
		}
		
		container.add(new Label("grandTotalHours", new FloatModel(totalHours, config)));
		
		label = new Label("grandTotalTurnover", new CurrencyModel(totalTurnover, config));
		label.setVisible(config.isShowTurnover());
		label.setEscapeModelStrings(false);
		container.add(label);
		
		label = HtmlUtil.getNbspLabel("grandRate");
		label.setVisible(config.isShowTurnover());
		label.setEscapeModelStrings(false);
		container.add(label);

		Label projectLabel = HtmlUtil.getNbspLabel("grandProject");
		Label customerLabel = HtmlUtil.getNbspLabel("grandCustomer");
		
		if (!config.isShowTurnover())
		{
			customerLabel.add(new SimpleAttributeModifier("style", "width: 30%"));
			projectLabel.add(new SimpleAttributeModifier("style", "width: 35%"));
		}
		container.add(customerLabel);
		container.add(projectLabel);
	}
	
	/**
	 * Add column labels
	 * @param container
	 * @param config
	 */
	private void addColumnLabels(WebMarkupContainer container, EhourConfig config)
	{
		Label	label;
		
		Label projectLabel = new Label("projectLabel", new ResourceModel("overview.project"));
		Label customerLabel = new Label("customerLabel", new ResourceModel("overview.customer"));
		
		if (!config.isShowTurnover())
		{
			customerLabel.add(new SimpleAttributeModifier("style", "width: 30%;margin-top: 0;"));
			projectLabel.add(new SimpleAttributeModifier("style", "width: 35%;margin-top: 0;"));
		}

		container.add(projectLabel);
		container.add(customerLabel);
		
		label = new Label("rateLabel",  new ResourceModel("overview.rate"));
		label.setVisible(config.isShowTurnover());
		container.add(label);
		
		container.add(new Label("bookedHoursLabel",  new ResourceModel("overview.hours")));

		label = new Label("turnoverLabel",  new ResourceModel("overview.turnover"));
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
		List<UserProjectStatus> stati;
		
		if (projectStatusSet == null)
		{
			stati = new ArrayList<UserProjectStatus>();
		}
		else
		{
			stati = new ArrayList<UserProjectStatus>(projectStatusSet);
		}
			
		
		ListView view = new ListView("projectStatus", stati)
		{
			private static final long serialVersionUID = -2544424604230082804L;
			EhourWebSession session = (EhourWebSession)getSession();
			
			public void populateItem(final ListItem item)
			{
				UserProjectStatus projectStatus = (UserProjectStatus) item.getModelObject();
				// add id to AggregateRow
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
				
				Label projectLabel = new Label("projectName", projectStatus.getProjectAssignment().getProject().getName());
				Label customerLabel = new Label("customerName", projectStatus.getProjectAssignment().getProject().getCustomer().getName()); 
				
				if (!session.getEhourConfig().isShowTurnover())
				{
					customerLabel.add(new SimpleAttributeModifier("style", "width: 30%;"));
					projectLabel.add(new SimpleAttributeModifier("style", "width: 35%;"));
				}
				
				item.add(projectLabel);
				item.add(customerLabel);
				item.add(new Label("projectCode", projectStatus.getProjectAssignment().getProject().getProjectCode()));
				
				
				Label label = new Label("rate", new CurrencyModel(projectStatus.getProjectAssignment().getHourlyRate(), session.getEhourConfig()));
				label.setVisible(session.getEhourConfig().isShowTurnover());
				label.setEscapeModelStrings(false);
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

				// valid from until label
				Label validityLabel = new Label("overview.validity", new StringResourceModel("overview.validity", 
																		this,  null,
																		new Object[]{new DateModel(projectStatus.getProjectAssignment().getDateStart(), session.getEhourConfig()),
																						new DateModel(projectStatus.getProjectAssignment().getDateEnd(), session.getEhourConfig())}));
				validityLabel.setEscapeModelStrings(false);
				summaryRow.add(validityLabel);
				
				WebMarkupContainer cont = new WebMarkupContainer("remainingHoursLabel");
				// only shown for allotted types
				cont.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());

				label = new Label("overview.totalbooked", new StringResourceModel("overview.totalbooked", 
																			this,  null,
																			new Object[]{new FloatModel(projectStatus.getTotalBookedHours(), session.getEhourConfig())}));
				cont.add(label);

				label = new Label("overview.remaining", new StringResourceModel("overview.remaining", 
																			this,  null,
																			new Object[]{new FloatModel(projectStatus.getHoursRemaining(), session.getEhourConfig())})); 
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
