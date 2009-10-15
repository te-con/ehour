package net.rrm.ehour.ui.admin.project.panel;

import java.util.Currency;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.converter.RoleConverter;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.NumberValidator;

public class ListCurrentProjectUsersPanel extends AbstractBasePanel
{
	private static final long serialVersionUID = 7558151841882107334L;
	private EhourConfig config;

	@SuppressWarnings("serial")
	public ListCurrentProjectUsersPanel(String id, List<ProjectAssignment> assignments)
	{
		super(id);
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
		
		ListView assignmentList = new ListView("assignmentList", assignments)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				ProjectAssignment assignment = (ProjectAssignment)item.getModelObject();
				
				item.add(new Label("user", assignment.getUser().getFullName()));
				
				item.add(addDate("dateStart", new PropertyModel(assignment, "dateStart")));
				item.add(addDate("dateEnd", new PropertyModel(assignment, "dateEnd")));
				
				item.add(addRole("role", new PropertyModel(assignment, "role")));
				
				item.add(new Label("currency", Currency.getInstance(config.getCurrency()).getSymbol(config.getCurrency())));
				item.add(addRate("rate" , new FloatModel(assignment.getHourlyRate(), config)));
				
				item.add(new AjaxCheckBox("active", new PropertyModel(assignment, "active"))
				{
					@Override
					protected void onUpdate(AjaxRequestTarget target)
					{
					}
				});
			}
		};
		
		add(assignmentList);
	}

	@SuppressWarnings("serial")
	private AjaxEditableLabel addRate(String id, IModel model)
	{
		AjaxEditableLabel editableLabel = new AjaxEditableLabel("rate", model)
		{
			@Override
			protected FormComponent newEditor(MarkupContainer parent, String componentId, IModel model)
			{
				FormComponent editor = super.newEditor(parent, componentId, model);

				editor.setType(Float.class);
				editor.add(new ValidatingFormComponentAjaxBehavior());
				editor.add(NumberValidator.minimum(0));
				
				return editor;
			}
		};
		
		return editableLabel;
	}


	@SuppressWarnings("serial")
	private AjaxEditableLabel addDate(String id, IModel model)
	{
		AjaxEditableLabel label = new AjaxEditableLabel(id, model)
		{
			@Override
			protected Component newLabel(MarkupContainer parent, String componentId, IModel model)
			{
				Label dateStart = new Label(componentId, new DateModel(model, config, DateModel.DATESTYLE_FULL_SHORT));
				dateStart.setEscapeModelStrings(false);
				
				dateStart.add(new LabelAjaxBehavior(getLabelAjaxEvent()));
				
				return dateStart;
			}
		};
		
		return label;
	}
	
	@SuppressWarnings("serial")
	private AjaxEditableLabel addRole(String id, IModel model)
	{
		AjaxEditableLabel label = new AjaxEditableLabel(id, model)
		{
			@SuppressWarnings("unchecked")
			@Override
			public IConverter getConverter(Class type)
			{
				return new RoleConverter();
			}
		};

		return label;
	}
}
