/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.admin.config.panel;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.ui.common.component.ImageResource;
import net.rrm.ehour.ui.common.form.ImageUploadForm;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.report.trend.PrintReport;
import net.rrm.ehour.ui.timesheet.export.ExportCriteriaParameter;
import net.rrm.ehour.ui.timesheet.export.excel.ExportReportDummyCreater;
import net.rrm.ehour.ui.timesheet.export.excel.ExportReportExcel;
import net.rrm.ehour.value.ImageLogo;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.util.value.ValueMap;

/**
 * Created on Apr 22, 2009, 4:14:39 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class SkinConfigPanel extends AbstractConfigPanel
{
	private static final long serialVersionUID = -1274285277029402888L;

	private Image previewImage;
	
	public SkinConfigPanel(String id, IModel model)
	{
		super(id, model, WebGeo.W_CONTENT_MEDIUM);
	}

	@SuppressWarnings("serial")
	@Override
	protected void addFormComponents(Form form)
	{
		previewImage = createPreviewImage();
		form.add(previewImage);
		
        form.add(new UploadProgressBar("progress", form));

        form.add(new AjaxButton("excelPreview")
        {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				createDummyExcelExport();
			}
        });
	}
	
	
	
	@SuppressWarnings("serial")
	@Override
	protected Form createForm(String id, IModel model)
	{
		ImageUploadForm uploadForm = new ImageUploadForm(id, model)
		{
			@Override
			protected void uploadImage(ImageLogo logo)
			{
				if (!getConfig().isInDemoMode())
				{
					getConfigService().persistExcelLogo(logo);
					updatePreviewImage();
				}
				else
				{
					replaceFeedbackMessage(new ResourceModel("demoMode"));
				}
			}

			@Override
			protected void uploadImageError()
			{
				replaceFeedbackMessage(new ResourceModel("general.image.invalid"));
			}
		};

		return uploadForm;
	}
	
	private void updatePreviewImage()
	{
		Image replacement = createPreviewImage();
		previewImage.replaceWith(replacement);
		previewImage = replacement;
	}
	
	private Image createPreviewImage()
	{
		final ImageLogo excelLogo = getConfigService().getExcelLogo();
		
		int width = excelLogo.getWidth();
		double divideBy = width / 350d;
		double height = (double)excelLogo.getHeight() / divideBy;
		
		Image img = new Image("excelImage");
		img.setOutputMarkupId(true);
		img.add(new SimpleAttributeModifier("width", "350"));
		img.add(new SimpleAttributeModifier("height", Integer.toString((int) height)));
		
		img.setImageResource(new ImageResource(excelLogo));
		return img;
	}
	
	private void createDummyExcelExport()
	{
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.getCustomParameters().put(ExportCriteriaParameter.INCL_SIGN_OFF.name(), Boolean.TRUE);
		userCriteria.setReportRange(ExportReportDummyCreater.getDateRangeForCurrentMonth());
		ReportCriteria criteria = new ReportCriteria(userCriteria);
		PrintReport report = new PrintReport(criteria);
		
		EhourWebSession.getSession().getObjectCache().addObjectToCache(report);
		
		final String reportId = report.getCacheId();
		
		ResourceReference excelResource = new ResourceReference(ExportReportExcel.getId());
		ValueMap params = new ValueMap();
		params.add("reportId", reportId);
		
		excelResource.bind(getApplication());
		CharSequence url = getRequestCycle().urlFor(excelResource, params);
		
		getRequestCycle().setRequestTarget(new RedirectRequestTarget(url.toString()));		
	}
}
