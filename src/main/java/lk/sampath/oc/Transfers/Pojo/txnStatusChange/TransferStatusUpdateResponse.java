package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import io.swagger.annotations.ApiModelProperty;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;

public class TransferStatusUpdateResponse extends ResponseHeader {

	@ApiModelProperty(notes = "Generated approval request ID", example = "0000000001")
	private String approvalId;

	@Override
	public String toString() {
		return "SubmitTransferStatusUpdateResponse [returnCode=" + getReturnCode() + " returnStatus="
				+ getReturnStatus() + ", returnMessage=" + getReturnMessage() + ", approvalId=" + approvalId + "]";
	}

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

}
