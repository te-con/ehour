package net.rrm.ehour.persistence.activity.dao;

import junit.framework.Assert;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ActivityDaoHibernateImpl}.
 * 
 */
public class ActivityDaoHibernateImplTest extends AbstractAnnotationDaoTest {

	@Autowired
	private ActivityDao activityDao;
	
	@Test
	public void shouldPersistAndRetrieveActivityById() {
		Activity activity = new Activity();
		String activityName = "testActivity";
		activity.setName(activityName);
		activity.setActive(Boolean.TRUE);
		Activity persistedActivity = activityDao.persist(activity);
		
		Activity retrievedActivity = activityDao.findById(persistedActivity.getId());
		Assert.assertNotNull(retrievedActivity);
		Assert.assertEquals(activityName, retrievedActivity.getName());
	}
}
