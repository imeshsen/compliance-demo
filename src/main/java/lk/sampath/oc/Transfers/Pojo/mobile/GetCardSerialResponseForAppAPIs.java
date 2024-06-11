package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCardSerialResponseForAppAPIs {
	
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
//	@JsonProperty("MessageQueue")
//	private String messageQueue;
	@JsonProperty("MessageCustomer")
	private String messageCustomer;
	@JsonProperty("RequestID")
	private String requestId;
//	@JsonProperty("RequestReference")
//	private String requestReference;
	@JsonProperty("ChainSerno")
	private Double chainSerno;
	@JsonProperty("ChainAuth")
	private String chainAuth;
	@JsonProperty("SingleUseChainDataList")
	private List<String> singleUseChainDataList;
}

