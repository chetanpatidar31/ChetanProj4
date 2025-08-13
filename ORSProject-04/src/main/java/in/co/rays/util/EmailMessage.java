package in.co.rays.util;

/**
 * Contains Email Message
 *
 * @author Chetan Patidar
 * @version 1.0
 * @Copyright (c) Chetan Patidar
 *
 */
public class EmailMessage {

	/**
	 * Contains comma separated TO addresses
	 */
	private String to;
	/**
	 * Contains message subject
	 */
	private String subject;
	/**
	 * Contains message
	 */
	private String message;

	/**
	 * Type of message whether it is Html or text, default is Text
	 */
	private int messageType = TEXT_MSG;

	public static final int HTML_MSG = 1;
	public static final int TEXT_MSG = 2;

	public EmailMessage() {
	}

	public EmailMessage(String to, String subject, String message) {
		this.to = to;
		this.subject = subject;
		this.message = message;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getMessageType() {
		return messageType;
	}

}
