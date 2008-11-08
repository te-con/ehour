package net.rrm.ehour.domain;

import java.util.Date;

public class Audit extends DomainObject<String, Audit>
{
	private static final long serialVersionUID = -5025801585806813596L;

	private User	user;
	private Date	date;
	private String	action;
		

	public Audit setAction(String action)
	{
		this.action = action;
		return this;
	}
	public Audit setUser(User user)
	{
		this.user = user;
		return this;
	}
	
	public Audit setDate(Date date)
	{
		this.date = date;
		return this;
	}
	
	@Override
	public boolean equals(Object object)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPK()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}
	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}
	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}
	public int compareTo(Audit o)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
