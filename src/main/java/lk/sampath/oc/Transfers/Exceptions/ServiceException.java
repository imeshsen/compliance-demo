package lk.sampath.oc.Transfers.Exceptions;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException  {
	
	private String errorCode;
	private String errorMessage;
	private Exception originalException;
	private HttpStatus status;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Exception getOriginalException() {
		return originalException;
	}

	public void setOriginalException(Exception originalException) {
		this.originalException = originalException;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public ServiceException(String errorCode, String errorMessage, Exception exception, HttpStatus status) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.status = status;
		this.setOriginalException(exception);
	}
}
