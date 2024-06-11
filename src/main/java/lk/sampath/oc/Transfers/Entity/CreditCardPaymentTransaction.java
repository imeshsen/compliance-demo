/**
 * 
 */
package lk.sampath.oc.Transfers.Entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hrsupun
 *
 */
@Getter
@Setter
@Entity
@Table(name="OC_TRN_CREDITCARD_PMT")
public class CreditCardPaymentTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TXN_PAY_ID")
	@SequenceGenerator(name = "TXN_PAY_ID", sequenceName = "SEQ_TRANSACTION_ID", allocationSize=1)
	@Column(name="CC_PAY_ID")
	private long ccPaymentId;
	
	@Column(name="VISHWA_USER_ID")
	private String vishwaUserId;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADDED_DATE")
	private Date addedDate;
	
	@Column(name = "CARD_SERIAL")
	private Long cardSerialNumber;
	
	@Column(name = "DEBIT_ACCOUNT")
	private String debitAccountNumber;
	
	@Column(name = "PAYMENT_AMOUNT")
	private BigDecimal amount;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "CURRENCY")
	private String currency;
	
	@Column(name = "CARD_NUMBER")
	private String cardNumber;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	private InvokeStatus status;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TRANSFER_METHOD")
	private TransactionType transferMethod;

	public CreditCardPaymentTransaction(String vishwaUserId, Long cardSerialNumber, String debitAccountNumber,
			BigDecimal amount, String remark, InvokeStatus status, TransactionType transferMethod, String currency,String cardnumber) {
		super();
		this.vishwaUserId = vishwaUserId;
		this.cardSerialNumber = cardSerialNumber;
		this.debitAccountNumber = debitAccountNumber;
		this.amount = amount;
		this.remark = remark;
		this.status = status;
		this.transferMethod = transferMethod;
		this.currency = currency;
		this.cardNumber = cardnumber;
	}

	public CreditCardPaymentTransaction() {
		// TODO Auto-generated constructor stub
	}
}
