package lk.sampath.oc.Transfers.Pojo.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetTransfersAppResponse implements Serializable {

    private AppResponseHeader responseHeader;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AppTransferData> tranList;

    private long totalCount;
    private int pageNo;
    private int pageLimit;


}
