package net.rrm.ehour.dbunit;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class ExtractDataset
{
	public static void main(String[] args) throws Exception
	{
		// database connection
		Class driverClass = Class.forName("com.mysql.jdbc.Driver");
		
		Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://192.168.1.10/ehour", "aja", "jaa");
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        QueryDataSet partialDataSet = new QueryDataSet(connection);

        // take note of the order to prevent FK constraint violation when re-inserting
        partialDataSet.addTable("user_department");
        partialDataSet.addTable("user");
        partialDataSet.addTable("user_role");
        partialDataSet.addTable("user_to_userrole");
        partialDataSet.addTable("customer");
        partialDataSet.addTable("project");
        partialDataSet.addTable("project_assignment_type");
        partialDataSet.addTable("project_assignment");
        partialDataSet.addTable("timesheet_entry");
        partialDataSet.addTable("timesheet_comment");
        partialDataSet.addTable("mail_type");
        partialDataSet.addTable("mail_log");
        partialDataSet.addTable("mail_log_assignment");

        FlatXmlDataSet.write(partialDataSet,
                new FileOutputStream("src/test/resources/t2est-dataset.xml"));		
		System.out.println("Dataset written");
	}

}
