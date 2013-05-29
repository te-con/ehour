package net.rrm.ehour.ui.common.report.excel;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.Currency;
import java.util.Locale;

public enum CellStyle {
    NORMAL_FONT {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
        }
    },
    BOLD_FONT {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            HSSFFont font = cellStyle.getFont(workbook);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
        }
    },
    DATE {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            cellStyle.setDataFormat((short) 0xf);
        }
    },
    BOLD_DATE {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            BOLD_FONT.apply(workbook, cellStyle);
            DATE.apply(workbook, cellStyle);
        }
    },
    DIGIT {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            cellStyle.setDataFormat((short) 2);
        }
    },
    CURRENCY {
        private short dataFormat = -1;

        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            if (dataFormat == -1) {
                init(workbook);
            }

            cellStyle.setDataFormat(dataFormat);
        }

        private void init(HSSFWorkbook workbook) {
            Locale currencyLocale = EhourWebSession.getSession().getEhourConfig().getCurrency();
            Currency currency = Currency.getInstance(currencyLocale);
            String currencySymbol = currency.getSymbol(currencyLocale);

            String format = "$#,##0.00;[Red]($#,##0.00)".replace("$", currencySymbol);

            HSSFDataFormat dataFormat = workbook.createDataFormat();
            this.dataFormat = dataFormat.getFormat(format);
        }
    },
    BORDER_SOUTH {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        }
    },
    BORDER_NORTH_THIN {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        }
    },
    DATE_BORDER_NORTH_THIN {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            BORDER_NORTH_THIN.apply(workbook, cellStyle);
            DATE.apply(workbook, cellStyle);
        }
    },
    DIGIT_BORDER_NORTH_THIN {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            BORDER_NORTH_THIN.apply(workbook, cellStyle);
            DIGIT.apply(workbook, cellStyle);
        }
    },
    BOLD_BORDER_SOUTH {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            BORDER_SOUTH.apply(workbook, cellStyle);
            DIGIT.apply(workbook, cellStyle);
        }
    },
    BORDER_NORTH {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
            cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        }
    },
    BOLD_BORDER_NORTH {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            BORDER_NORTH.apply(workbook, cellStyle);
            BOLD_FONT.apply(workbook, cellStyle);
        }
    },
    DIGIT_BOLD_BORDER_NORTH {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            BORDER_NORTH.apply(workbook, cellStyle);
            BOLD_FONT.apply(workbook, cellStyle);
            DIGIT.apply(workbook, cellStyle);
        }
    },
    HEADER {
        @Override
        public void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
            BOLD_FONT.apply(workbook, cellStyle);
            BORDER_SOUTH.apply(workbook, cellStyle);

            cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }
    };

    public abstract void apply(HSSFWorkbook workbook, HSSFCellStyle cellStyle);
}
