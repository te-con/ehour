package net.rrm.ehour.ui.common.report.excel;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.poi.ss.usermodel.*;

import java.util.Currency;
import java.util.Locale;

public enum ExcelStyle {
    NORMAL_FONT,
    BOLD_FONT {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
    },
    DATE {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("d-mmm-yy"));
        }
    },
    BOLD_DATE {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            BOLD_FONT.apply(workbook, cellStyle, font);
            DATE.apply(workbook, cellStyle, font);
        }
    },
    DIGIT {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            cellStyle.setDataFormat((short) 2);
        }
    },
    CURRENCY {
        private short dataFormat = -1;

        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            if (dataFormat == -1) {
                init(workbook);
            }

            cellStyle.setDataFormat(dataFormat);
        }

        private void init(Workbook workbook) {
            Locale currencyLocale = EhourWebSession.getEhourConfig().getCurrency();
            Currency currency = Currency.getInstance(currencyLocale);
            String currencySymbol = currency.getSymbol(currencyLocale);

            String format = "$#,##0.00;[Red]($#,##0.00)".replace("$", currencySymbol);

            DataFormat dataFormat = workbook.createDataFormat();
            this.dataFormat = dataFormat.getFormat(format);
        }
    },
    BORDER_SOUTH {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        }
    },
    BORDER_NORTH_THIN {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
        }
    },
    DATE_BORDER_NORTH_THIN {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            BORDER_NORTH_THIN.apply(workbook, cellStyle, font);
            DATE.apply(workbook, cellStyle, font);
        }
    },
    DIGIT_BORDER_NORTH_THIN {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            BORDER_NORTH_THIN.apply(workbook, cellStyle, font);
            DIGIT.apply(workbook, cellStyle, font);
        }
    },
    BOLD_BORDER_SOUTH {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            BORDER_SOUTH.apply(workbook, cellStyle, font);
            DIGIT.apply(workbook, cellStyle, font);
        }
    },
    BORDER_NORTH {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
        }
    },
    BOLD_BORDER_NORTH {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            BORDER_NORTH.apply(workbook, cellStyle, font);
            BOLD_FONT.apply(workbook, cellStyle, font);
        }
    },
    DIGIT_BOLD_BORDER_NORTH {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            BORDER_NORTH.apply(workbook, cellStyle, font);
            BOLD_FONT.apply(workbook, cellStyle, font);
            DIGIT.apply(workbook, cellStyle, font);
        }
    },
    HEADER {
        @Override
        public void apply(Workbook workbook, CellStyle cellStyle, Font font) {
            BOLD_FONT.apply(workbook, cellStyle, font);
            BORDER_SOUTH.apply(workbook, cellStyle, font);

            cellStyle.setFillForegroundColor(IndexedColors.BLUE.index);
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
    };

    public void apply(Workbook workbook, CellStyle cellStyle, Font font) {

    }
}
