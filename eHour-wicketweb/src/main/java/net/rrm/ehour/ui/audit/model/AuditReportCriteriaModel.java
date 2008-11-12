package net.rrm.ehour.ui.audit.model;

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

	/**
	 * 
	 */
	public AuditReportCriteriaModel()
	{
		InjectorHolder.getInjector().inject(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected Object load()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
