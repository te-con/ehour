package net.rrm.ehour.ui.common.formguard;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/9/11 - 3:23 PM
 */
public final class GuardDirtyFormUtil
{
    private GuardDirtyFormUtil()
    {
    }

    public static String getEventHandler(CharSequence originalEventHandler) {
        return "var k=true;if (window.wicket && wicket.guardform) { k=wicket.guardform.promptForDirtyForm(); };if (k) { " + originalEventHandler + " };";
    }
}
