package lk.sampath.oc.Transfers.Pojo.approval;

import java.io.Serializable;

import lk.sampath.oc.Transfers.Pojo.CommonResponse;

public class ApprovalResponseDTO  extends CommonResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private ApprovalDTO payLoad;
	private String approvalStatus;

	public ApprovalDTO  getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(ApprovalDTO payLoad) {
		this.payLoad = payLoad;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

}