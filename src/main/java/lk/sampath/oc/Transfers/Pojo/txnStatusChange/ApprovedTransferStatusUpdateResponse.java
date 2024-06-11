package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import io.swagger.annotations.ApiModelProperty;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;

public class ApprovedTransferStatusUpdateResponse extends ResponseHeader {

	@ApiModelProperty(notes = "Comment by authorizer", example = "Comments")
	private String comment;

	@Override
	public String toString() {
		return "SubmitTransferStatusUpdateResponse [returnCode=" + getReturnCode() + " returnStatus="
				+ getReturnStatus() + ", returnMessage=" + getReturnMessage() + ", comment=" + comment + "]";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
