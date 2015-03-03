package net.rrm.ehour.persistence.dao;

import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@ContextConfiguration(locations = {"classpath:test-context-props.xml", "classpath:context-dbconnectivity.xml"})
public abstract class AbstractAnnotationDaoTest extends AbstractDaoTest {
    public AbstractAnnotationDaoTest() {
    }

    public AbstractAnnotationDaoTest(String dataSetFileName) {
        super(dataSetFileName);
    }

    public AbstractAnnotationDaoTest(List<String> additionalDataSets) {
        super(additionalDataSets);
    }
}
