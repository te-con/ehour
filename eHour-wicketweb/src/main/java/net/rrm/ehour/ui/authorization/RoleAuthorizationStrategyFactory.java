package net.rrm.ehour.ui.authorization;

import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;

public class RoleAuthorizationStrategyFactory implements AuthorizationStrategyFactory
{

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.authorization.RoleAuthorizationFactory#getRoleAuthorizationStrategy(org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy)
	 */
	public IAuthorizationStrategy getAuthorizationStrategy(IRoleCheckingStrategy strategy)
	{
		return new RoleAuthorizationStrategy(strategy);
	}
}
