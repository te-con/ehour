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

package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.common.component.CommonJavascript;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.component.TooltipLabel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.FormHighlighter;
import net.rrm.ehour.ui.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.timesheet.dto.ProjectTotalModel;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.MinimumValidator;

import java.util.Calendar;
import java.util.List;

/**
 * Representation of a timesheet row
 **/

public class TimesheetRowList extends ListView<TimesheetRow>
{
	private static final long serialVersionUID = -6905022018110510887L;

	private	EhourConfig config;
	private final GrandTotal	grandTotals;
	private	Form<?> form;

    private	static final Logger LOGGER = Logger.getLogger(TimesheetRowList.class);

	public TimesheetRowList(String id, final List<TimesheetRow> model, GrandTotal grandTotals, Form<?> form)
	{
		super(id, model);
		setReuseItems(true);
		this.grandTotals = grandTotals;
		this.form = form;
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
	 */
	@Override
	protected void populateItem(ListItem<TimesheetRow> item)
	{
		final TimesheetRow row = item.getModelObject();

		item.add(createBookWholeWeekLink(row));
		item.add(createProjectLabel(row));
		item.add(new Label("projectCode", row.getProjectAssignment().getProject().getProjectCode()));
		item.add(createStatusLabel(item));
		addInputCells(item, row);
		item.add(createTotalHoursLabel(row));
	}

	private Label createTotalHoursLabel(final TimesheetRow row)
	{
		Label totalHours = new Label("total", new ProjectTotalModel(row));
		totalHours.setOutputMarkupId(true);
		return totalHours;
	}

	private void addInputCells(ListItem<TimesheetRow> item, final TimesheetRow row)
	{
		Calendar dateIterator = (Calendar)row.getFirstDayOfWeekDate().clone();

		DateRange range = new DateRange(row.getProjectAssignment().getDateStart(),
										row.getProjectAssignment().getDateEnd());
		
		// now add every cell
		for (int i = 1;
			 i <= 7;
			 i++, dateIterator.add(Calendar.DAY_OF_YEAR, 1))
		{
			String id = "day" + i;
			
			if (DateUtil.isDateWithinRange(dateIterator, range))
			{
				createTimesheetEntryItems(id, row, dateIterator.get(Calendar.DAY_OF_WEEK) - 1, item);
			}
			else
			{
				createEmptyTimesheetEntry(id, item);
			}
		}
	}

	private AjaxLink<Void> createBookWholeWeekLink(final TimesheetRow row)
	{
		AjaxLink<Void> projectLink = new AjaxLink<Void>("bookWholeWeek")
		{
			private static final long serialVersionUID = -663239917205218384L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				row.bookRemainingHoursOnRow();
				target.addComponent(form);
			}
		};
		
		ContextImage img = new ContextImage("bookImg", new Model<String>("img/check_all_off.png"));
		CommonJavascript.addMouseOver(img, this, getContextRoot() + "img/check_all_on.png", getContextRoot() + "img/check_all_off.png", "bwh");
		projectLink.add(img);
		
		return projectLink;
	}
	
	private String getContextRoot()
	{
		return getRequest().getRelativePathPrefixToContextRoot();
	}


	private TooltipLabel createProjectLabel(final TimesheetRow row)
	{
		return new TooltipLabel("project", row.getProjectAssignment().getProject().getName(), row.getProjectAssignment().getProject().getDescription());
    }

    private Label createStatusLabel(ListItem<TimesheetRow> item)
	{
		Label label = new Label("status", new PropertyModel<String>(item.getModel(), "status")) {
            @Override
            public boolean isVisible() {
                return StringUtils.isNotBlank(getDefaultModelObjectAsString());
            }
        };

		label.setEscapeModelStrings(false);
		label.setOutputMarkupId(true);
        label.setOutputMarkupPlaceholderTag(true);
		return label;
	}
	
	private void createEmptyTimesheetEntry(String id, ListItem<TimesheetRow> item)
	{
		Fragment fragment = new Fragment(id, "dayInputHidden", this);
		
		item.add(fragment);
	}	
	
	/**
	 * Get validated text field
	 * @param id
	 * @param row
	 * @param index
	 * @return
	 */
	private void createTimesheetEntryItems(String id, TimesheetRow row, final int index, ListItem<TimesheetRow> item)
	{
		Fragment fragment = new Fragment(id, "dayInput", this);
		
		item.add(fragment);
		
		fragment.add(createValidatedTextField(row, index));
		
		createTimesheetEntryComment(row, index, fragment);
	}
	
	/**
	 * Create a validating text field for row[index]
	 * @param id
	 * @param row
	 * @param index
	 * @return
	 */
	private TimesheetTextField createValidatedTextField(TimesheetRow row, final int index)
	{
		final TimesheetTextField	dayInput;
		PropertyModel<Float>		cellModel;
		
		cellModel = new PropertyModel<Float>(row, "timesheetCells[" + index + "].timesheetEntry.hours");
		
		// make sure it's added to the grandtotal
		grandTotals.addValue(index, cellModel);
		
		// list it on the page
		dayInput = new TimesheetTextField("day", cellModel, 1);
		dayInput.add(new MinimumValidator<Float>(0f));
		dayInput.setOutputMarkupId(true);
		
		// make sure values are checked
		AjaxFormComponentUpdatingBehavior behavior = new AjaxFormComponentUpdatingBehavior("onblur")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
                LOGGER.trace(target.getLastFocusedElementId() + " onblur");
				// update the project total
				target.addComponent(dayInput.getParent().getParent().get("total"));
				
				// update the grand total & day total
				target.addComponent(((MarkupContainer)dayInput.findParent(Form.class)
												.get("blueFrame"))
												.get("grandTotal"));
				
				
				target.addComponent(((MarkupContainer)dayInput.findParent(Form.class)
												.get("blueFrame"))
												.get("day" + grandTotals.getOrderForIndex(index) + "Total"));
				
				form.visitFormComponents(new FormHighlighter(target));
			}		
			
			@Override
			protected void onError(final AjaxRequestTarget target, RuntimeException e)
			{
                LOGGER.debug(target.getLastFocusedElementId() + " onblur error!", e);
				form.visitFormComponents(new FormHighlighter(target));
			}
		};

		dayInput.add(behavior);

		return dayInput;
	}
	
	/**
	 * Create comment box for timesheet entry
	 * @param id
	 * @param row
	 * @param index
	 */
	@SuppressWarnings("serial")
	private void createTimesheetEntryComment(final TimesheetRow row, final int index, WebMarkupContainer parent)
	{
		final ModalWindow modalWindow;
		final AjaxLink<Void> commentLink;
		final PropertyModel<String> commentModel = new PropertyModel<String>(row, "timesheetCells[" + index + "].timesheetEntry.comment");
		
		modalWindow = new ModalWindow("dayWin");
		modalWindow.setResizable(false);
		modalWindow.setInitialWidth(400);
		modalWindow.setInitialHeight(225);
		
		modalWindow.setTitle(new StringResourceModel("timesheet.dayCommentsTitle", this, null));
		modalWindow.setContent(new TimesheetEntryCommentPanel(modalWindow.getContentId(),
																		commentModel, row, index, modalWindow));
		
		commentLink = new AjaxLink<Void>("dayLink")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.show(target);
			}
		};		
		
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			public void onClose(AjaxRequestTarget target)
			{
				setCommentLinkClass(commentModel, commentLink);
				
				target.addComponent(commentLink);
			}
		});
		
		parent.add(modalWindow);
		
		commentLink.setOutputMarkupId(true);
		commentLink.add(CommonModifiers.tabIndexModifier(255));
		
		ContextImage img;
		if (StringUtils.isBlank(commentModel.getObject()))
		{
			img = new ContextImage("commentLinkImg", new Model<String>("img/comment/comment_blue_off.gif"));
			CommonJavascript.addMouseOver(img, this, getContextRoot() + "img/comment/comment_blue_on.gif", getContextRoot() + "img/comment/comment_blue_off.gif", "comment");
		}
		else
		{
			img = new ContextImage("commentLinkImg", new Model<String>("img/comment/comment_blue_on.gif"));
		}
		commentLink.add(img);
		
		parent.add(commentLink);
	}
	
	/**
	 * Set comment link css class
	 * @param commentModel
	 * @param commentLink
	 */
	private void setCommentLinkClass(IModel<String> commentModel, AjaxLink<Void> commentLink)
	{
		commentLink.add(new SimpleAttributeModifier("class"
				, StringUtils.isBlank(commentModel.getObject()) ? "timesheetEntryComment"
				: "timesheetEntryCommented"));
	}	
	
	/**
	 * Comments panel for timesheet entries
	 * @author Thies
	 *
	 */
	class TimesheetEntryCommentPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		public TimesheetEntryCommentPanel(String id, final IModel<String> model, TimesheetRow row, int index, final ModalWindow window)
		{
			super(id);

			Calendar thisDate = (Calendar)row.getFirstDayOfWeekDate().clone();
			// Use the render order, not the index order, when calculating the date
			thisDate.add(Calendar.DAY_OF_YEAR, grandTotals.getOrderForIndex(index)-1);

			final String previousModel = model.getObject();
			add(new Label("dayComments",
					new StringResourceModel("timesheet.dayComments",
												this,
												null,
												new Object[]{row.getProjectAssignment().getFullName(),
															 new DateModel(thisDate, config, DateModel.DATESTYLE_DAYONLY_LONG)})));
			
			final KeepAliveTextArea textArea = new KeepAliveTextArea("comment", model);
			textArea.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					// simple hack to get around IE's prob with nested forms in a modalwindow
				}
			});
            textArea.setOutputMarkupId(true);
			
			add(textArea);
			
			AjaxLink<Void> submitButton = new AjaxLink<Void>("submit")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					window.close(target);
				}
			};
			add(submitButton);

			AbstractLink cancelButton = new AjaxLink<Void>("cancel")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					model.setObject(previousModel);
					window.close(target);
				}
			};
			add(cancelButton);
		}
	}
}