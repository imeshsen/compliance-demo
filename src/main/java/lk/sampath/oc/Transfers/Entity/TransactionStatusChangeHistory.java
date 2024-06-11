package lk.sampath.oc.Transfers.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lk.sampath.oc.Transfers.Enums.TransactionApprovalAction;

@Entity
@Table(name="OC_TRN_TRANSACTION_STATUS_CHANGE_HISTORY")
public class TransactionStatusChangeHistory {
	
	@Id
	private int id;
	
	@NotNull
	@Column(name = "ADMIN_USER_ID")
	private String adminUserId;
	
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRANSACTION_REQUEST_ID")
	private TransactionRequestHistory transactionRequestHistory;
	
//	@OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "CC_PAY_ID")
//	private CreditCardPaymentTransaction creditCardPaymentTransaction;
	
	@Column(name = "NEW_STATUS")
	private String newStatus;
	
	@ApiModelProperty(notes = "Approval Id")
	@Column(name = "APPROVAL_ID")
	private String approvalId;
	
	@ApiModelProperty(notes = "Approval user group")
	@Column(name = "USER_GROUP")
	private String userGroup;
	
	
	@ApiModelProperty(notes = "Accept / Reject", example = "Accept")
	@Column(name = "APPROVAL_ACTION")
	private TransactionApprovalAction approvalAction;
	
	@ApiModelProperty(notes = "Comment by authorizer")
	@Column(name = "COMMENT")
	private String comment;


//	public CreditCardPaymentTransaction getCreditCardPaymentTransaction() {
//		return creditCardPaymentTransaction;
//	}
//
//	public void setCreditCardPaymentTransaction(CreditCardPaymentTransaction creditCardPaymentTransaction) {
//		this.creditCardPaymentTransaction = creditCardPaymentTransaction;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public TransactionRequestHistory getTransactionRequestHistory() {
		return transactionRequestHistory;
	}

	public void setTransactionRequestHistory(TransactionRequestHistory transactionRequestHistory) {
		this.transactionRequestHistory = transactionRequestHistory;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	

	public TransactionApprovalAction getApprovalAction() {
		return approvalAction;
	}

	public void setApprovalAction(TransactionApprovalAction approvalAction) {
		this.approvalAction = approvalAction;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	
	
		
}
