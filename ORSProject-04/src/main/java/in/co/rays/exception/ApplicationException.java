package in.co.rays.exception;

/**
 * ApplicationException is a custom checked exception used to indicate
 * application-level errors.
 * 
 * It extends the standard Java Exception class.
 * 
 * Usage: This exception can be thrown when an unexpected error occurs during
 * the execution of application logic.
 * 
 * @author Chetan Patidar
 */
public class ApplicationException extends Exception {

	/**
	 * Constructs a new ApplicationException with the specified detail message.
	 * 
	 * @param msg the detail message
	 */
	public ApplicationException(String msg) {
		super(msg);
	}
}