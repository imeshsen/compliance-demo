package lk.sampath.oc.Transfers.Exceptions;

public class InvalidUrlException

		extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUrlException(String message) {
		super(message);
	}

	public InvalidUrlException(String message, Throwable cause) {
		super(message, cause);
	}

}
