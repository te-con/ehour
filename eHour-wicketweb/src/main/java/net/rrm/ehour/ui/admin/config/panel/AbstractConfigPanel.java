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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.config.service.IPersistConfiguration;
import net.rrm.ehour.ui.admin.config.MainConfigBackingBean;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.decorator.DemoDecorator;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Apr 21, 2009, 3:10:49 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public abstract class AbstractConfigPanel extends AbstractFormSubmittingPanel<MainConfigBackingBean> {
    private static final long serialVersionUID = -3129819024578782528L;
    private static final Logger LOGGER = Logger.getLogger(AbstractConfigPanel.class);

    private WebComponent serverMessage;

    @SpringBean
    private ConfigurationService configService;

    @SpringBean
    private IPersistConfiguration iPersistConfiguration;

    public AbstractConfigPanel(String id, IModel<MainConfigBackingBean> model) {
        this(id, model, WebGeo.W_CONTENT_MEDIUM);
    }

    public AbstractConfigPanel(String id, IModel<MainConfigBackingBean> model, WebGeo width) {
        super(id, model);

        createComponents(model, width);
    }

    private void createComponents(IModel<MainConfigBackingBean> model, WebGeo width) {
        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", width);
        add(greyBorder);

        Form<?> form = createForm("form", model);
        greyBorder.add(form);
        form.setOutputMarkupId(true);

        serverMessage = new WebComponent("serverMessage");
        serverMessage.setOutputMarkupId(true);
        form.add(serverMessage);

        addFormComponents(form);
        addSubmitButton(form);
    }

    protected Form<?> createForm(String id, IModel<MainConfigBackingBean> model) {
        return new Form<>(id, model);
    }

    /**
     * Set ajax submit button
     */
    @SuppressWarnings("serial")
    private void addSubmitButton(Form<?> form) {
        form.add(new AjaxButton("submitButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                IModel<?> msgModel;

                if (!getConfig().isInDemoMode()) {
                    try {
                        persistConfiguration();
                        msgModel = new ResourceModel("general.dataSaved");
                    } catch (Exception t) {
                        LOGGER.error("While saving config", t);
                        msgModel = new ResourceModel("general.saveError");
                    }

                    getEhourWebSession().reloadConfig();

                    replaceFeedbackMessage(msgModel);
                }
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(getConfig().isInDemoMode() ? new DemoDecorator() : new LoadingSpinnerDecorator());
            }

            @Override
            protected void onError(final AjaxRequestTarget target, Form<?> form) {
                target.add(form);
            }
        });
    }

    private void persistConfiguration() {
        iPersistConfiguration.persistAndCleanUp(getConfigStub(), getPanelModelObject().getConvertManagersTo());
    }

    protected void replaceFeedbackMessage(IModel<?> msgModel) {
        Label replacementLabel = new Label("serverMessage", msgModel);
        replacementLabel.setOutputMarkupId(true);
        replacementLabel.add(AttributeModifier.replace("class", "smallTextRed"));
        serverMessage.replaceWith(replacementLabel);
        serverMessage = replacementLabel;

        AjaxRequestTarget target = getRequestCycle().find(AjaxRequestTarget.class);

        if (target != null) {
            target.add(serverMessage);
        }
    }

    private EhourConfig getConfigStub() {
        return getPanelModelObject().getConfig();
    }

    protected abstract void addFormComponents(Form<?> form);

    protected final WebComponent getServerMessage() {
        return serverMessage;
    }

    protected final void setServerMessage(WebComponent serverMessage) {
        this.serverMessage = serverMessage;
    }

    protected ConfigurationService getConfigService() {
        return configService;
    }
}
