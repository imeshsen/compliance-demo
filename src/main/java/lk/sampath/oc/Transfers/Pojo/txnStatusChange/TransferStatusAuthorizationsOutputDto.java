package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class TransferStatusAuthorizationsOutputDto {
	@ApiModelProperty(notes = "Approval request ID")
	private String approvalId;
	@ApiModelProperty(notes = "New status updated by enterer")
	private String newStatus;
	@ApiModelProperty(notes = "Current transaction details ")
	private long transferRequestId;
	@ApiModelProperty(notes = "")
	private String transferType;
	@ApiModelProperty(notes = "")
	private Date transferRequestDate;
	@ApiModelProperty(notes = "")
	private BigDecimal amount;
	@ApiModelProperty(notes = "")
	private String currency;
	@ApiModelProperty(notes = "")
	private String fromAccount;
	@ApiModelProperty(notes = "")
	private String toAccount;
	@ApiModelProperty(notes = "")
	private String toBank;
	@ApiModelProperty(notes = "")
	private String userId;
	@ApiModelProperty(notes = "")
	private String status;
	public String getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	public long getTransferRequestId() {
		return transferRequestId;
	}
	public void setTransferRequestId(long transferRequestId) {
		this.transferRequestId = transferRequestId;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public Date getTransferRequestDate() {
		return transferRequestDate;
	}
	public void setTransferRequestDate(Date transferRequestDate) {
		this.transferRequestDate = transferRequestDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public String getToBank() {
		return toBank;
	}
	public void setToBank(String toBank) {
		this.toBank = toBank;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
