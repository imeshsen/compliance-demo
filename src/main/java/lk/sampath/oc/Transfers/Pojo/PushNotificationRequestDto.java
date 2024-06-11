package lk.sampath.oc.Transfers.Pojo;

import java.io.Serializable;

public class PushNotificationRequestDto implements Serializable {

	private static final long serialVersionUID = -6561989839789206273L;

	private String ocUserId;
	private String deviceId;
	private String username;
	private String topic;
	private String content;
	private String notificationDate;
	private String status;

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(String notificationDate) {
		this.notificationDate = notificationDate;
	}

	public String getOcUserId() {
		return ocUserId;
	}

	public void setOcUserId(String ocUserId) {
		this.ocUserId = ocUserId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
