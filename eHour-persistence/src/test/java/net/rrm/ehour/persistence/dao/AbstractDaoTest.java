package net.rrm.ehour.persistence.dao;

import com.google.common.collect.Lists;
import net.rrm.ehour.config.PersistenceConfig;
import org.hibernate.SessionFactory;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(propagation = Propagation.REQUIRES_NEW)
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractDaoTest {
    @Autowired
    private SessionFactory sessionFactory;

    private List<String> datasetFilenames = Lists.newArrayList("dataset-users.xml");

    public AbstractDaoTest() {
        System.setProperty("EHOUR_TEST", "true");
    }

    public AbstractDaoTest(String dataSetFileName) {
        this(Lists.newArrayList(dataSetFileName));
    }

    public AbstractDaoTest(List<String> additionalDataSets) {
        this();
        this.datasetFilenames.addAll(additionalDataSets);
    }

    @Before
    public final void setUpDatabase() throws Exception {
        DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
        DatabasePopulator.setUpDatabase(dataSource, datasetFilenames, getRequiredDbVersion());
    }

    protected String getRequiredDbVersion() {
        return PersistenceConfig.DB_VERSION;
    }
}
