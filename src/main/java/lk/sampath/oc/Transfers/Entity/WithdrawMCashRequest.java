package lk.sampath.oc.Transfers.Entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="OC_TRN_WITHDRAW_MCASH")
public class WithdrawMCashRequest {
	
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WITHDRAW_ID")
	@SequenceGenerator(name = "WITHDRAW_ID", sequenceName = "SEQ_WITHDRAW_ID", allocationSize=1)
	@Column(name="WITHDRAW_ID")
	private long withdrawId;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="TRANSACTION_ID")
	private String transactionId;
	
	@Column(name="ACCNT_NUM")
	private String accountNumber;
	
	@Column(name="BENEFICIARY_ID")
	private String beneficiaryId;
	
	@Column(name="BENEFICIARY_MESSAGE")
	private String beneficiaryMessage;
	
	@Transient
	@Column(name="BENEFICIARY_PIN")
	private String beneficiaryPin;
	
	@Column(name="COMPANY_NAME")
	private String companyName;
	
	@Column(name="PAID_BY")
	private String paidBy;
	
	@Column(name="PAID_DATE")
	private Date paidDate;
	
	@Column(name="SEND_MESSAGE")
	private String sendMessage;
	
	@Column(name="TXN_AMOUNT")
	private BigDecimal txnAmount;
	
	@Transient
	@Column(name="TXN_PIN")
	private String txnPin;
	
	@JsonIgnore
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADDED_DATE")
	private Date addedDate;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBeneficiaryId() {
		return beneficiaryId;
	}
	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}
	public String getBeneficiaryMessage() {
		return beneficiaryMessage;
	}
	public void setBeneficiaryMessage(String beneficiaryMessage) {
		this.beneficiaryMessage = beneficiaryMessage;
	}
	public String getBeneficiaryPin() {
		return beneficiaryPin;
	}
	public void setBeneficiaryPin(String beneficiaryPin) {
		this.beneficiaryPin = beneficiaryPin;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPaidBy() {
		return paidBy;
	}
	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	public Date getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public BigDecimal getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getTxnPin() {
		return txnPin;
	}
	public void setTxnPin(String txnPin) {
		this.txnPin = txnPin;
	}
	/**
	 * @return the withdrawId
	 */
	public long getWithdrawId() {
		return withdrawId;
	}
	/**
	 * @param withdrawId the withdrawId to set
	 */
	public void setWithdrawId(long withdrawId) {
		this.withdrawId = withdrawId;
	}
	/**
	 * @return the addedDate
	 */
	public Date getAddedDate() {
		return addedDate;
	}
	/**
	 * @param addedDate the addedDate to set
	 */
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	public WithdrawMCashRequest() {}
	public WithdrawMCashRequest(String userName, String transactionId, String accountNumber, String beneficiaryId,
			String beneficiaryMessage, String beneficiaryPin, String companyName, String paidBy, Date paidDate,
			String sendMessage, BigDecimal txnAmount, String txnPin) {
		super();
		this.userName = userName;
		this.transactionId = transactionId;
		this.accountNumber = accountNumber;
		this.beneficiaryId = beneficiaryId;
		this.beneficiaryMessage = beneficiaryMessage;
		this.beneficiaryPin = beneficiaryPin;
		this.companyName = companyName;
		this.paidBy = paidBy;
		this.paidDate = paidDate;
		this.sendMessage = sendMessage;
		this.txnAmount = txnAmount;
		this.txnPin = txnPin;
	}
	@Override
	public String toString() {
		return "WithdrawMCashRequest [userName=" + userName + ", transactionId=" + transactionId + ", accountNumber="
				+ accountNumber + ", beneficiaryId=" + beneficiaryId + ", beneficiaryMessage=" + beneficiaryMessage
				+ ", beneficiaryPin=" + beneficiaryPin + ", companyName=" + companyName + ", paidBy=" + paidBy
				+ ", paidDate=" + paidDate + ", sendMessage=" + sendMessage + ", txnAmount=" + txnAmount + ", txnPin="
				+ txnPin + "]";
	}
	

}
