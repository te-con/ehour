package net.rrm.ehour.ui.audit.model;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.audit.service.dto.AuditReportRequest;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AuditReportModel extends LoadableDetachableModel
{
	private static final long serialVersionUID = 1743532708139752797L;

	@SpringBean
	private AuditService auditService;
	private AuditReportRequest request;
	
	public AuditReportModel(AuditReportRequest request)
	{
		this.request = request;
	}
	
	
	@Override
	protected Object load()
	{
		InjectorHolder.getInjector().inject(this);

		return auditService.getAudit(request);
	}

}
