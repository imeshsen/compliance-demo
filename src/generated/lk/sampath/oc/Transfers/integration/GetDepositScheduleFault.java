
package lk.sampath.oc.Transfers.integration;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.2
 * 2019-05-06T11:41:02.027+05:30
 * Generated source version: 3.0.2
 */

@WebFault(name = "ExecutionException", targetNamespace = "http://www.sampath.lk/SD/IIBFinacleIntegration/")
public class GetDepositScheduleFault extends Exception {
    
    private lk.sampath.oc.Transfers.integration.ExecutionExceptionType executionException;

    public GetDepositScheduleFault() {
        super();
    }
    
    public GetDepositScheduleFault(String message) {
        super(message);
    }
    
    public GetDepositScheduleFault(String message, Throwable cause) {
        super(message, cause);
    }

    public GetDepositScheduleFault(String message, lk.sampath.oc.Transfers.integration.ExecutionExceptionType executionException) {
        super(message);
        this.executionException = executionException;
    }

    public GetDepositScheduleFault(String message, lk.sampath.oc.Transfers.integration.ExecutionExceptionType executionException, Throwable cause) {
        super(message, cause);
        this.executionException = executionException;
    }

    public lk.sampath.oc.Transfers.integration.ExecutionExceptionType getFaultInfo() {
        return this.executionException;
    }
}
