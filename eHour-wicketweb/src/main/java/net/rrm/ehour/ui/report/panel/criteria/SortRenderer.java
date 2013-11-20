package net.rrm.ehour.ui.report.panel.criteria;

import net.rrm.ehour.report.criteria.Sort;
import net.rrm.ehour.ui.common.renderers.LocalizedResourceRenderer;

public class SortRenderer extends LocalizedResourceRenderer<Sort> {

    @Override
    protected String getResourceKey(Sort o) {
        switch (o) {
            case CODE:
                return "report.sortOn.code";
            case NAME:
            default:
                return "report.sortOn.name";
        }
    }
}