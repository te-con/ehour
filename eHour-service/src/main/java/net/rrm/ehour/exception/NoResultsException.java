package net.rrm.ehour.exception;

public class NoResultsException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6710566328564224736L;

	public NoResultsException()
    {
        super();
    }

    public NoResultsException(Exception e)
    {
        super(e);
    }

    public NoResultsException(String s)
    {
        super(s);
    }
}
