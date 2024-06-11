package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lk.sampath.oc.Transfers.Enums.TransactionApprovalAction;
import lk.sampath.oc.Transfers.Enums.TransactionApprovalStatus;

public class TransferStatusUpdateDTO {
	@NotNull
	@ApiModelProperty(notes = "User ID of logged in Admin")
	private String adminUserId;
	@NotNull
	@ApiModelProperty(notes = "Approval Id")
	private String approvalId;
	@NotNull
	@ApiModelProperty(notes = "User Group")
	private String userGroup;
	
	@NotNull
	@ApiModelProperty(notes = "PENDING / VERIFIED / REJECTED")
	private TransactionApprovalAction action;
	@ApiModelProperty(notes = "Comments By the Autorizer")
	private String comments;

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	public TransactionApprovalAction getAction() {
		return action;
	}

	public void setAction(TransactionApprovalAction action) {
		this.action = action;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

}
