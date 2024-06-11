package lk.sampath.oc.Transfers.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name="OC_TRN_TRANSACTION_STATUS_CHANGE")
public class TransactionStatusChange {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_CHANGE_ID")
	@SequenceGenerator(name = "STATUS_CHANGE_ID", sequenceName = "SEQ_STATUS_CHANGE_ID", allocationSize=1)
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
	
	@ApiModelProperty(notes = "Approval User Group")
	@Column(name = "USER_GROUP")
	private String userGroup;
	
//	@Column(name = "MODE_OF_CC")
//	private String modeOfCC;
//
//	public String getModeOfCC() {
//		return modeOfCC;
//	}
//
//	public void setModeOfCC(String modeOfCC) {
//		this.modeOfCC = modeOfCC;
//	}

	public int getId() {
		return id;
	}

//	public CreditCardPaymentTransaction getCreditCardPaymentTransaction() {
//		return creditCardPaymentTransaction;
//	}
//
//	public void setCreditCardPaymentTransaction(CreditCardPaymentTransaction creditCardPaymentTransaction) {
//		this.creditCardPaymentTransaction = creditCardPaymentTransaction;
//	}

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

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	
		
}
