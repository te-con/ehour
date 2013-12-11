package net.rrm.ehour.ui.common.formguard;

import org.apache.wicket.ajax.attributes.AjaxCallListener;

public class GuardFormAjaxCallListener extends AjaxCallListener {
    public GuardFormAjaxCallListener() {
        onPrecondition("if (typeof window.ajaxGuard == 'function') return ajaxGuard(); else return true;");
        onSuccess("if (typeof window.guardForm == 'function') guardForm();");
    }
}
