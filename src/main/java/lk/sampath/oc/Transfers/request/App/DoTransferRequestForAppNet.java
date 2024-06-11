/**
 * 
 */
package lk.sampath.oc.Transfers.request.App;

import lk.sampath.oc.Transfers.Enums.CommissionStatus;
import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Enums.TransactionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hrsupun
 *
 */
@Data
public class DoTransferRequestForAppNet implements Serializable {

	private static final long serialVersionUID = 1L;

	private long transactionId;
	private String externalId;
	private String fromAccountNumber;
	private String toAccountNumber;
	private String cardName;
	private String bankName;
	private String bankCode;
	private String branchCode;
	private TransactionType transactionType;
	private BigDecimal transactionAmount;
	private Date requestedDate;
	private String status;
	private String channel;
	private TransactionCategory transactionCategory;
	private String benificiaryRemarks;
	private String accountNickName;
	private String senderRemarks;
	private String currency;
	private String backendRefNumber;
	private String cdciStatus;
	private String backendResponse;
	private String mobileCashSenderNIC;
	private String mobileCashSenderMobile;
	private String mobileRecipientMobile;
	private String mobileRecipientName;
	private String mobileRecipientNIC;
	private String transferFrequency;
	private Date nextSchedule;
	private long numOfTransfers;
	private String scheduleId;
	private String toAccountName;
	private String fromAccountName;
	private String commissionFromAccountNumber;
	private BigDecimal commissionAmount;
	private CommissionStatus commissionStatus;
	private String purpose;
	private String scheduleOccr;
//GETCARDSERIALFROMCARDNUMBERFOROMNIMOBILE request
	 // ==> card number
	private Double initiatedSerno;
	private String initiatedKey;
	private Double chainSerno;
	private String chainAuth;
	private String deviceId;
}
