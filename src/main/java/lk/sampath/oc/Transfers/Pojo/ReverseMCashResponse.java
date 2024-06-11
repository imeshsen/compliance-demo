package lk.sampath.oc.Transfers.Pojo;

import lk.sampath.oc.Transfers.response.CustomMcashResponse;

public class ReverseMCashResponse {
	
	ResponseHeader responseHeader;
	CustomMcashResponse reverseResponse;
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	/**
	 * @return the reverseResponse
	 */
	public CustomMcashResponse getReverseResponse() {
		return reverseResponse;
	}
	/**
	 * @param reverseResponse the reverseResponse to set
	 */
	public void setReverseResponse(CustomMcashResponse reverseResponse) {
		this.reverseResponse = reverseResponse;
	}
	public ReverseMCashResponse(ResponseHeader responseHeader, CustomMcashResponse reverseResponse) {
		super();
		this.responseHeader = responseHeader;
		this.reverseResponse = reverseResponse;
	}
	public ReverseMCashResponse() {
		super();
	}
	@Override
	public String toString() {
		return "ReverseMCashResponse [responseHeader=" + responseHeader + ", reverseResponse=" + reverseResponse + "]";
	}
	
	

}
