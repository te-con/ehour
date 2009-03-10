package net.rrm.ehour.audit.service;

import net.rrm.ehour.audit.NonAuditable;

import org.springframework.stereotype.Component;

@Component
@NonAuditable
public class MockNonTransactService
{
	public void getMethod()
	{
		
	}
}
