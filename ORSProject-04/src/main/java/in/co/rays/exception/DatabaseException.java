package in.co.rays.exception;

/**
 * DatabaseException is a custom checked exception used to indicate errors
 * related to database operations.
 * 
 * It extends the standard Java Exception class.
 * 
 * Usage: This exception can be thrown when there is a failure or error while
 * interacting with the database.
 * 
 * @author Chetan Patidar
 */
public class DatabaseException extends Exception {

	/**
	 * Constructs a new DatabaseException with the specified detail message.
	 * 
	 * @param msg the detail message
	 */
	public DatabaseException(String msg) {
		super(msg);
	}
}