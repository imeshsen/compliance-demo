package lk.sampath.oc.Transfers.Pojo;

import lk.sampath.oc.Transfers.response.CustomMcashResponse;

public class WithdrawMCashResponse {

	ResponseHeader responseHeader;
	CustomMcashResponse withdrawResponse;
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	/**
	 * @return the withdrawResponse
	 */
	public CustomMcashResponse getWithdrawResponse() {
		return withdrawResponse;
	}
	/**
	 * @param withdrawResponse the withdrawResponse to set
	 */
	public void setWithdrawResponse(CustomMcashResponse withdrawResponse) {
		this.withdrawResponse = withdrawResponse;
	}
	@Override
	public String toString() {
		return "WithdrawMCashResponse [responseHeader=" + responseHeader + ", withdrawResponse=" + withdrawResponse
				+ "]";
	}
	public WithdrawMCashResponse(ResponseHeader responseHeader, CustomMcashResponse withdrawResponse) {
		super();
		this.responseHeader = responseHeader;
		this.withdrawResponse = withdrawResponse;
	}
	public WithdrawMCashResponse() {
		super();
	}
	
	
}
