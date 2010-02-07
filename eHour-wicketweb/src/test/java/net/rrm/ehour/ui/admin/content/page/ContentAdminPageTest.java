package net.rrm.ehour.ui.admin.content.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.domain.UserDepartmentMother;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;

import org.junit.Before;
import org.junit.Test;

/**
 * Created on Feb 6, 2010 9:55:35 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ContentAdminPageTest extends AbstractSpringWebAppTester
{
	private UserService userService;

	@Before
	public void setup()
	{
		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
	}
	
	@Test
	public void shouldRender()
	{
		expect(userService.getUserDepartments())
			.andReturn(UserDepartmentMother.createUserDepartments());
		
		replay(userService);
		
		tester.startPage(ContentAdminPage.class);
		tester.assertNoErrorMessage();
		
		verify(userService);
	}
}
