package lk.sampath.oc.Transfers.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCardSerailResponse {
	
	private static final long serialVersionUID = -3939097028077198401L;

	private ResponseHeader responseHeader;
	@JsonProperty("Serno")
	private Long serno;
	@JsonProperty("Code")
	private Long code;
	@JsonProperty("SubCode")
	private Long subCode;
	@JsonProperty("Message")
	private String message;
	@JsonProperty("MessageQueue")
	private String messageQueue;
	@JsonProperty("MessageCustomer")
	private String messageCustomer;
	@JsonProperty("RequestID")
	private String requestId;
	@JsonProperty("RequestReference")
	private String requestReference;
}
