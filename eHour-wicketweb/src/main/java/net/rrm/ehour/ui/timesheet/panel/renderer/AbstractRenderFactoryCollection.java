package net.rrm.ehour.ui.timesheet.panel.renderer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AbstractRenderFactoryCollection<T extends PrioritizedRenderFactory> {
    private List<? extends T> renderFactories;

    AbstractRenderFactoryCollection() {
    }

    public AbstractRenderFactoryCollection(List<? extends T> renderFactories) {
        sortOnPriority(renderFactories);

        this.renderFactories = renderFactories;
    }

    private void sortOnPriority(List<? extends T> renderFactories) {
        Collections.sort(renderFactories, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1.getPriority() == o2.getPriority()) {
                    return 0;
                } else if (o1.getPriority() == RenderPriority.HIGH) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }


    public List<? extends T> getRenderFactories() {
        return renderFactories;
    }
}
