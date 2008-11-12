package net.rrm.ehour.ui.audit.model;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * 
 * @author thies
 *
 */
public class AuditReportCriteriaModel extends LoadableDetachableModel
{
	private static final long serialVersionUID = 7613019543226503699L;

	private AuditReportRequest request;
	
	public AuditReportCriteriaModel(AuditReportRequest request)
	{
		this.request = request;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected Object load()
	{
		InjectorHolder.getInjector().inject(this);
		return request;
	}

	/**
	 * @return the request
	 */
	public AuditReportRequest getRequest()
	{
		return request;
	}


	/**
	 * @param request the request to set
	 */
	public void setRequest(AuditReportRequest request)
	{
		this.request = request;
	}

}
