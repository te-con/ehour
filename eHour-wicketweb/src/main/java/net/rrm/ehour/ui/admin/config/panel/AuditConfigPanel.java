/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.admin.config.panel;

import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.ui.admin.config.MainConfigBackingBean;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.Arrays;

/**
 * Created on May 5, 2009, 12:36:56 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class AuditConfigPanel extends AbstractConfigPanel {
    private static final long serialVersionUID = -5212420452301193422L;

    public AuditConfigPanel(String id, IModel<MainConfigBackingBean> model) {
        super(id, model);
    }

    @Override
    protected void addFormComponents(Form<?> configForm) {
        configForm.add(new DropDownChoice<>("config.auditType", Arrays.asList(AuditType.values()), new IChoiceRenderer<AuditType>() {
            @Override
            public Object getDisplayValue(AuditType object) {
                return new MessageResourceModel("admin.config.audit." + object.name().toLowerCase(), AuditConfigPanel.this).getObject();
            }

            @Override
            public String getIdValue(AuditType object, int index) {
                return Integer.toString(index);
            }
        }));
    }
}
