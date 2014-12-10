package net.rrm.ehour.ui.timesheet.panel.renderer;

import com.google.common.collect.Lists;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SectionRenderFactoryCollectionTest {
    @Test
    public void should_order_on_render_prio() {
        SectionRenderFactory high = new SectionRenderFactory() {

            @Override
            public RenderPriority getPriority() {
                return RenderPriority.HIGH;
            }

            @Override
            public Panel renderForId(String id, IModel<TimesheetContainer> timesheetContainer) {
                return null;
            }
        };

        SectionRenderFactory low = new SectionRenderFactory() {

            @Override
            public RenderPriority getPriority() {
                return RenderPriority.LOW;
            }

            @Override
            public Panel renderForId(String id, IModel<TimesheetContainer> timesheetContainer) {
                return null;
            }
        };

        SectionRenderFactoryCollection collection = new SectionRenderFactoryCollection(Lists.newArrayList(low, high));

        assertEquals(high, collection.getRenderFactories().get(0));
    }

}