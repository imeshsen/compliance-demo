package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lk.sampath.oc.Transfers.Enums.TransactionApprovalStatus;

public class SubmitTransferStatusChangeRequestDTO {
	
	@NotNull(message = "Admin user id can not be empty")
	@ApiModelProperty(notes = "User ID of logged in Admin")
	private String adminUserId;
	
	@NotNull(message = "User Group id can not be empty")
	@ApiModelProperty(notes = "User Group of logged in Admin")
	private String userGroup;
	
//	@NotNull(message = "modeOfCC can not be empty")
//	@ApiModelProperty(notes = "Mode of cc (CC_OWN / CC_OTH )")
//	private String modeOfCC;
	
	@NotNull(message = "Transfer Request ID Can not be empty")
	private String request;
	@NotNull(message = "Transfer Request ID Can not be empty")
	@ApiModelProperty(notes = "Transfer reference")
	
	private long transferRequestId;
	@ApiModelProperty(notes = "New status (SUCCESS / FAIL )")
	private TransactionApprovalStatus newStatus;
	
//	public String getModeOfCC() {
//		return modeOfCC;
//	}
//	public void setModeOfCC(String modeOfCC) {
//		this.modeOfCC = modeOfCC;
//	}
	
	public String getAdminUserId() {
		return adminUserId;
	}
	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}
	public long getTransferRequestId() {
		return transferRequestId;
	}
	public void setTransferRequestId(long transferRequestId) {
		this.transferRequestId = transferRequestId;
	}
	public TransactionApprovalStatus getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(TransactionApprovalStatus newStatus) {
		this.newStatus = newStatus;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
		
}
