package net.rrm.ehour.ui.manage.user;

import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.util.WebGeo;
import org.apache.wicket.markup.html.panel.Panel;

public class UserAddPlaceholderPanel extends Panel {
    public UserAddPlaceholderPanel(String id) {
        super(id);

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.AUTO);
        add(greyBorder);

    }
}
