package net.rrm.ehour.it;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTruncater {
    public static void truncate(DataSource dataSource) throws SQLException {
         Connection connection = dataSource.getConnection();

        connection.prepareCall("DELETE FROM AUDIT").execute();
        connection.prepareCall("DELETE FROM MAIL_LOG").execute();
        connection.prepareCall("DELETE FROM TIMESHEET_COMMENT").execute();
        connection.prepareCall("DELETE FROM TIMESHEET_ENTRY").execute();

        connection.prepareCall("DELETE FROM PROJECT_ASSIGNMENT").execute();

        connection.prepareCall("DELETE FROM PROJECT").execute();
        connection.prepareCall("DELETE FROM CUSTOMER").execute();
        connection.prepareCall("DELETE FROM USER_TO_USERROLE WHERE user_id > 1").execute();
        connection.prepareCall("DELETE FROM USER_TO_DEPARTMENT WHERE user_id > 1").execute();
        connection.prepareCall("DELETE FROM USERS WHERE user_id > 1").execute();

        connection.close();
    }
}
