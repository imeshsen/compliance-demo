package lk.sampath.oc.Transfers.Pojo.mobile;


import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lombok.Data;

import java.io.Serializable;


@Data
public class GetTransfersDataResponse implements Serializable {

    private AppResponseHeader responseHeader;

    private AppTransferDetail transferData;



}
