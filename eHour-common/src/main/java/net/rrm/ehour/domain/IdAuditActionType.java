package net.rrm.ehour.domain;

public interface IdAuditActionType<E extends Enum<E>>
{
    public int getId();  
    public E getEnum();  
}
