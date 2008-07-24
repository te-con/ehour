/**
 * 
 */
package net.rrm.ehour.ui.authorization;

import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;

/**
 * @author Thies
 *
 */
public interface AuthorizationStrategyFactory
{
	/**
	 * Create role authorization strategy
	 * @param strategy
	 * @return
	 */
	public IAuthorizationStrategy getAuthorizationStrategy(IRoleCheckingStrategy strategy);
}
