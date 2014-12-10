package net.rrm.ehour.ui.timesheet.panel.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionRenderFactoryCollection extends AbstractRenderFactoryCollection<SectionRenderFactory> {

    protected SectionRenderFactoryCollection() {
    }

    @Autowired(required = false)
    public SectionRenderFactoryCollection(List<SectionRenderFactory> renderFactories) {
        super(renderFactories);
    }
}
