package lk.sampath.oc.Transfers.Pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetCardNumberResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3939097028077198401L;

	
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
	@JsonProperty("CardNumber")
	private String cardNumber;
	@JsonProperty("CardTokenID")
	private String cardTokenId;
	@JsonProperty("RequestReference")
	private String requestReference;
	
}
