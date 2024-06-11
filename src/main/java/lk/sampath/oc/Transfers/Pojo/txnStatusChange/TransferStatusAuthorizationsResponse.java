package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import java.util.List;

import lk.sampath.oc.Transfers.Pojo.CommonResponse;

public class TransferStatusAuthorizationsResponse extends CommonResponse {

	private List<TransferStatusAuthorizationsOutputDto> listOfTransactions;

	@Override
	public String toString() {
		return "TransferStatusAuthorizationsResponse [returnCode=" + returnCode + ", returnMessage=" + returnMessage + "listOfTransactions="
				+ listOfTransactions + "]";
	}

	public List<TransferStatusAuthorizationsOutputDto> getListOfTransactions() {
		return listOfTransactions;
	}

	public void setListOfTransactions(List<TransferStatusAuthorizationsOutputDto> listOfTransactions) {
		this.listOfTransactions = listOfTransactions;
	}

}
