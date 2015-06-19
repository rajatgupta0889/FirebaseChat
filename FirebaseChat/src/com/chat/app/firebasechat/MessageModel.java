package com.chat.app.firebasechat;

public class MessageModel {
	String message, remainingDay, newMessage, userName, userImageUrl, userId;

	public MessageModel(String message, String remainingDay, String newMessage,
			String userName, String userImageUrl, String userId) {
		super();
		this.message = message;
		this.remainingDay = remainingDay;
		this.newMessage = newMessage;
		this.userName = userName;
		this.userImageUrl = userImageUrl;
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRemainingDay() {
		return remainingDay;
	}

	public void setRemainingDay(String remainingDay) {
		this.remainingDay = remainingDay;
	}

	public String getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
