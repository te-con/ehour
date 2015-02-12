package net.rrm.ehour.ui.admin.backup.backup;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.IResourceStream;

public abstract class AbstractDownloadBehavior extends AbstractAjaxBehavior {

    private final IModel<Boolean> model;

    public AbstractDownloadBehavior(IModel<Boolean> model) {
        this.model = model;
    }

    public void initiate(AjaxRequestTarget target) {
        String url = getCallbackUrl().toString();

        url = url + (url.contains("?") ? "&" : "?");
        url = url + "antiCache=" + System.currentTimeMillis();

        target.appendJavaScript("setTimeout(\"window.location.href='" + url + "'\", 100);");
    }

    public void onRequest() {
        ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(getResourceStream(), getFileName()) {
            @Override
            public void respond(IRequestCycle requestCycle) {
                super.respond(requestCycle);
                model.setObject(Boolean.FALSE);
            }
        };
        handler.setContentDisposition(ContentDisposition.ATTACHMENT);
        getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
    }


    protected String getFileName() {
        return null;
    }

    protected abstract IResourceStream getResourceStream();
}