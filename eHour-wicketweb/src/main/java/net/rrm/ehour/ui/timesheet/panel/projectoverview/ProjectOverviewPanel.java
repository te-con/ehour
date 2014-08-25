/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.timesheet.panel.projectoverview;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.component.CommonJavascript;
import net.rrm.ehour.ui.common.component.CurrencyLabel;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.component.TooltipLabel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.common.util.WebGeo;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Panel showing overview
 */

public class ProjectOverviewPanel extends AbstractBasePanel<Void> {
	private static final long serialVersionUID = -5935376941518756941L;
	private static final String ID_GREY_BORDER = "greyBorder";
	private static final String ID_TABLE_DATA = "projectStatus";
	private static final String ID_SUMMARY_ROW = "summaryRow";
	private static final String ID_FOLD_LINK = "foldLink";
	private static final String ID_FOLD_IMG = "foldImg";

	private String tableDatePath;

	public ProjectOverviewPanel(String id, Calendar overviewFor, Collection<UserProjectStatus> projectStatusSet) {
		super(id);

		this.setOutputMarkupId(true);

		// this should be easier..
		Label label = new Label("title", new StringResourceModel("projectoverview.aggregatedPerMonth", this, null,
				new Object[] { new DateModel(overviewFor, getConfig(), DateModel.DATESTYLE_MONTHONLY) }));

		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder(ID_GREY_BORDER, label, WebGeo.W_CONTENT_MEDIUM);

		addTotals(greyBorder, projectStatusSet, getConfig());
		addColumnLabels(greyBorder, getConfig());

		tableDatePath = ID_GREY_BORDER;
		addTableData(greyBorder, projectStatusSet, getConfig());

		add(greyBorder);
	}

	/**
	 * Add grand totals to the mix
	 * 
	 * @param container
	 * @param projectStatusSet
	 * @param config
	 */
	private void addTotals(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet, EhourConfig config) {
		float totalHours = 0;
		float totalTurnover = 0;
		Label label;

		if (projectStatusSet != null) {
			for (UserProjectStatus status : projectStatusSet) {
				totalHours += (status.getHours() != null) ? status.getHours().floatValue() : 0;
				totalTurnover += (status.getTurnOver() != null) ? status.getTurnOver().floatValue() : 0;
			}
		}

		container.add(new Label("grandTotalHours", new Model<Float>(totalHours)));

		label = new CurrencyLabel("grandTotalTurnover", new Model<Float>(totalTurnover));

		label.setVisible(config.isShowTurnover());
		label.setEscapeModelStrings(false);
		container.add(label);

		label = HtmlUtil.getNbspLabel("grandRate");
		label.setVisible(config.isShowTurnover());
		label.setEscapeModelStrings(false);
		container.add(label);

		Label projectLabel = HtmlUtil.getNbspLabel("grandProject");
		Label customerLabel = HtmlUtil.getNbspLabel("grandCustomer");

		if (!config.isShowTurnover()) {
			customerLabel.add(new SimpleAttributeModifier("style", "width: 30%"));
			projectLabel.add(new SimpleAttributeModifier("style", "width: 35%"));
		}
		container.add(customerLabel);
		container.add(projectLabel);
	}

	/**
	 * Add column labels
	 * 
	 * @param container
	 * @param config
	 */
	private void addColumnLabels(WebMarkupContainer container, EhourConfig config) {
		Label projectLabel = new Label("projectLabel", new ResourceModel("overview.project"));
		setProjectLabelWidth(projectLabel);
		container.add(projectLabel);

		Label customerLabel = new Label("customerLabel", new ResourceModel("overview.customer"));
		setCustomerLabelWidth(customerLabel);
		container.add(customerLabel);

		Label rateLabel = new Label("rateLabel", new ResourceModel("overview.rate"));
		setRateWidthOrHide(rateLabel);
		container.add(rateLabel);

		container.add(new Label("bookedHoursLabel", new ResourceModel("overview.hours")));

		Label turnOverLabel = new Label("turnoverLabel", new ResourceModel("overview.turnover"));
		setTurnoverWidthOrHide(turnOverLabel);
		container.add(turnOverLabel);
	}

	/**
	 * Add table data
	 * 
	 * @param container
	 * @param projectStatusSet
	 * @param config
	 */
	@SuppressWarnings("serial")
	private void addTableData(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet, EhourConfig config) {
		List<UserProjectStatus> statusses;

		if (projectStatusSet == null) {
			statusses = new ArrayList<UserProjectStatus>();
		} else {
			statusses = new ArrayList<UserProjectStatus>(projectStatusSet);
		}

		// table data should reflect the path to the listView
		this.tableDatePath += ":" + ID_TABLE_DATA;
		ListView<UserProjectStatus> view = new ListView<UserProjectStatus>(ID_TABLE_DATA, statusses) {
			public void populateItem(final ListItem<UserProjectStatus> item) {
				UserProjectStatus projectStatus = item.getModelObject();

				item.add(createFoldLink(projectStatus, item.getId()));

				TooltipLabel projectLabel = new TooltipLabel("projectName", projectStatus.getActivity().getProject().getName(),
						projectStatus.getActivity().getProject().getDescription());
				setProjectLabelWidth(projectLabel);
				item.add(projectLabel);

				Label customerLabel = new Label("customerName", projectStatus.getActivity().getProject().getCustomer().getName());
				setCustomerLabelWidth(customerLabel);
				item.add(customerLabel);

				item.add(new Label("projectCode", projectStatus.getActivity().getProject().getProjectCode()));

				Label rateLabel = new Label("rate", "--");
				// TODO-NK No Hourly rate for Activity
				// if (projectStatus.getActivity().getHourlyRate() != null)
				// {
				// rateLabel = new CurrencyLabel("rate",
				// projectStatus.getProjectAssignment().getHourlyRate());
				// } else
				// {
				// rateLabel = new Label("rate", "--");
				// }

				rateLabel.setEscapeModelStrings(false);

				setRateWidthOrHide(rateLabel);

				item.add(rateLabel);

				Label hoursLabel = new Label("monthHours", new Model<Float>(projectStatus.getHours().floatValue()));
				item.add(hoursLabel);

				Label turnOverLabel;

				if (projectStatus.getActivity().getProject().isBillable())

				{
					turnOverLabel = new CurrencyLabel("turnover", projectStatus.getTurnOver().floatValue());
				} else

				{
					turnOverLabel = new Label("turnover", "--");
				}

				setTurnoverWidthOrHide(turnOverLabel);

				item.add(turnOverLabel);

				// SummaryRow
				item.add(new

				PlaceholderPanel(ID_SUMMARY_ROW)

				);
			}
		};

		container.add(view);
	}

	/**
	 * Create fold link (also contains the creation of the summary row)
	 * 
	 * @param projectStatus
	 * @return
	 */
	@SuppressWarnings("serial")
	private Component createFoldLink(final UserProjectStatus projectStatus, final String domId) {
		// set relative URL to image and set id
		ContextImage img = createFoldImage(false);

		AjaxLink<String> foldLink = new AjaxLink<String>(ID_FOLD_LINK) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Component original = ProjectOverviewPanel.this.get(getSummaryRowPath(domId));
				Component originalImage = ProjectOverviewPanel.this.get(getFoldImagePath(domId));
				Component replacement;
				Component replacementImage;

				if (original instanceof PlaceholderPanel) {
					replacement = createProjectSummaryRow(projectStatus);
					replacementImage = createFoldImage(true);
				} else {
					replacement = new PlaceholderPanel(ID_SUMMARY_ROW);
					replacementImage = createFoldImage(false);
				}

				original.replaceWith(replacement);
				originalImage.replaceWith(replacementImage);
				target.addComponent(replacement);
				target.addComponent(replacementImage);

				target.appendJavascript("initImagePreload();");
			}
		};

		foldLink.add(img);
		return foldLink;
	}

	private ContextImage createFoldImage(boolean up) {
		String upStr = "img/icon_" + (up ? "up_" : "down_");

		ContextImage img = new ContextImage(ID_FOLD_IMG, new Model<String>(upStr + "off.gif"));
		img.setOutputMarkupId(true);
		CommonJavascript.addMouseOver(img, this, getContextRoot() + upStr + "on.gif", getContextRoot() + upStr + "off.gif", "upDown");

		return img;
	}

	private String getContextRoot() {
		return getRequest().getRelativePathPrefixToContextRoot();
	}

	private String getFoldImagePath(String domId) {
		StringBuilder builder = new StringBuilder(tableDatePath);
		builder.append(":");
		builder.append(domId);
		builder.append(":");
		builder.append(ID_FOLD_LINK);
		builder.append(":");
		builder.append(ID_FOLD_IMG);

		return builder.toString();
	}

	private String getSummaryRowPath(String domId) {
		StringBuilder builder = new StringBuilder(tableDatePath);
		builder.append(":");
		builder.append(domId);
		builder.append(":");
		builder.append(ID_SUMMARY_ROW);

		return builder.toString();
	}

	/**
	 * Create project summary row
	 * 
	 * @param projectStatus
	 * @return
	 */
	private Component createProjectSummaryRow(UserProjectStatus projectStatus) {
		// SummaryRow placeholder
		Fragment summaryRow = new Fragment(ID_SUMMARY_ROW, "summaryRowFragment", ProjectOverviewPanel.this);
		summaryRow.setOutputMarkupId(true);
		// valid from until label
		Label validityLabel = new Label("overview.validity", new StringResourceModel("overview.validity", this, null, new Object[] {
				new DateModel(projectStatus.getActivity().getDateStart(), getConfig()),
				new DateModel(projectStatus.getActivity().getDateEnd(), getConfig()) }));
		validityLabel.setEscapeModelStrings(false);
		summaryRow.add(validityLabel);

		WebMarkupContainer cont = new WebMarkupContainer("remainingHoursLabel");
		// only shown for allotted types
		//TODO-NK Activity are always of Alloted Types
		cont.setVisible(true);
//		cont.setVisible(projectStatus.getActivity().getAssignmentType().isAllottedType());

		Label totalBookedLabel = new Label("overview.totalbooked", new StringResourceModel("overview.totalbooked", this, null,
				new Object[] { new Model<Float>(projectStatus.getTotalBookedHours().floatValue()) }));
		cont.add(totalBookedLabel);

		Label remainingLabel = new Label("overview.remainingfixed", new StringResourceModel("overview.remainingfixed", this, null,
				new Object[] { new Model<Float>(projectStatus.getFixedHoursRemaining()) }));
		//TODO-NK Activity are always of Alloted Types
		remainingLabel.setVisible(true);
		cont.add(remainingLabel);

		Label remainingFlexLabel = new Label("overview.remainingflex", new StringResourceModel("overview.remainingflex", this, null,
				new Object[] { new Model<Float>(projectStatus.getFlexHoursRemaining()) }));

		// only shown for flex allotted types
		//TODO-NK Activity are always of Alloted Types
		remainingFlexLabel.setVisible(true);
//		remainingFlexLabel.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isFlexAllottedType());
		cont.add(remainingFlexLabel);

		summaryRow.add(cont);

		return summaryRow;
	}

	private void setCustomerLabelWidth(Label label) {
		if (!isTurnOverVisible()) {
			label.add(new SimpleAttributeModifier("style", "width: 30%;"));
		}
	}

	private void setProjectLabelWidth(Component label) {
		if (!isTurnOverVisible()) {
			label.add(new SimpleAttributeModifier("style", "width: 35%;"));
		}
	}

	private void setRateWidthOrHide(Label label) {
		label.setVisible(isTurnOverVisible());
	}

	private void setTurnoverWidthOrHide(Label label) {
		label.setVisible(isTurnOverVisible());
	}

	private boolean isTurnOverVisible() {
		return getConfig().isShowTurnover();
	}
}
