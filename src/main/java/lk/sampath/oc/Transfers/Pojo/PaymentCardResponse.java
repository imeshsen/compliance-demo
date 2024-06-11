/**
 * 
 */
package lk.sampath.oc.Transfers.Pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

/**
 * @author hrsupun
 *
 */
@Data
@ToString
public class PaymentCardResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("Code")
	private Long code;
	@JsonProperty("SubCode")
	private Long subCode;
	@JsonProperty("Message")
	private String message;
	@JsonProperty("MessageCustomer")
	private String messageCustomer;
	@JsonProperty("RequestID")
	private String requestId;
	
}
