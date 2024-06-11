package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentCardResponseForAppAPIs implements Serializable {

    private static final long serialVersionUID = 1L;

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
