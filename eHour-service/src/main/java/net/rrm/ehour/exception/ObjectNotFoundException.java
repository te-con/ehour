package net.rrm.ehour.exception;

public class ObjectNotFoundException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7136651575349812016L;

	/**
	 * 
	 */
	public ObjectNotFoundException()
    {
        super();
    }

    public ObjectNotFoundException(Exception e)
    {
        super(e);
    }

    public ObjectNotFoundException(String s)
    {
        super(s);
    }
}
