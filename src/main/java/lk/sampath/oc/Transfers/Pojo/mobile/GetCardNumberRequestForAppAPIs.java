package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class GetCardNumberRequestForAppAPIs implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1857071452953105353L;

	@JsonProperty("RequestTime")
	private String requestTime;
	@JsonProperty("CardSerno")
	private Long cardSerialNumber;
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
}
