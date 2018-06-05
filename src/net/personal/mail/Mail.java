package net.personal.mail;

import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	private ArrayList<InternetAddress> recipients;
	private ArrayList<InternetAddress> ccs;
	private ArrayList<InternetAddress> bccs;
	private String title;
	private String content;
	private String charset = "UTF-8";
	private String type = "text/html;charset=" + charset;

	private Mail() {
		recipients = new ArrayList<>();
		ccs = new ArrayList<>();
		bccs = new ArrayList<>();
		title = "";
		content = "";
	}

	public Mail(String title, String content) {
		this();
		this.title = title;
		this.content = content;
	}

	public void setTitleCharset(String charset) {
		this.charset = charset;
	}

	public void setContentType(String type) {
		this.type = type;
	}

	public void addRecipients(InternetAddress recipient) {
		recipients.add(recipient);
	}

	public void addCc(InternetAddress cc) {
		ccs.add(cc);
	}

	public void addBcc(InternetAddress bcc) {
		bccs.add(bcc);
	}

	public Address[] getRecipients() {
		Address to[] = new Address[recipients.size()];
		to = recipients.toArray(to);
		return to;
	}

	public Address[] getCcs() {
		Address cc[] = new Address[ccs.size()];
		cc = ccs.toArray(cc);
		return cc;
	}

	public Address[] getBccs() {
		Address bcc[] = new Address[bccs.size()];
		bcc = bccs.toArray(bcc);
		return bcc;
	}

	public void setTitle(MimeMessage message) throws MessagingException {
		message.setSubject(title, charset);
	}

	public void setContent(MimeMessage message) throws MessagingException {
		message.setContent(content, type);
	}
}
