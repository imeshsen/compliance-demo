package lk.sampath.oc.Transfers.Pojo;

public class CommonApplicationResponse {

    private String errorMessage;

    private int status;

    private Object payload;

    public CommonApplicationResponse() {

    }

    public CommonApplicationResponse(int status, String errorMessage) {
	this.status = status;
	this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public Object getPayload() {
	return payload;
    }

    public void setPayload(Object payload) {
	this.payload = payload;
    }

}
