package net.rrm.ehour.ui.admin.project.panel;

import java.util.Currency;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.converter.RoleConverter;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

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
				
				item.add(new CheckBox("active", new PropertyModel(assignment, "active")));
			}
		};
		
		add(assignmentList);
	}

	@SuppressWarnings("serial")
	private AjaxEditableLabel addRate(String id, IModel model)
	{
		AjaxEditableLabel label = new AjaxEditableLabel(id, model)
		{

		};

		return label;
	}
	
	
	@SuppressWarnings("serial")
	private AjaxEditableLabel addDate(String id, IModel model)
	{
		AjaxEditableLabel label = new AjaxEditableLabel(id, model)
		{
			@Override
			protected Component newLabel(MarkupContainer parent, String componentId, IModel model)
			{
				Label dateStart = new Label(componentId, new DateModel((Date)model.getObject(), config, DateModel.DATESTYLE_FULL_SHORT));
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
