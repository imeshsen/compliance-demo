package lk.sampath.oc.Transfers.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetCardSerialRequest {
	
	private static final long serialVersionUID = 1857071452953105353L;
	
	@JsonProperty("Cardnumber")
	private String cardNumber;
	@JsonProperty("RequestTime")
	private String requestTime;
	
}
