package edu.sru.thangiah.exception;

/**
 * Custom exception class that represents a scenario where a resource is not found in the application.
 * It extends RuntimeException, meaning it is an unchecked exception.
 */
public class ResourceNotFoundException extends RuntimeException {
	 
	/**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
