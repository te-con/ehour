package net.rrm.ehour.ui.common.report.excel;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Currency;
import java.util.Locale;

public class CurrencyCellStyle implements CellStyle {

    private String format;

    public CurrencyCellStyle() {
        Locale currencyLocale = EhourWebSession.getSession().getEhourConfig().getCurrency();
        Currency currency = Currency.getInstance(currencyLocale);
        String currencySymbol = currency.getSymbol(currencyLocale);

        format = "$#,##0.00;[Red]($#,##0.00)".replace("$", currencySymbol);
    }

    @Override
    public CellStyleElement.CellStylePopulator getCellStylePopulator() {
        return new CellStyleElement.CellStylePopulator() {
            @Override
            public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook) {
                HSSFDataFormat dataFormat = workbook.createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat(format));
            }
        };
    }
}
