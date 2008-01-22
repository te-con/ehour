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

package net.rrm.ehour.ui.panel.timesheet;

import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.component.CommonModifiers;
import net.rrm.ehour.ui.component.KeepAliveTextArea;
import net.rrm.ehour.ui.component.ModalWindowFix;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.panel.timesheet.dto.ProjectTotalModel;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonWebUtil;
import net.rrm.ehour.ui.validator.DoubleRangeWithNullValidator;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Representation of a timesheet row
 **/

public class TimesheetRowList extends ListView
{
	private static final long serialVersionUID = -6905022018110510887L;

	private int 			counter;
	private final boolean 	hidden;
	private	EhourConfig		config;
	private final GrandTotal	grandTotals;
	private	Form			form;
	
	/**
	 * 
	 * @param id
	 * @param model
	 * @param hidden
	 */
	public TimesheetRowList(String id, final List<TimesheetRow> model, boolean hidden, GrandTotal grandTotals, Form form)
	{
		super(id, model);
		setReuseItems(true);
		counter = 1;
		this.hidden = hidden;
		this.grandTotals = grandTotals;
		this.form = form;
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
	 */
	@Override
	protected void populateItem(ListItem item)
	{
		final TimesheetRow row = (TimesheetRow) item.getModelObject();

		// add id to row
		item.add(new AttributeModifier("id", true, new AbstractReadOnlyModel()
		{
			private static final long serialVersionUID = 1L;

			public Object getObject()
			{
				return "pw" + row.getProjectAssignment().getProject().getCustomer().getCustomerId().toString() + counter++;
			}
		}));

		// add project + link to book whole week on project
		// TODO use icon instead of project list
		AjaxLink projectLink = new AjaxLink("bookWholeWeek")
		{
			private static final long serialVersionUID = -663239917205218384L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				row.bookRemainingHoursOnRow();
				target.addComponent(form);
			}
		};
		
		projectLink.add(new Label("project", row.getProjectAssignment().getProject().getName()));
		item.add(projectLink);
		item.add(new Label("projectCode", row.getProjectAssignment().getProject().getProjectCode()));
		
		for (int i = 0; 
			 i < CommonWebUtil.weekDays.length; // I take it that this is 7 ;)
			 i++)
		{
			createTimesheetEntryItems(CommonWebUtil.weekDays[i], row, i, item);
		}

		Label	totalHours = new Label("total", new FloatModel(new ProjectTotalModel(row), config));
		totalHours.setOutputMarkupId(true);
		item.add(totalHours);

		if (hidden)
		{
			item.add(new AttributeModifier("style", true, new AbstractReadOnlyModel()
			{
				private static final long serialVersionUID = -6996376634435316294L;

				public Object getObject()
				{
					return "display: none";
				}
			}));
		}
	}
	
	/**
	 * Get validated text field
	 * @param id
	 * @param row
	 * @param index
	 * @return
	 */
	private void createTimesheetEntryItems(String id, TimesheetRow row, final int index, ListItem item)
	{
		item.add(createValidatedTextField(id, row, index));
		
		createTimesheetEntryComment(id, row, index, item);
	}
	
	/**
	 * Create a validating text field for row[index]
	 * @param id
	 * @param row
	 * @param index
	 * @return
	 */
	private TextField createValidatedTextField(String id, TimesheetRow row, final int index)
	{
		final TimesheetTextField	dayInput;
		PropertyModel				cellModel;
		
		cellModel = new PropertyModel(row, "timesheetCells[" + index + "].timesheetEntry.hours");
		
		// make sure it's added to the grandtotal
		grandTotals.addValue(index, cellModel);
		
		// list it on the page
		dayInput = new TimesheetTextField(id, new FloatModel(cellModel, config, null), Float.class, 1);
		dayInput.add(new DoubleRangeWithNullValidator(0, 24));
		dayInput.setOutputMarkupId(true);
		
		// make sure values are checked
		AjaxFormValidatingBehavior behavior = new AjaxFormValidatingBehavior(form, "onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				// update the project total
				target.addComponent(dayInput.getParent().get("total"));
				
				// update the grand total & day total
				target.addComponent(((MarkupContainer)dayInput.findParent(Form.class)
												.get("blueFrame"))
												.get("grandTotal"));
				target.addComponent(((MarkupContainer)dayInput.findParent(Form.class)
												.get("blueFrame"))
												.get(CommonWebUtil.weekDays[index] + "Total"));
				
				form.visitFormComponents(new FormHighlighter(target));
			}		
			
			@Override
			protected void onError(AjaxRequestTarget target)
			{
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
	private void createTimesheetEntryComment(String id, final TimesheetRow row, final int index, ListItem item)
	{
		final ModalWindow modalWindow;
		final AjaxLink commentLink;
		final PropertyModel commentModel = new PropertyModel(row, "timesheetCells[" + index + "].timesheetEntry.comment");
		
		modalWindow = new ModalWindowFix(id + "Win");
		modalWindow.setResizable(false);
		modalWindow.setInitialWidth(400);
		modalWindow.setInitialHeight(225);
		
		modalWindow.setTitle(new StringResourceModel("timesheet.dayCommentsTitle", this, null));
		modalWindow.setContent(new TimesheetEntryCommentPanel(modalWindow.getContentId(),
																		commentModel, row, index, modalWindow));
		
		commentLink = new AjaxLink(id + "Link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.show(target);
			}
		};		
		
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			public void onClose(AjaxRequestTarget target)
			{
				setCommentLinkClass(commentModel, commentLink);
				
				target.addComponent(commentLink);
			}
		});
		
		item.add(modalWindow);
		
		commentLink.setOutputMarkupId(true);
		commentLink.add(CommonModifiers.tabIndexModifier(255));
		
		setCommentLinkClass(commentModel, commentLink);
		
		item.add(commentLink);
	}
	
	/**
	 * Set comment link css class
	 * @param commentModel
	 * @param commentLink
	 */
	private void setCommentLinkClass(IModel commentModel, AjaxLink commentLink)
	{
		commentLink.add(new SimpleAttributeModifier("class"
				, StringUtils.isBlank((String)commentModel.getObject()) ? "timesheetEntryComment"
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

		public TimesheetEntryCommentPanel(String id, final IModel model, TimesheetRow row, int index, final ModalWindow window)
		{
			super(id);

			Calendar thisDate = (Calendar)row.getSundayDate().clone();
			thisDate.add(Calendar.DAY_OF_YEAR, index);
			
			Form form = new Form("commentForm");

			form.add(new Label("dayComments",
					new StringResourceModel("timesheet.dayComments",
												this,
												null,
												new Object[]{row.getProjectAssignment().getFullName(),
															 new DateModel(thisDate, config, DateModel.DATESTYLE_DAYONLY_LONG)})));
			
			final TextArea textArea = new KeepAliveTextArea("comment", model);
			form.add(textArea);
			
			AbstractLink submitButton = new AjaxSubmitLink("submit", form)
			{
				private static final long serialVersionUID = 4796005602570042916L;
	
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form)
				{
					window.close(target);
				}
			};
			
			add(submitButton);

			AbstractLink cancelButton = new AjaxLink("cancel")
			{
				private static final long serialVersionUID = 4796005602570042916L;
	
				@Override
				public void onClick(AjaxRequestTarget target)
				{
					window.close(target);
				}
			};
			add(cancelButton);
			
			add(form);
		}
	}
}