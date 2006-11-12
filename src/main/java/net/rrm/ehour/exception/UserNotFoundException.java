package net.rrm.ehour.exception;

public class UserNotFoundException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7136651575349812016L;

	/**
	 * 
	 */
	public UserNotFoundException()
    {
        super();
    }

    public UserNotFoundException(Exception e)
    {
        super(e);
    }

    public UserNotFoundException(String s)
    {
        super(s);
    }
}
