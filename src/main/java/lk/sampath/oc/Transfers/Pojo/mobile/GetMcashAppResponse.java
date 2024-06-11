package lk.sampath.oc.Transfers.Pojo.mobile;



import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetMcashAppResponse implements Serializable {

    private AppResponseHeader responseHeader;

    private List<AppMcashData> tranList;

    private long totalCount;
    private int pageNo;
    private int pageLimit;


}
