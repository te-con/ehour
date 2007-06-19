/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.rrm.ehour.ui.authentication;

import net.rrm.ehour.ui.authorization.PrincipalImpl;

import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.strategies.LoginException;

/**
 * Context for primary login. It will let you use the home, balance and transfer
 * pages. just not the commit page. you will need a secondary login for that.
 * 
 * @author marrink
 * 
 */
public class EhourLoginContext extends LoginContext
{

	private static final long serialVersionUID = 1L;

	private  String username;

	private  String password;

	public EhourLoginContext(String username, String password)
	{
		super();
		this.username = username;
		this.password = password;
	}

	/**
	 * @see org.apache.wicket.security.hive.authentication.LoginContext#login()
	 */
	public Subject login() throws LoginException
	{
		// the context is a disposable object, we explicitly check that here,
		// but it is not required
		if(username==null || password==null)
			throw new LoginException("Can not reuse this context");

		if (username.equalsIgnoreCase("thies") && password.equalsIgnoreCase("test"))
		{
		// authenticate username, password, throw exception if not found

		DefaultSubject user=new DefaultSubject();
		user.addPrincipal(new PrincipalImpl("consultant"));
		// grant principals: (user.addPrincipal(Principal))
		// Note Subjects are read only, but implementations like DefaultSubject
		// will usually have some sort of way for you to add principals.
		// Implementations are required to honor the readonly flag which swarm
		// sets immediately after obtaining the subject.

		// clear username and password to enforce disposable behaviour
		this.username=null;
		this.password=null;
		return user;
		}
		else if (username.equalsIgnoreCase("thies") && password.equalsIgnoreCase("test2"))
		{
			DefaultSubject user=new DefaultSubject();
			user.addPrincipal(new PrincipalImpl("admin"));
			System.out.println("admin");
			// grant principals: (user.addPrincipal(Principal))
			// Note Subjects are read only, but implementations like DefaultSubject
			// will usually have some sort of way for you to add principals.
			// Implementations are required to honor the readonly flag which swarm
			// sets immediately after obtaining the subject.

			// clear username and password to enforce disposable behaviour
			this.username=null;
			this.password=null;
			return user;
		}
		else
		{
			throw new LoginException("you not you!");
		}
 	}
}
