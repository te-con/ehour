/**
 * Created on Dec 29, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.criteria;

import java.util.List;

import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 * Available criteria for aggregate reports
 **/

public class AggregateAvailableCriteria extends AvailableCriteria
{
	private static final long serialVersionUID = -6687214845760958691L;
	
	private List<User>				users;
	private List<UserDepartment>	userDepartments;

	/**
	 * @return the userDepartments
	 */
	public List<UserDepartment> getUserDepartments()
	{
		return userDepartments;
	}
	/**
	 * @param userDepartments the userDepartments to set
	 */
	public void setUserDepartments(List<UserDepartment> userDepartments)
	{
		this.userDepartments = userDepartments;
	}
	/**
	 * @return the users
	 */
	public List<User> getUsers()
	{
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users)
	{
		this.users = users;
	}

}
