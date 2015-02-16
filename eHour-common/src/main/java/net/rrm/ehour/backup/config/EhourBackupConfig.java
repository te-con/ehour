package net.rrm.ehour.backup.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.common.BackupEntityType;
import net.rrm.ehour.backup.common.BackupJoinTable;
import net.rrm.ehour.domain.*;

import java.util.*;


public class EhourBackupConfig implements BackupConfig {
    private static final BackupEntityType userDepartment = new BackupEntityType(UserDepartment.class, "USER_DEPARTMENT", 0);
    private static final BackupEntityType userRole = new BackupEntityType(UserRole.class, "USER_ROLE", 1);
    private static final BackupEntityType users = new BackupEntityType(User.class, "USERS", "USERLIST", 2);
    private static final BackupEntityType customer = new BackupEntityType(Customer.class, "CUSTOMER", 3);
    private static final BackupEntityType project = new BackupEntityType(Project.class, "PROJECT", 4);
    private static final BackupEntityType projectAssignmentType = new BackupEntityType(ProjectAssignmentType.class, "PROJECT_ASSIGNMENT_TYPE", 5);
    private static final BackupEntityType projectAssignment = new BackupEntityType(ProjectAssignment.class, "PROJECT_ASSIGNMENT", 6);
    private static final BackupEntityType timesheetEntry = new BackupEntityType(TimesheetEntry.class, "TIMESHEET_ENTRY", "TIMESHEET_ENTRIES", new TimesheetEntryRowProcessor(), 7);
    private static final BackupEntityType timesheetComment = new BackupEntityType(TimesheetComment.class, "TIMESHEET_COMMENT", 8);
    private static final BackupEntityType timesheetLock = new BackupEntityType(TimesheetLock.class, "TIMESHEET_LOCK", 11);

    private static final BackupJoinTable USER_TO_USERROLE = new BackupJoinTable("USER_TO_USERROLE", "USER_ID", "ROLE");
    private static final BackupJoinTable USER_TO_DEPARTMENT = new BackupJoinTable("USER_TO_DEPARTMENT", "USER_ID", "DEPARTMENT_ID");

    private static final List<BackupEntityType> ENTITIES = Lists.newArrayList(userDepartment, userRole, users,
                                                                            customer, project, projectAssignmentType, projectAssignment,
                                                                            timesheetEntry, timesheetComment, timesheetLock);

    private static final List<BackupEntityType> REVERSE_ORDER;

    private static final Map<Class<?>, BackupEntityType> ENTITY_CLASS_MAP;

    static {
        ENTITY_CLASS_MAP = new HashMap<>();

        for (BackupEntityType entity : ENTITIES) {
            if (entity.getDomainObjectClass() != null) {
                ENTITY_CLASS_MAP.put(entity.getDomainObjectClass(), entity);
            }
        }

        REVERSE_ORDER = new ArrayList<>(ENTITIES);
        Collections.sort(REVERSE_ORDER, new Comparator<BackupEntityType>() {
            @Override
            public int compare(BackupEntityType o1, BackupEntityType o2) {
                return o2.getOrder() - o1.getOrder();
            }

        });
    }

    private static final List<BackupJoinTable> JOIN_TABLES = Lists.newArrayList(USER_TO_USERROLE, USER_TO_DEPARTMENT);

    private static final Map<String, BackupJoinTable> JOIN_TABLE_NAME_MAP;

    static {
        JOIN_TABLE_NAME_MAP = Maps.newHashMap();

        for (BackupJoinTable joinTable : JOIN_TABLES) {
            JOIN_TABLE_NAME_MAP.put(joinTable.getTableName(), joinTable);
        }
    }

    @Override
    public List<BackupEntityType> backupEntities() {
        return ENTITIES;
    }

    @Override
    public BackupEntityType entityForClass(Class clazz) {
        return ENTITY_CLASS_MAP.get(clazz);
    }

    @Override
    public List<BackupEntityType> reverseOrderedValues() {
        return REVERSE_ORDER;
    }

    @Override
    public List<BackupJoinTable> joinTables() {
        return JOIN_TABLES;
    }

    @Override
    public BackupJoinTable joinTableForName(String name) {
        return JOIN_TABLE_NAME_MAP.get(name);
    }
}
