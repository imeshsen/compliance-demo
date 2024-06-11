package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CCpaymentCardResponseForAppAPIs {

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
	@JsonProperty("RequestReference")
	private String requestReference;
	@JsonProperty("ChainSerno")
	private Double chainSerno;
	@JsonProperty("ChainAuth")
	private String chainAuth;
}
