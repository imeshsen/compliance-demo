package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import java.util.List;

import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lk.sampath.oc.Transfers.Pojo.reports.FundTransferReportDTO;

public class FundTransfersReportResponseDTO {

	ResponseHeader responseHeader;
	List<FundTransferReportDTO> listOfTransactions;
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	
	public FundTransfersReportResponseDTO(ResponseHeader responseHeader, List<FundTransferReportDTO> listOfTransactions) {
		super();
		this.responseHeader = responseHeader;
		this.listOfTransactions = listOfTransactions;
	}
	@Override
	public String toString() {
		return "fundTransfersResponseDTO [responseHeader=" + responseHeader + ", listOfTransactions=" + listOfTransactions + "]";
	}
	public List<FundTransferReportDTO> getListOfTransactions() {
		return listOfTransactions;
	}
	public void setListOfTransactions(List<FundTransferReportDTO> listOfTransactions) {
		this.listOfTransactions = listOfTransactions;
	}
	public FundTransfersReportResponseDTO() {
		super();
	}
	
	
}
