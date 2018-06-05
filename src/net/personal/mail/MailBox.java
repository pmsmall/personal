package net.personal.mail;

import javax.mail.Session;

public interface MailBox {

	public MailBox openSSL(String SSL_FACTORY, int port);

	public MailBox openSSL(int port);

	public MailBox closeSSL();

	public Session getMailSession() throws Exception;

	public String getOwner();

	public void send(Mail mail) throws Exception;
}
