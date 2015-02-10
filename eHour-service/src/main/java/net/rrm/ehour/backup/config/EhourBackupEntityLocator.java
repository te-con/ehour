package net.rrm.ehour.backup.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.rrm.ehour.backup.service.backup.BackupEntity;
import net.rrm.ehour.backup.service.backup.BackupEntityLocator;
import net.rrm.ehour.backup.service.backup.BackupEntitySingleTable;
import net.rrm.ehour.backup.service.backup.BackupJoinTable;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.backup.dao.TimesheetEntryRowProcessor;

import java.util.*;


// TODO rename to config
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
    private static final BackupEntity timesheetLock = new BackupEntitySingleTable(TimesheetLock.class, "TIMESHEET_LOCK", 11);

    private static final BackupJoinTable USER_TO_USERROLE = new BackupJoinTable("USER_TO_USERROLE", "USER_ID", "ROLE");
    private static final BackupJoinTable USER_TO_DEPARTMENT = new BackupJoinTable("USER_TO_DEPARTMENT", "USER_ID", "DEPARTMENT_ID");

    private static final List<BackupEntity> ENTITIES = Lists.newArrayList(userDepartment, userRole, users,
                                                                            customer, project, projectAssignmentType, projectAssignment,
                                                                            timesheetEntry, timesheetComment, timesheetLock);

    private static final List<BackupEntity> REVERSE_ORDER;

    private static final Map<Class<?>, BackupEntity> ENTITY_CLASS_MAP;
    private static final Map<String, BackupEntity> ENTITY_CLASS_NAME_MAP;


    static {
        ENTITY_CLASS_MAP = new HashMap<>();
        ENTITY_CLASS_NAME_MAP = new HashMap<>();

        for (BackupEntity entity : ENTITIES) {
            if (entity.getDomainObjectClass() != null) {
                ENTITY_CLASS_MAP.put(entity.getDomainObjectClass(), entity);
            }

            ENTITY_CLASS_NAME_MAP.put(entity.name(), entity);
        }

        REVERSE_ORDER = new ArrayList<>(ENTITIES);
        Collections.sort(REVERSE_ORDER, new Comparator<BackupEntity>() {
            @Override
            public int compare(BackupEntity o1, BackupEntity o2) {
                return o1.getOrder() - o2.getOrder();
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
    public List<BackupEntity> backupEntities() {
        return ENTITIES;
    }

    @Override
    public BackupEntity entityForClass(Class clazz) {
        return ENTITY_CLASS_MAP.get(clazz);
    }

    @Override
    public BackupEntity entityForName(String name) {
        return ENTITY_CLASS_NAME_MAP.get(name);
    }

    @Override
    public List<BackupEntity> reverseOrderedValues() {
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

    // @TODO REMOVE
    @Override
    public BackupEntity userRoleBackupEntity() {
        return null;
    }
}
