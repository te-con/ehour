package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.ui.audit.model.AuditReportCriteriaModel;
import net.rrm.ehour.ui.audit.model.AuditReportDataProvider;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;

public class AuditReportDataPanel extends AbstractAjaxPanel
{
	private static final long serialVersionUID = -2380789244030608920L;

	public AuditReportDataPanel(String id, AuditReportCriteriaModel model)
	{
		super(id, model);

		setOutputMarkupId(true);
		
		addComponents(model);
	}
	
	/**
	 * Add components to the page
	 */
	private void addComponents(AuditReportCriteriaModel model)
	{
		Border greyBorder = new GreyRoundedBorder("border", 450);
		add(greyBorder);
		
		greyBorder.add(getPagingDataView(model));
		
	}

	private WebMarkupContainer getPagingDataView(AuditReportCriteriaModel model)
	{
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);


		DataView dataView =  getDataview(model);
		dataContainer.add(dataView);
		
		AjaxPagingNavigator pager = new AjaxPagingNavigator("pager", dataView) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(dataContainer);
			}
		};
		dataContainer.add(pager);	
		
		return dataContainer;
	}
	
	/**
	 * Add the dataview table
	 * @return
	 */
	private DataView getDataview(AuditReportCriteriaModel model)
	{
		// TODO pass propertymodel to dataprovider
		AuditReportRequest request = model.getRequest();
		
		final EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		DataView dataView = new DataView("data", new AuditReportDataProvider(request))
		{
			private static final long serialVersionUID = -2331713940323233655L;

			@Override
			protected void populateItem(Item item)
			{
				Audit audit = (Audit)item.getModelObject();
				
				item.add(new Label("userName", audit.getUserName()));
				item.add(new Label("date", new DateModel(audit.getDate(), config)));
				item.add(new Label("actionType", audit.getAuditActionType().getValue()));
				item.add(new Label("action", audit.getAction()));
			}
		};
		
		dataView.setItemsPerPage(15);
		
		return dataView;
	}
	
}
