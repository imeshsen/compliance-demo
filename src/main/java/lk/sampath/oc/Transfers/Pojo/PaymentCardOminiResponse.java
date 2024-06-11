/**
 * 
 */
package lk.sampath.oc.Transfers.Pojo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * @author hrsupun
 *
 */
@Data
@ToString
public class PaymentCardOminiResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long code;
	private Long subCode;
	private String message;
	private String messageCustomer;
	private String requestId;
	
	
	public PaymentCardOminiResponse(PaymentCardResponse response) {
		this.code = response.getCode();
		this.subCode = response.getSubCode();
		this.message = response.getMessage();
		this.messageCustomer = response.getMessageCustomer();
		this.requestId = response.getRequestId();
	}


	public PaymentCardOminiResponse() {
		
	}
}
