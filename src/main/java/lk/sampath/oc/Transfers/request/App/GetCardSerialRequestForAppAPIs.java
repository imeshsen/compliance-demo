package lk.sampath.oc.Transfers.request.App;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCardSerialRequestForAppAPIs {
	
	private static final long serialVersionUID = 1857071452953105353L;

	@JsonProperty("RequestTime")
	private String requestTime;
	@JsonProperty("EncryptedCardnumber")
	private String cardNumber;
	@JsonProperty("EncryptionType")
	private String encryptionType;
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
