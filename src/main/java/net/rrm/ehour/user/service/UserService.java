package net.rrm.ehour.user.service;

import net.rrm.ehour.exception.NoResultsException;
import net.rrm.ehour.user.domain.User;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;


public interface UserService extends UserDetailsService
{
	/**
	 * Get user by username and password
	 * @param username
	 * @param password
	 * @return
	 * @throws NoResultsException
	 */
	public User getUser(String username, String password);

	/**
	 * Get user by username (acegi)
	 * @param username
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;
	
	/**
	 * Get user by userID
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */
    public User getUser(Integer userID);

}
