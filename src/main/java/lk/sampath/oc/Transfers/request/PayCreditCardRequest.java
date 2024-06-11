/**
 * 
 */
package lk.sampath.oc.Transfers.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lk.sampath.oc.Transfers.Enums.TransactionType;
import lombok.Data;
import lombok.ToString;

/**
 * @author hrsupun
 *
 */
@Data
@ToString
public class PayCreditCardRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String vishwaUserId;
	private Long cardSerialNumber;
	private String debitAccountNumber;
	private BigDecimal amount;
	private TransactionType transactionType;
	private String paymentCurrency;
	private Double initiatedSerno;
	private String initiatedKey;
	private Double chainSerno;
	private String chainAuth;
	private String deviceId;
	private String IdentityType;
	private String NIC;
	
}
