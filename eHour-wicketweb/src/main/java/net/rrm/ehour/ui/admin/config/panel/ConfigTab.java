package net.rrm.ehour.ui.admin.config.panel;

/**
 * Holds the various configuration title and tab indices
 *
 * @author Thies
 */
public enum ConfigTab {
    MISC("admin.config.misc.title", 0),
    LOCALE("admin.config.locale.title", 1),
    SKIN("admin.config.skin.title", 2),
    AUDIT("admin.config.audit.title", 3);

    private String titleResourceId;
    private int tabIndex;

    ConfigTab(String titleResourceId, int tabIndex) {
        this.titleResourceId = titleResourceId;
        this.tabIndex = tabIndex;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public String getTitleResourceId() {
        return titleResourceId;
    }
}
