package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import java.util.List;

import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;
import lk.sampath.oc.Transfers.Pojo.FundTransferDTO;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;

public class GetFundTransfersResponseDTO {

	ResponseHeader responseHeader;
	List<FundTransferDTO> listOfTransactions;
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	
	public GetFundTransfersResponseDTO(ResponseHeader responseHeader, List<FundTransferDTO> listOfTransactions) {
		super();
		this.responseHeader = responseHeader;
		this.listOfTransactions = listOfTransactions;
	}
	@Override
	public String toString() {
		return "getTransfersResponse [responseHeader=" + responseHeader + ", listOfTransactions=" + listOfTransactions + "]";
	}
	public List<FundTransferDTO> getListOfTransactions() {
		return listOfTransactions;
	}
	public void setListOfTransactions(List<FundTransferDTO> listOfTransactions) {
		this.listOfTransactions = listOfTransactions;
	}
	public GetFundTransfersResponseDTO() {
		super();
	}
	
	
}
