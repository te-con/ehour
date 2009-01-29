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
import org.apache.wicket.Component;
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
import org.wicketstuff.minis.mootipbehavior.MootipBehaviour;

/**
 * Panel showing overview
 */

public class ProjectOverviewPanel extends Panel implements IHeaderContributor
{
	private static final long serialVersionUID = -5935376941518756941L;
	private transient EhourWebSession session;
	
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
		Label projectLabel = new Label("projectLabel", new ResourceModel("overview.project"));
		setProjectLabelWidth(projectLabel);
		container.add(projectLabel);
		
		Label customerLabel = new Label("customerLabel", new ResourceModel("overview.customer"));
		setCustomerLabelWidth(customerLabel);
		container.add(customerLabel);
		
		Label rateLabel = new Label("rateLabel",  new ResourceModel("overview.rate"));
		setRateWidthOrHide(rateLabel);
		container.add(rateLabel);
		
		container.add(new Label("bookedHoursLabel",  new ResourceModel("overview.hours")));

		Label turnOverLabel = new Label("turnoverLabel",  new ResourceModel("overview.turnover"));
		setTurnoverWidthOrHide(turnOverLabel);
		container.add(turnOverLabel);
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
				setProjectLabelWidth(projectLabel);
				ResourceModel toolTipTitleRM = new ResourceModel("overview.projectDescription");
				projectLabel.add(new MootipBehaviour( (String)toolTipTitleRM.getObject(), projectStatus.getProjectAssignment().getProject().getDescription() ));
				item.add(projectLabel);
				
				Label customerLabel = new Label("customerName", projectStatus.getProjectAssignment().getProject().getCustomer().getName()); 
				setCustomerLabelWidth(customerLabel);
				item.add(customerLabel);

				item.add(new Label("projectCode", projectStatus.getProjectAssignment().getProject().getProjectCode()));
				
				Label rateLabel = new Label("rate", new CurrencyModel(projectStatus.getProjectAssignment().getHourlyRate(), session.getEhourConfig()));
				rateLabel.setEscapeModelStrings(false);
				setRateWidthOrHide(rateLabel);
				item.add(rateLabel);

				Label hoursLabel = new Label("monthHours", new FloatModel(projectStatus.getHours(), session.getEhourConfig()));
				item.add(hoursLabel);

				Label turnOverLabel = new Label("turnover", new CurrencyModel(projectStatus.getTurnOver(), session.getEhourConfig()));
				setTurnoverWidthOrHide(turnOverLabel);
				item.add(turnOverLabel);
				
				// SummaryRow 
				item.add(createProjectSummaryRow(item, projectStatus));
			}
		};
		
		container.add(view);
	}
	
	@SuppressWarnings("serial")
	private Component createProjectSummaryRow(final ListItem item, UserProjectStatus projectStatus)
	{
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

		Label totalBookedLabel = new Label("overview.totalbooked", new StringResourceModel("overview.totalbooked", 
																	this,  null,
																	new Object[]{new FloatModel(projectStatus.getTotalBookedHours(), session.getEhourConfig())}));
		cont.add(totalBookedLabel);

		Label remainingLabel = new Label("overview.remainingfixed", new StringResourceModel("overview.remainingfixed", 
																	this,  null,
																	new Object[]{new FloatModel(projectStatus.getFixedHoursRemaining(), session.getEhourConfig())})); 
		remainingLabel.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());
		cont.add(remainingLabel);

		Label remainingFlexLabel = new Label("overview.remainingflex", new StringResourceModel("overview.remainingflex",
																	this,  null,
																	new Object[]{new FloatModel(projectStatus.getFlexHoursRemaining(), session.getEhourConfig())}));

		// only shown for flex allotted types
		remainingFlexLabel.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isFlexAllottedType());
		cont.add(remainingFlexLabel);

		summaryRow.add(cont);	
		
		return summaryRow;
	}
	
	
	private void setCustomerLabelWidth(Label label)
	{
		if (!isTurnOverVisible())
		{
			label.add(new SimpleAttributeModifier("style", "width: 30%;"));
		}		
	}

	private void setProjectCodeLabelWidth(Label label)
	{
		
	}

	private void setProjectLabelWidth(Label label)
	{
		if (!isTurnOverVisible())
		{
			label.add(new SimpleAttributeModifier("style", "width: 35%;"));
		}		
	}

	private void setRateWidthOrHide(Label label)
	{
		label.setVisible(isTurnOverVisible());
	}
	
	private void setTurnoverWidthOrHide(Label label)
	{
		label.setVisible(isTurnOverVisible());
	}

	private void setHoursWidth(Label label)
	{

		
	}

	private boolean isTurnOverVisible()
	{
		return getEhourSession().getEhourConfig().isShowTurnover();
	}
	
	private EhourWebSession getEhourSession()
	{
		if (session == null)
		{
			session = (EhourWebSession)getSession();
		}
		return session;
	}
	
	/**
	 * Make sure the images are preloaded
	 */
	public void renderHead(IHeaderResponse response)
	{
		 response.renderOnLoadJavascript("init();");		
	}
}
