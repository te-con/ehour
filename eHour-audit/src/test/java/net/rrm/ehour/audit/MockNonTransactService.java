package net.rrm.ehour.audit;

import org.springframework.stereotype.Component;

@Component
@NonAuditable
public class MockNonTransactService
{
	public void getMethod()
	{
		
	}
}
