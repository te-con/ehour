package net.rrm.ehour.ui.report.panel.detail;

import net.rrm.ehour.ui.common.report.AbstractExcelReport;
import net.rrm.ehour.ui.common.report.ReportConfig;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class DetailedReportExcel  extends AbstractExcelReport
{
	private static final long serialVersionUID = 7211392869328367507L;
	
	public DetailedReportExcel()
	{
		super(ReportConfig.DETAILED_REPORT);
	}
	
	@Override
	protected IModel getExcelReportName()
	{
		return new ResourceModel("report.title.detailed");
	}

	@Override
	protected IModel getHeaderReportName()
	{
		return new ResourceModel("report.title.detailed");
	}
}
