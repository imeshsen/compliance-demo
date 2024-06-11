package lk.sampath.oc.Transfers.Pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.sampath.oc.Transfers.Pojo.mobile.CapexPayLoad;

public class TransferResponse {
	
	String authenticationType;
	long transferReference;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private CapexPayLoad capexPayLoad;
	ResponseHeader responseHeader;
	
	
	public String getAuthenticationType() {
		return authenticationType;
	}
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	/**
	 * @return the transferReference
	 */
	public long getTransferReference() {
		return transferReference;
	}

	public CapexPayLoad getCapexPayLoad() {
		return capexPayLoad;
	}

	public void setCapexPayLoad(CapexPayLoad capexPayLoad) {
		this.capexPayLoad = capexPayLoad;
	}

	/**
	 * @param transferReference the transferReference to set
	 */

	public void setTransferReference(long transferReference) {
		this.transferReference = transferReference;
	}
	public TransferResponse(String authenticationType, long transferReference, ResponseHeader resHeader) {
		super();
		this.authenticationType = authenticationType;
		this.transferReference = transferReference;
		this.responseHeader = resHeader;
	}

	public TransferResponse() {
		
	}
	@Override
	public String toString() {
		return "TransferResponse [authenticationType=" + authenticationType + ", transferReference=" + transferReference
				+ ", responseHeader=" + responseHeader + "]";
	}
	
	
	
}
