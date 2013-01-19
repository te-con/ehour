package net.rrm.ehour.ui.common.formguard;

public final class GuardDirtyFormUtil {
    public static final String PRECONDITION = "var k=true;if (window.wicket && wicket.guardform) { k=wicket.guardform.promptForDirtyForm(); };reurn (k);";

    private GuardDirtyFormUtil() {
    }
}
