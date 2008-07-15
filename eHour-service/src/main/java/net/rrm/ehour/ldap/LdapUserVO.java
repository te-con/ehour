package net.rrm.ehour.ldap;

import net.rrm.ehour.domain.User;


public class LdapUserVO  {

	public String lastName;
	public String firstName;
	public String description;
	public String password;
	public String login;
	public String email;
	
	public User wrapToUser(){
		
		
		User user = new User();
		
		user.setActive(true);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(login);
		
		return user;
		
	}
	


	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


/********************************* METHODES d'accés aux interfaces DAO *************************/
	public String  toString(){
		String temp="";
		temp+="login = " + getLogin() +" , ";
		temp+="lastName = " + getLastName() +" , ";
		temp+="firstName = " + getFirstName() +" , ";
		temp+="email = " + getEmail() +" , ";
		temp+="password = " + getPassword() ;
		
	  return temp;
	}
	
	
/********************************Fin METHODES d'accés aux interfaces DAO *************************/	
	

}
