package net.rrm.ehour.ui.admin.project.panel.editusers;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.converter.RoleConverter;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.MinimumValidator;

import javax.management.relation.Role;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class ListCurrentProjectUsersPanel extends AbstractBasePanel<Void>
{
	private static final long serialVersionUID = 7558151841882107334L;
	private EhourConfig config;

	@SuppressWarnings("serial")
	public ListCurrentProjectUsersPanel(String id, List<ProjectAssignment> assignments)
	{
		super(id);

		add(CSSPackageResource.getHeaderContribution(ListCurrentProjectUsersPanel.class, "ListCurrentProjectUsersPanel.css"));
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
		
		ListView<ProjectAssignment> assignmentList = new ListView<ProjectAssignment>("assignmentList", assignments)
		{
			@Override
			protected void populateItem(ListItem<ProjectAssignment> item)
			{
				ProjectAssignment assignment = item.getModelObject();
				
				item.add(new Label("user", assignment.getUser().getFullName()));
				
				item.add(addDate("dateStart", new PropertyModel<Date>(assignment, "dateStart")));
				item.add(addDate("dateEnd", new PropertyModel<Date>(assignment, "dateEnd")));
				
				item.add(addRole("role", new PropertyModel<Role>(assignment, "role")));
				
				item.add(new Label("currency", Currency.getInstance(config.getCurrency()).getSymbol(config.getCurrency())));
				item.add(addRate("rate" , new Model<Float>(assignment.getHourlyRate())));
				
				item.add(new AjaxCheckBox("active", new PropertyModel<Boolean>(assignment, "active"))
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
	private AjaxEditableLabel<Float> addRate(String id, IModel<Float> model)
	{
		return new AjaxEditableLabel<Float>("rate", model)
		{
			@Override
			protected FormComponent<Float> newEditor(MarkupContainer parent, String componentId, IModel<Float> model)
			{
				FormComponent<Float> editor = super.newEditor(parent, componentId, model);

				editor.setType(Float.class);
				editor.add(new ValidatingFormComponentAjaxBehavior());
				editor.add(new MinimumValidator<Float>(0f));
				editor.add(new SimpleAttributeModifier("style", "width: 50px"));

				return editor;
			}
		};
	}


	@SuppressWarnings("serial")
	private AjaxEditableLabel<Date> addDate(String id, IModel<Date> model)
	{
		return new AjaxEditableLabel<Date>(id, model)
		{
			@Override
			protected WebComponent newLabel(MarkupContainer parent, String componentId, IModel<Date> model)
			{
				Label dateStart = new Label(componentId, new DateModel(model, config, DateModel.DATESTYLE_FULL_SHORT));
				dateStart.setEscapeModelStrings(false);
				
				dateStart.add(new LabelAjaxBehavior(getLabelAjaxEvent()));
				
				return dateStart;
			}
		};
	}
	
	@SuppressWarnings("serial")
	private AjaxEditableLabel<Role> addRole(String id, IModel<Role> model)
	{
		return new AjaxEditableLabel<Role>(id, model)
		{
			@Override
			public IConverter getConverter(Class<?> type)
			{
				return new RoleConverter();
			}
		};
	}
}
