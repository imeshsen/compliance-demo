package lk.sampath.oc.Transfers.Entity;

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
@Table(name="OC_TRN_REVERSE_MCASH")
public class ReverseMCashRequest {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVERSE_ID")
	@SequenceGenerator(name = "REVERSE_ID", sequenceName = "SEQ_REVERSE_ID", allocationSize=1)
	@Column(name="REVERSE_ID")
	private long reverseId;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="TRANSACTION_ID")
	private String transactionId;
	
	@Column(name="BENEFICIARY_MESSAGE")
	private String beneficiaryMessage;
	
	@Column(name="COMPANY_NAME")
	private String companyName;
	
	@Column(name="SEND_MESSAGE")
	private String sendMessage;
	
	@Column(name="SENDER_ID")
	private String senderId;
	
	@Transient
	@Column(name="SENDER_PIN")
	private String senderPin;
	
	@Column(name="SUSPENDED_BY")
	private String suspendedBy;
	
	@Column(name="SUSPENDED_SOL")
	private String suspendedSol;
	
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
	public String getBeneficiaryMessage() {
		return beneficiaryMessage;
	}
	public void setBeneficiaryMessage(String beneficiaryMessage) {
		this.beneficiaryMessage = beneficiaryMessage;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderPin() {
		return senderPin;
	}
	public void setSenderPin(String senderPin) {
		this.senderPin = senderPin;
	}
	public String getSuspendedBy() {
		return suspendedBy;
	}
	public void setSuspendedBy(String suspendedBy) {
		this.suspendedBy = suspendedBy;
	}
	public String getSuspendedSol() {
		return suspendedSol;
	}
	public void setSuspendedSol(String suspendedSol) {
		this.suspendedSol = suspendedSol;
	}
	/**
	 * @return the reverseId
	 */
	public long getReverseId() {
		return reverseId;
	}
	/**
	 * @param reverseId the reverseId to set
	 */
	public void setReverseId(long reverseId) {
		this.reverseId = reverseId;
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

	@Override
	public String toString() {
		return "ReverseMCashRequest [userName=" + userName + ", transactionId=" + transactionId
				+ ", beneficiaryMessage=" + beneficiaryMessage + ", companyName=" + companyName + ", sendMessage="
				+ sendMessage + ", senderId=" + senderId + ", senderPin=" + senderPin + ", suspendedBy=" + suspendedBy
				+ ", addedDate=" + addedDate + ", reverseId=" + reverseId + ", suspendedSol=" + suspendedSol + "]";
	}
	public ReverseMCashRequest(String userName, String transactionId, String beneficiaryMessage, String companyName,
			String sendMessage, String senderId, String senderPin, String suspendedBy, String suspendedSol) {
		super();
		this.userName = userName;
		this.transactionId = transactionId;
		this.beneficiaryMessage = beneficiaryMessage;
		this.companyName = companyName;
		this.sendMessage = sendMessage;
		this.senderId = senderId;
		this.senderPin = senderPin;
		this.suspendedBy = suspendedBy;
		this.suspendedSol = suspendedSol;
	}
	public ReverseMCashRequest() {}
	
}
