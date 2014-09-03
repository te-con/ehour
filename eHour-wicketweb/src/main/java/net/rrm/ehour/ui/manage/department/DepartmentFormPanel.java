/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.manage.department;

import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * user department form panel
 */

public class DepartmentFormPanel extends AbstractFormSubmittingPanel<DepartmentAdminBackingBean> {
    private static final long serialVersionUID = -6469066920645156569L;

    @SpringBean
    private UserService userService;

    public DepartmentFormPanel(String id, CompoundPropertyModel<DepartmentAdminBackingBean> model) {
        super(id, model);

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.AUTO);
        add(greyBorder);

        setOutputMarkupId(true);

        final Form<DepartmentAdminBackingBean> form = new Form<DepartmentAdminBackingBean>("deptForm", model);

        // name
        RequiredTextField<String> nameField = new RequiredTextField<String>("department.name");
        form.add(nameField);
        nameField.add(StringValidator.maximumLength(64));
        nameField.setLabel(new ResourceModel("admin.dept.name"));
        nameField.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));

        // code
        RequiredTextField<String> codeField = new RequiredTextField<String>("department.code");
        form.add(codeField);
        codeField.add(StringValidator.maximumLength(16));
        codeField.setLabel(new ResourceModel("admin.dept.code"));
        codeField.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));

        // data save label
        form.add(new ServerMessageLabel("serverMessage", "formValidationError"));

        boolean deletable = model.getObject().isDeletable();
        FormConfig formConfig = FormConfig.forForm(form).withDelete(deletable).withSubmitTarget(this)
                .withDeleteEventType(DepartmentAjaxEventType.DEPARTMENT_DELETED)
                .withSubmitEventType(DepartmentAjaxEventType.DEPARTMENT_UPDATED);
        FormUtil.setSubmitActions(formConfig);

        greyBorder.add(form);
    }

    private void persistDepartment(DepartmentAdminBackingBean backingBean) throws ObjectNotUniqueException {
        userService.persistUserDepartment(backingBean.getDepartment());
    }

    private void deleteDepartment(DepartmentAdminBackingBean backingBean) {
        userService.deleteDepartment(backingBean.getDepartment().getDepartmentId());
    }

    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        DepartmentAdminBackingBean departmentBackingBean = (DepartmentAdminBackingBean) backingBean;

        if (type == DepartmentAjaxEventType.DEPARTMENT_UPDATED) {
            persistDepartment(departmentBackingBean);
        } else if (type == DepartmentAjaxEventType.DEPARTMENT_DELETED) {
            deleteDepartment(departmentBackingBean);
        }
        return true;
    }
}
