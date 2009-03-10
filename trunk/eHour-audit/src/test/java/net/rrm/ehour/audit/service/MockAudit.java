package net.rrm.ehour.audit.service;

import net.rrm.ehour.domain.Audit;

public interface MockAudit
{
	public int getCalled();

	public void resetCalled();
	
	public Audit getAudit();
}
