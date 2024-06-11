package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lombok.Data;

@Data
public class MLTransferRes {

    private String authenticationType;
    private long transferReference;
    private int tranIndex;
    private String transferType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CapexPayLoad capexPayLoad;
    private ResponseHeader responseHeader;


}
