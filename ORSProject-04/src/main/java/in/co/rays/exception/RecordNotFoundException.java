package in.co.rays.exception;

/**
 * RecordNotFoundException is a custom checked exception used to indicate that a
 * requested record was not found in the data source.
 * 
 * It extends the standard Java Exception class.
 * 
 * Usage: This exception can be thrown when a search or retrieval operation
 * fails to find a record with the specified criteria.
 * 
 * @author Chetan Patidar
 */
public class RecordNotFoundException extends Exception {

	/**
	 * Constructs a new RecordNotFoundException with the specified detail message.
	 * 
	 * @param msg the detail message
	 */
	public RecordNotFoundException(String msg) {
		super(msg);
	}
}
