package lk.sampath.oc.Transfers.Pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ValidateTransferResponse {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String tranIndex;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String transferType;
	ResponseHeader responseHeader;

	public void setTranIndex(String tranIndex) {
		this.tranIndex = tranIndex;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTranIndex() {
		return tranIndex;
	}

	public String getTransferType() {
		return transferType;
	}

	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	@Override
	public String toString() {
		return "ValidateTransferOutput [responseHeader=" + responseHeader + "]";
	}
	
	
}
