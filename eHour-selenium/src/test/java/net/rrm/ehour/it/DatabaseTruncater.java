package net.rrm.ehour.it;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTruncater {
    private DatabaseTruncater() {
    }

    public static void truncate(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();

             CallableStatement c1 = connection.prepareCall("DELETE FROM AUDIT");
             CallableStatement c2 = connection.prepareCall("DELETE FROM MAIL_LOG");
             CallableStatement c3 = connection.prepareCall("DELETE FROM TIMESHEET_COMMENT");
             CallableStatement c4 = connection.prepareCall("DELETE FROM TIMESHEET_ENTRY");
             CallableStatement c5 = connection.prepareCall("DELETE FROM TIMESHEET_LOCK_EXCLUSION");
             CallableStatement c6 = connection.prepareCall("DELETE FROM TIMESHEET_LOCK");

             CallableStatement c7 = connection.prepareCall("DELETE FROM PROJECT_ASSIGNMENT");

             CallableStatement c8 = connection.prepareCall("DELETE FROM PROJECT");
             CallableStatement c9 = connection.prepareCall("DELETE FROM CUSTOMER");
             CallableStatement c10 = connection.prepareCall("DELETE FROM USER_TO_USERROLE WHERE user_id != 1");
             CallableStatement c11 = connection.prepareCall("DELETE FROM USER_TO_DEPARTMENT WHERE user_id != 1");
             CallableStatement c12 = connection.prepareCall("DELETE FROM USERS WHERE user_id != 1");
        ) {
            c1.execute();
            c2.execute();
            c3.execute();
            c4.execute();
            c5.execute();
            c6.execute();
            c7.execute();
            c8.execute();
            c9.execute();
            c10.execute();
            c11.execute();
            c12.execute();
        }
    }
}
