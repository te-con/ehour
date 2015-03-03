package net.rrm.ehour.persistence.dao;

import net.rrm.ehour.persistence.dbvalidator.DerbyDbValidator;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.util.List;

public abstract class DatabasePopulator {
    public static void setUpDatabase(DataSource dataSource, List<String> dataSetFileNames, String dbVersion) throws Exception {
        DerbyDbValidator validator = new DerbyDbValidator(dbVersion, dataSource);
        validator.checkDatabaseState();

        Connection con = DataSourceUtils.getConnection(dataSource);
        con.setAutoCommit(false);
        IDatabaseConnection connection = new DatabaseConnection(con);

        for (String dataSetFileName : dataSetFileNames) {
            FlatXmlDataSet additionalDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/datasets/" + dataSetFileName));
            DatabaseOperation.CLEAN_INSERT.execute(connection, additionalDataSet);
        }
    }
}
