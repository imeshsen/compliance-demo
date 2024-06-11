package lk.sampath.oc.Transfers.Pojo.mobile;

import lombok.Data;

import java.io.Serializable;
@Data
public class AppResponseHeader implements Serializable {
    private boolean statusCode;
    private String statusDescription;
    private String errorCode;
    private String errorDescription;

    public AppResponseHeader(boolean statusCode, String statusDescription, String errorCode, String errorDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
