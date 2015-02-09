package net.rrm.ehour.backup.config;

import com.google.common.collect.Lists;
import net.rrm.ehour.backup.service.backup.BackupEntity;
import net.rrm.ehour.backup.service.backup.BackupEntityLocator;
import net.rrm.ehour.backup.service.backup.BackupEntityManyToManyTable;
import net.rrm.ehour.backup.service.backup.BackupEntitySingleTable;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.backup.dao.TimesheetEntryRowProcessor;

import java.util.*;

public class EhourBackupEntityLocator implements BackupEntityLocator {
    private static final BackupEntity userDepartment = new BackupEntitySingleTable(UserDepartment.class, "USER_DEPARTMENT", 0);
    private static final BackupEntity userRole = new BackupEntitySingleTable(UserRole.class, "USER_ROLE", 1);
    private static final BackupEntity users = new BackupEntitySingleTable(User.class, "USERS", "USERLIST", 2);
    private static final BackupEntity customer = new BackupEntitySingleTable(Customer.class, "CUSTOMER", 3);
    private static final BackupEntity project = new BackupEntitySingleTable(Project.class, "PROJECT", 4);
    private static final BackupEntity projectAssignmentType = new BackupEntitySingleTable(ProjectAssignmentType.class, "PROJECT_ASSIGNMENT_TYPE", 5);
    private static final BackupEntity projectAssignment = new BackupEntitySingleTable(ProjectAssignment.class, "PROJECT_ASSIGNMENT", 6);
    private static final BackupEntity timesheetEntry = new BackupEntitySingleTable(ProjectAssignment.class, "TIMESHEET_ENTRY", "TIMESHEET_ENTRIES", new TimesheetEntryRowProcessor(), 7);
    private static final BackupEntity timesheetComment = new BackupEntitySingleTable(TimesheetComment.class, "TIMESHEET_COMMENT", 8);
    private static final BackupEntity userToUserRole = new BackupEntityManyToManyTable("USER_TO_USERROLE", "ROLE", "USER_ID", null, 9);
    private static final BackupEntity userToDepartmentRole = new BackupEntityManyToManyTable("USER_TO_DEPARTMENT", "DEPARTMENT_ID", "USER_ID", null, 10);
    private static final BackupEntity timesheetLock = new BackupEntitySingleTable(TimesheetLock.class, "TIMESHEET_LOCK", 11);

    private static final List<BackupEntity> ENTITIES = Lists.newArrayList(userDepartment, userRole, users, userToUserRole, userToDepartmentRole,
                                                                            customer, project, projectAssignmentType, projectAssignment,
                                                                            timesheetEntry, timesheetComment, timesheetLock);

    private static final List<BackupEntity> REVERSE_ORDER;

    private static Map<Class<?>, BackupEntity> classMap;

    static {
        classMap = new HashMap<>();

        for (BackupEntity entity : ENTITIES) {
            if (entity.getDomainObjectClass() != null) {
                classMap.put(entity.getDomainObjectClass(), entity);
            }
        }

        REVERSE_ORDER = new ArrayList<>(ENTITIES);
        Collections.sort(REVERSE_ORDER, new Comparator<BackupEntity>() {
            @Override
            public int compare(BackupEntity o1, BackupEntity o2) {
                return o1.getOrder() - o2.getOrder();
            }

        });
    }

    @Override
    public List<BackupEntity> findBackupEntities() {
        return ENTITIES;
    }

    @Override
    public BackupEntity forClass(Class clazz) {
        return classMap.get(clazz);
    }

    @Override
    public List<BackupEntity> reverseOrderedValues() {
        return REVERSE_ORDER;
    }

    @Override
    public BackupEntity userRoleBackupEntity() {
        return userToUserRole;
    }
}
