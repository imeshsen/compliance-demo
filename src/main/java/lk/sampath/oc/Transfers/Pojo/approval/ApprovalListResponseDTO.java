package lk.sampath.oc.Transfers.Pojo.approval;

import java.io.Serializable;
import java.util.Collection;

import lk.sampath.oc.Transfers.Pojo.CommonResponse;

public class ApprovalListResponseDTO  extends CommonResponse implements Serializable {


	private static final long serialVersionUID = 1L;
	private Collection<ApprovalDTO> listOfApprovals;
	private String approvalStatus;

	public Collection<ApprovalDTO> getListOfApprovals() {
		return listOfApprovals;
	}

	public void setListOfApprovals(Collection<ApprovalDTO> listOfApprovals) {
		this.listOfApprovals = listOfApprovals;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

}