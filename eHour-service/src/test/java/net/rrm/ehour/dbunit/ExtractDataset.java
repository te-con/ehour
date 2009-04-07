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
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/ehour_08", "root", "root");
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        QueryDataSet partialDataSet = new QueryDataSet(connection);

        // take note of the order to prevent FK constraint violation when re-inserting
//        partialDataSet.addTable("user_department");
//        partialDataSet.addTable("user");
//        partialDataSet.addTable("user_role");
//        partialDataSet.addTable("user_to_userrole");
//        partialDataSet.addTable("customer");
//        partialDataSet.addTable("project");
//        partialDataSet.addTable("project_assignment_type");
//        partialDataSet.addTable("project_assignment");
//        partialDataSet.addTable("timesheet_entry");
//        partialDataSet.addTable("timesheet_comment");
//        partialDataSet.addTable("mail_type");
//        partialDataSet.addTable("mail_log");
//        partialDataSet.addTable("mail_log_assignment");
        partialDataSet.addTable("AUDIT");

        FlatXmlDataSet.write(partialDataSet,
                new FileOutputStream("src/test/resources/test-dataset-20081112.xml"));		
		System.out.println("Dataset written");
	}

}
