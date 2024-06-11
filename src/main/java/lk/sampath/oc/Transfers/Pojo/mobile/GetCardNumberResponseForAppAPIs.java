package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class GetCardNumberResponseForAppAPIs implements Serializable{

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
//	@JsonProperty("RequestReference")
//	private String requestReference;
	@JsonProperty("ChainSerno")
	private Double chainSerno;
	@JsonProperty("ChainAuth")
	private String chainAuth;
	@JsonProperty("SingleUseChainDataList")
	private List<String> singleUseChainDataList;

	@Override
	public String toString() {
		return "GetCardNumberResponseForNewAPIs{" +
				"code=" + code +
				", subCode=" + subCode +
				", message='" + message + '\'' +
				", messageCustomer='" + messageCustomer + '\'' +
				", requestId='" + requestId + '\'' +
				", cardNumber='" + cardNumber + '\'' +
				", cardTokenId='" + cardTokenId + '\'' +
				", chainSerno=" + chainSerno +
				", chainAuth='" + chainAuth + '\'' +
				", singleUseChainDataList=" + singleUseChainDataList +
				'}';
	}
	
}
