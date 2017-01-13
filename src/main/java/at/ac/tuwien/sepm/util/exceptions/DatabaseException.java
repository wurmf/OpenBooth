package at.ac.tuwien.sepm.util.exceptions;

/**
 * Exception thrown if an error occurs while establishing a connection to a database or while doing anything at a similar low level near the database.
 */
public class DatabaseException extends Exception{
    public DatabaseException(String message){
        super(message);
    }
    public DatabaseException(Throwable e){
        super(e);
    }
    public DatabaseException(String message, Throwable e){super(message, e);}
}