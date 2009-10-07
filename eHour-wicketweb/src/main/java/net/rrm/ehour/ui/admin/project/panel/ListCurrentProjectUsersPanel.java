package net.rrm.ehour.ui.admin.project.panel;

import java.util.Currency;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.converter.RoleConverter;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
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
				
				Label dateStart = new Label("dateStart", new DateModel(assignment.getDateStart(), config, DateModel.DATESTYLE_FULL_SHORT));
				dateStart.setEscapeModelStrings(false);
				item.add(dateStart);
				
				Label dateEnd = new Label("dateEnd", new DateModel(assignment.getDateEnd(), config, DateModel.DATESTYLE_FULL_SHORT));
				dateEnd.setEscapeModelStrings(false);
				item.add(dateEnd);
				
				item.add(addRole(assignment));
				
				item.add(new Label("currency",  Currency.getInstance(config.getCurrency()).getSymbol(config.getCurrency())));
				item.add(new Label("rate", new FloatModel(assignment.getHourlyRate(), config)));
				
				item.add(new CheckBox("active", new PropertyModel(assignment, "active")));
			}
		};
		
		add(assignmentList);
	}
	
	@SuppressWarnings("serial")
	private AjaxEditableLabel addRole(ProjectAssignment assignment)
	{
		
		AjaxEditableLabel label = new AjaxEditableMultiLineLabel("role", new PropertyModel(assignment, "role"))
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
