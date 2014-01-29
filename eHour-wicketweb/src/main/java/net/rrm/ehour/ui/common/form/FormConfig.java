package net.rrm.ehour.ui.common.form;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.Form;

import java.io.Serializable;

public class FormConfig implements Serializable {

    private Form<?> form;
    private boolean includeDelete = false;
    private AjaxEventType submitEventType;
    private AjaxEventType deleteEventType;
    private AjaxEventType errorEventType;
    private EhourConfig config;

    private MarkupContainer submitTarget;

    public FormConfig() {
    }

    public FormConfig(Form<?> form) {
        this.form = form;
    }

    public MarkupContainer getSubmitTarget() {
        return submitTarget;
    }

    public FormConfig withSubmitTarget(MarkupContainer submitTarget) {
        this.submitTarget = submitTarget;
        return this;
    }

    public static FormConfig forForm(Form<?> form) {
        return new FormConfig(form);
    }

    public FormConfig withDelete(boolean withDelete) {
        includeDelete = withDelete;
        return this;
    }


    public FormConfig withSubmitEventType(AjaxEventType submitEventType) {
        this.submitEventType = submitEventType;
        return this;
    }

    public FormConfig withDeleteEventType(AjaxEventType deleteEventType) {
        this.deleteEventType = deleteEventType;
        return this;
    }

    public FormConfig withErrorEventType(AjaxEventType errorEventType) {
        this.errorEventType = errorEventType;
        return this;
    }

    public Form<?> getForm() {
        return form;
    }

    public boolean isIncludeDelete() {
        return includeDelete;
    }

    public AjaxEventType getSubmitEventType() {
        return submitEventType;
    }

    public AjaxEventType getDeleteEventType() {
        return deleteEventType;
    }

    public AjaxEventType getErrorEventType() {
        return errorEventType;
    }

    public EhourConfig getConfig() {
        return config == null ? EhourWebSession.getEhourConfig() : config;
    }
}
