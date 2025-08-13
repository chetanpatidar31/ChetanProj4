package in.co.rays.exception;

/**
 * DuplicateRecordException is a custom checked exception used to indicate that
 * a duplicate record exists when trying to add or update data.
 * 
 * It extends the standard Java Exception class.
 * 
 * Usage: This exception can be thrown when an operation violates a uniqueness
 * constraint, such as a duplicate login ID or primary key.
 * 
 * @author Chetan Patidar
 */
public class DuplicateRecordException extends Exception {

	/**
	 * Constructs a new DuplicateRecordException with the specified detail message.
	 * 
	 * @param msg the detail message
	 */
	public DuplicateRecordException(String msg) {
		super(msg);
	}
}
