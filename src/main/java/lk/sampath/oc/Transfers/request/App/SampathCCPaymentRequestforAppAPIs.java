/**
 * 
 */
package lk.sampath.oc.Transfers.request.App;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hrsupun
 *
 */
@Data
@ToString
@AllArgsConstructor
public class SampathCCPaymentRequestforAppAPIs implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("CardSerno")
	private Long cardSerialNumber;
	@JsonProperty("FinacleAccountNumber")
	private String debitAccountNumber;
	@JsonProperty("Amount")
	private BigDecimal amount;
	@JsonProperty("RequestTime")
	private String requestTime;
	@JsonProperty("InitiatedSerno")
	private Double initiatedSerno;
	@JsonProperty("InitiatedKey")
	private String initiatedKey;
	@JsonProperty("ChainSerno")
	private Double chainSerno;
	@JsonProperty("ChainAuth")
	private String chainAuth;
	@JsonProperty("DeviceId")
	private String deviceId;
	@JsonProperty("PaymentCurrency")
	private String paymentCurrency;
}
