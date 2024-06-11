package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CapexPayLoad {

    private Long code;
    private Long subCode;
    private String message;
    private String messageCustomer;
    private String requestId;
    private Double chainSerno;
    private String chainAuth;
}
