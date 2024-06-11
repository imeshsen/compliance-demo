package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import io.swagger.annotations.ApiModelProperty;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;

public class SubmitTransferStatusUpdateResponse extends ResponseHeader {

	@ApiModelProperty(notes = "approval Id")
	private String approvalId;

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	@Override
	public String toString() {
		return "SubmitTransferStatusUpdateResponse [returnCode=" + getReturnCode() + " returnStatus="
				+ getReturnStatus() + ", returnMessage=" + getReturnMessage() + ", approvalId=" + approvalId + "]";
	}

}
