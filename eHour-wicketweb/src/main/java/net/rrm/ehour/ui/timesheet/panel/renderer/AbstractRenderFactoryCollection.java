package net.rrm.ehour.ui.timesheet.panel.renderer;

import java.util.List;

public class AbstractRenderFactoryCollection<T> {
    private List<? extends T> renderFactories;

    AbstractRenderFactoryCollection() {
    }

    public AbstractRenderFactoryCollection(List<? extends T> renderFactories) {
        this.renderFactories = renderFactories;
    }

    public List<? extends T> getRenderFactories() {
        return renderFactories;
    }
}
