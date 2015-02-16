package net.rrm.ehour.ui.admin.backup.backup;

import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

public class SingleDownloadLink extends AjaxLink<Boolean> {

    private final AbstractDownloadBehavior downloadBehavior;

    public SingleDownloadLink(String id, IModel<Boolean> model, AbstractDownloadBehavior downloadBehavior) {
        super(id, model);
        this.downloadBehavior = downloadBehavior;

        add(downloadBehavior);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        Boolean busy = getModelObject();

        if (!busy) {
            getModel().setObject(Boolean.TRUE);
            downloadBehavior.initiate(target);
        }
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);

        attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
    }
}
