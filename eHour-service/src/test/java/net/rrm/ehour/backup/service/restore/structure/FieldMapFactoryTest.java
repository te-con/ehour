package net.rrm.ehour.backup.service.restore.structure;

import com.google.common.collect.Maps;
import net.rrm.ehour.domain.*;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FieldMapFactoryTest {
    private FieldMap fieldDefinitionMap = FieldMapFactory.buildFieldMapForEntity(User.class);

    @Test
    public void should_process_many_to_many_column() throws InstantiationException, IllegalAccessException {

        FieldDefinition fieldDef = fieldDefinitionMap.get("user_to_department");

        User user = UserObjectMother.createUser();
        user.getUserDepartments().clear();

        UserDepartment department = UserDepartmentObjectMother.createUserDepartment();

        Map<Class<?>, Object> embeddables = Maps.newHashMap();
        fieldDef.process(user, embeddables, department);

        assertEquals(1, user.getUserDepartments().size());
    }

    @Test
    public void should_process_one_to_many_column() throws InstantiationException, IllegalAccessException {
        FieldMap fieldDefinitionMap = FieldMapFactory.buildFieldMapForEntity(Project.class);

        FieldDefinition fieldDef = fieldDefinitionMap.get("customer_id");

        Customer customer = CustomerObjectMother.createCustomer();

        Project project = ProjectObjectMother.createProject(1);
        project.setCustomer(null);

        Map<Class<?>, Object> embeddables = Maps.newHashMap();
        fieldDef.process(project, embeddables, customer);

        assertEquals(customer, project.getCustomer());
    }

    @Test
    public void should_process_column_with_basic_value() throws InstantiationException, IllegalAccessException {
        FieldMap fieldDefinitionMap = FieldMapFactory.buildFieldMapForEntity(Project.class);

        FieldDefinition fieldDef = fieldDefinitionMap.get("project_code");

        Project project = ProjectObjectMother.createProject(1);
        project.setProjectCode(null);

        Map<Class<?>, Object> embeddables = Maps.newHashMap();
        fieldDef.process(project, embeddables, "TEC");

        assertEquals("TEC", project.getProjectCode());
    }

    @Test
    public void should_process_column_with_embeddable() throws InstantiationException, IllegalAccessException {
        FieldMap fieldDefinitionMap = FieldMapFactory.buildFieldMapForEntity(TimesheetEntry.class);

        FieldDefinition fieldDef = fieldDefinitionMap.get("assignment_id");

        TimesheetEntry timesheetEntry = TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5f);
        timesheetEntry.setEntryId(null);

        Map<Class<?>, Object> embeddables = Maps.newHashMap();

        ProjectAssignment projectAssignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        fieldDef.process(timesheetEntry, embeddables, projectAssignment);

        assertEquals(projectAssignment, ((TimesheetEntryId) embeddables.get(TimesheetEntryId.class)).getProjectAssignment());
    }
}