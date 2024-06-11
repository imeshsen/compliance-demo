/**
 * 
 */
package lk.sampath.oc.Transfers.request;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author hrsupun
 *
 */
@Data
@ToString
@AllArgsConstructor
public class SampathCCPaymentRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("CardSerno")
	private Long cardSerialNumber;
	@JsonProperty("FinacleAccountNumber")
	private String debitAccountNumber;
	@JsonProperty("Amount")
	private BigDecimal amount;
	@JsonProperty("RequestTime")
	private String requestTime;
	
	
}
