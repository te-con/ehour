/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.timesheet.export.excel.part;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.persistence.value.ImageLogo;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.PoiUtil;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.CellStyle;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Mar 25, 2009, 6:37:20 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class ExportReportHeader extends AbstractExportReportPart
{
    @SpringBean(name = "configurationService")
    private ConfigurationService configurationService;


    public ExportReportHeader(int cellMargin, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
    {
        super(cellMargin, sheet, report, workbook);
    }

    /* (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.ui.timesheet.export.excel.part.AbstractExportReportPart#createPart(int)
      */
    @Override
    public int createPart(int rowNumber)
    {
        rowNumber = addLogo(rowNumber);
        rowNumber = addTitleRow(rowNumber);
        rowNumber = addTitleDateRow(rowNumber);
        rowNumber++;

        return rowNumber;
    }


    private int addLogo(int rowNumber)
    {
        ImageLogo excelLogo = getConfigurationService().getExcelLogo();

        byte[] image = excelLogo.getImageData();

        int index = getWorkbook().addPicture(image, PoiUtil.getImageType(excelLogo.getImageType()));

        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 0, (short) 8, 7);

        HSSFPatriarch patriarch = getSheet().createDrawingPatriarch();
        patriarch.createPicture(anchor, index);
        anchor.setAnchorType(0); // 0 = Move and size with Cells, 2 = Move but don't size with cells, 3 = Don't move or size with cells.

        return rowNumber;
    }


    private int addTitleRow(int rowNumber)
    {
        HSSFRow row = getSheet().createRow(rowNumber++);

        CellFactory.createCell(row, getCellMargin(), getExcelReportName(getReport().getReportRange()), getWorkbook(), CellStyle.NORMAL);
        return rowNumber;
    }

    private int addTitleDateRow(int rowNumber)
    {
        HSSFRow row = getSheet().createRow(rowNumber++);

        CellFactory.createCell(row, getCellMargin(), new ResourceModel("excelMonth.date"), getWorkbook(), CellStyle.NORMAL);
        CellFactory.createCell(row, getCellMargin() + 2, CommonWebUtil.formatDate("MMMM yyyy", getReport().getReportRange().getDateStart()), getWorkbook(), CellStyle.NORMAL);

        return rowNumber;
    }

    private IModel<String> getExcelReportName(DateRange dateRange)
    {
        EhourWebSession session = EhourWebSession.getSession();
        EhourConfig config = session.getEhourConfig();

        return new StringResourceModel("excelMonth.reportName",
                null,
                new Object[]{session.getUser().getUser().getFullName(),
                             new DateModel(dateRange.getDateStart(), config, DateModel.DATESTYLE_MONTHONLY)});
    }

    private ConfigurationService getConfigurationService()
    {
        if (configurationService == null)
        {
            CommonWebUtil.springInjection(this);
        }

        return configurationService;
    }
}
