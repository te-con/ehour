package net.rrm.ehour.ui.audit.model;

import java.util.Iterator;
import java.util.List;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * 
 * @author thies
 *
 */
public class AuditReportDataProvider extends SortableDataProvider
{
	private static final long serialVersionUID = 8795552030531153903L;

	@SpringBean
	private AuditService auditService;
	
	private transient Integer		auditsCount;
	private transient List<Audit>	audits;
	private transient int			auditCacheFirst;
	private transient int			auditCacheCount;
	private AuditReportRequest		request;
	
	public AuditReportDataProvider(AuditReportRequest request)
	{
		this.request = request;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	public Iterator<Audit> iterator(int first, int count)
	{
		if (audits == null ||
				auditCacheFirst != first &&
				auditCacheCount != count)
		{
			CommonWebUtil.springInjection(this);
			
			request.setOffset(first);
			request.setMax(count);
			
			audits = auditService.getAudit(request);
		}
		
		return audits.iterator();
	}


	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	public int size()
	{
		if (auditsCount == null)
		{
			CommonWebUtil.springInjection(this);
			
			auditsCount = auditService.getAuditCount(request).intValue();
		}
		
		return auditsCount;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel model(Object object)
	{
		return new CompoundPropertyModel(object);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	public void detach()
	{
		auditsCount = null;
		audits = null;
	}
}
