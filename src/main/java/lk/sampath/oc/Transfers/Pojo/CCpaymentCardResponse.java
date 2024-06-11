package lk.sampath.oc.Transfers.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CCpaymentCardResponse {

	private static final long serialVersionUID = 1L;

	private ResponseHeader responseHeader;
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
