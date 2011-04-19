package net.rrm.ehour.persistence.dao;

import net.rrm.ehour.persistence.dbvalidator.DerbyDbValidator;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context-props.xml",
                                   "classpath:test-context-datasource.xml",
                                   "classpath:context-dbconnectivity.xml",
                                   "classpath:test-context-scanner-repository.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractAnnotationDaoTest
{
    @Autowired
    private DataSource eHourDataSource;

    @Value("${ehour.db.version}")
    private String dbVersion;

    private static FlatXmlDataSet userDataSet;
    private String[] additionalDataSetFileNames = new String[0];

    static
    {
        try
        {
            userDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/datasets/dataset-users.xml"));
        } catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public AbstractAnnotationDaoTest()
    {
    }

    public AbstractAnnotationDaoTest(String dataSetFileNames)
    {
        this(new String[]{dataSetFileNames});
    }

    public AbstractAnnotationDaoTest(String[] dataSetFileNames)
    {
        this.additionalDataSetFileNames = dataSetFileNames;
    }

    protected void setAdditionalDataSetFileNames(String[] additionalDataSetFileNames)
    {
        this.additionalDataSetFileNames = additionalDataSetFileNames;
    }


    @Before
    public final void setUpDatabase() throws Exception
    {
        DerbyDbValidator validator = new DerbyDbValidator(dbVersion, eHourDataSource);
        validator.checkDatabaseState();

        Connection con = DataSourceUtils.getConnection(eHourDataSource);
        IDatabaseConnection connection = new DatabaseConnection(con);

        DatabaseOperation.CLEAN_INSERT.execute(connection, userDataSet);

        for (String dataSetFileName : additionalDataSetFileNames)
        {
            FlatXmlDataSet additionalDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/datasets/" + dataSetFileName));
            DatabaseOperation.CLEAN_INSERT.execute(connection, additionalDataSet);
        }
    }

}
