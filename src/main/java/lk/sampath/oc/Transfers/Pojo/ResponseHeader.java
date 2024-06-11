package lk.sampath.oc.Transfers.Pojo;

import lk.sampath.oc.Transfers.Enums.InvokeStatus;

public class ResponseHeader {

	private InvokeStatus returnStatus;
	private String returnCode;
	private String returnMessage;

	public InvokeStatus getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(InvokeStatus returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public ResponseHeader(InvokeStatus returnStatus, String returnCode, String returnMessage) {
		super();
		this.returnStatus = returnStatus;
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}

	public ResponseHeader(InvokeStatus returnStatus, String returnMessage) {
		super();
		this.returnStatus = returnStatus;
		this.returnMessage = returnMessage;
	}

	public ResponseHeader() {
		super();
	}

	@Override
	public String toString() {
		return "ResponseHeader [returnStatus=" + returnStatus + ", returnCode=" + returnCode + ", returnMessage="
				+ returnMessage + "]";
	}

}
