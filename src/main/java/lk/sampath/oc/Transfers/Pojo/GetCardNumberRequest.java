package lk.sampath.oc.Transfers.Pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetCardNumberRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1857071452953105353L;
	
	@JsonProperty("CardSerno")
	private Long cardSerialNumber;
	@JsonProperty("RequestTime")
	private String requestTime;
	
	

}
