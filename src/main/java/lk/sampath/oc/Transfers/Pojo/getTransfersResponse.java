package lk.sampath.oc.Transfers.Pojo;

import java.util.List;

import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;

public class getTransfersResponse {

	ResponseHeader responseHeader;
	List<TransactionRequestHistory> trList;
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	public List<TransactionRequestHistory> getTrList() {
		return trList;
	}
	public void setTrList(List<TransactionRequestHistory> trList) {
		this.trList = trList;
	}
	public getTransfersResponse(ResponseHeader responseHeader, List<TransactionRequestHistory> trList) {
		super();
		this.responseHeader = responseHeader;
		this.trList = trList;
	}
	@Override
	public String toString() {
		return "getTransfersResponse [responseHeader=" + responseHeader + ", trList=" + trList + "]";
	}
	public getTransfersResponse() {
		super();
	}
	
	
}
