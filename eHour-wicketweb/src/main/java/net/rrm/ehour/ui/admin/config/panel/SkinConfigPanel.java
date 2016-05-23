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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.persistence.value.ImageLogo;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.admin.config.MainConfigBackingBean;
import net.rrm.ehour.ui.common.form.ImageUploadForm;
import net.rrm.ehour.ui.common.report.excel.ExcelRequestHandler;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.timesheet.export.TimesheetExcelExport;
import net.rrm.ehour.ui.timesheet.export.TimesheetExportParameter;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.DynamicImageResource;

import java.util.*;

/**
 * Created on Apr 22, 2009, 4:14:39 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class SkinConfigPanel extends AbstractConfigPanel {
    private static final long serialVersionUID = -1274285277029402888L;

    private Image previewImage;

    public SkinConfigPanel(String id, IModel<MainConfigBackingBean> model) {
        super(id, model, WebGeo.W_CONTENT_MEDIUM);
    }

    @SuppressWarnings("serial")
    @Override
    protected void addFormComponents(Form<?> form) {
        previewImage = createPreviewImage();
        form.add(previewImage);

        form.add(new UploadProgressBar("progress", form));

        form.add(new Link<Void>("excelPreview") {
            @Override
            public void onClick() {
                createDummyExcelExport();
            }
        });
    }


    @SuppressWarnings("serial")
    @Override
    protected Form<Void> createForm(String id, IModel<MainConfigBackingBean> model) {
        return new ImageUploadForm(id) {
            @Override
            protected void uploadImage(ImageLogo logo) {
                if (!getConfig().isInDemoMode()) {
                    getConfigService().persistExcelLogo(logo);
                    updatePreviewImage();
                } else {
                    replaceFeedbackMessage(new ResourceModel("demoMode"));
                }
            }

            @Override
            protected void uploadImageError() {
                replaceFeedbackMessage(new ResourceModel("general.image.invalid"));
            }
        };
    }

    private void updatePreviewImage() {
        Image replacement = createPreviewImage();
        previewImage.replaceWith(replacement);
        previewImage = replacement;
    }

    private Image createPreviewImage() {
        final ImageLogo excelLogo = getConfigService().getExcelLogo();

        int width = excelLogo.getWidth();
        double divideBy = width / 350d;
        double height = (double) excelLogo.getHeight() / divideBy;

        Image img = new Image("excelImage", "img");
        img.setOutputMarkupId(true);
        img.add(AttributeModifier.replace("width", "350"));
        img.add(AttributeModifier.replace("height", Integer.toString((int) height)));

        img.setImageResource(new DynamicImageResource() {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return excelLogo.getImageData();
            }
        });
        return img;
    }

    private void createDummyExcelExport() {
        final UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.getCustomParameters().put(TimesheetExportParameter.INCL_SIGN_OFF.name(), Boolean.TRUE);
        userSelectedCriteria.setReportRange(TimesheetExportDummyDataGenerator.getDateRangeForCurrentMonth());
        final ReportCriteria criteria = new ReportCriteria(userSelectedCriteria);

        final TimesheetExcelExport timesheetExcelExport = new TimesheetExcelExport(criteria);

        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ExcelRequestHandler(timesheetExcelExport.getFilenameWihoutSuffix(), timesheetExcelExport));
    }

    public static class TimesheetExportDummyDataGenerator {
        public static List<FlatReportElement> createMonthData(EhourConfig config) {
            List<FlatReportElement> elements = new ArrayList<>();

            DateRange range = getDateRangeForCurrentMonth();

            List<Date> month = DateUtil.createDateSequence(range, config);

            for (Date date : month) {
                if (Math.random() >= 0.2) {
                    elements.add(createElement(date));
                }
            }
            return elements;
        }

        public static DateRange getDateRangeForCurrentMonth() {
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.NOVEMBER);

            return DateUtil.getDateRangeForMonth(cal);
        }

        private static FlatReportElement createElement(Date date) {
            FlatReportElement element = new FlatReportElement();
            element.setCustomerCode("TE1");
            element.setCustomerName("TEST #1");
            element.setProjectName("Project #1");
            element.setProjectCode("PRJ");
            element.setDayDate(date);
            element.setTotalHours(Math.random() * 8);

            return element;
        }
    }

}
