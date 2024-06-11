package lk.sampath.oc.Transfers.Pojo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentCCResponse  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8647813718400347152L;
	
	
	
	private Long code;
	private Long subCode;
	private String message;
	private String messageCustomer;
	private String requestId;
	private Long reference;
	
}
