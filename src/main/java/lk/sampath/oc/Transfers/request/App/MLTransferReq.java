package lk.sampath.oc.Transfers.request.App;

import lombok.Data;

import java.util.List;

@Data
public class MLTransferReq {

    private String debitAccount;
    private String currency;
    private List<MLTranCategorySingleReq> tranList;
}
