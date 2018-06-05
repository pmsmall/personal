package net.personal.mail.service;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

import net.personal.mail.Mail;
import net.personal.mail.MailBox;

public class SMTPService {

	private SMTPService() {
	}

	public static MailBox getMailBox(String user, String password, String host) {
		return new SMTPMailBox(user, password, host);
	}

	private static class SMTPMailBox implements MailBox {
		private String host;
		private String user;
		private transient String password;
		Properties props = null;
		Session session = null;
		boolean SSLOn = false;
		int sslPort;
		String SSL_FACTORY;

		public SMTPMailBox(String user, String password, String host) {
			this.user = user;
			this.password = password;
			this.host = host;
		}

		public MailBox openSSL(int port) {
			return openSSL("javax.net.ssl.SSLSocketFactory", port);
		}

		@Override
		public MailBox openSSL(String SSL_FACTORY, int port) {
			SSLOn = true;
			sslPort = port;
			this.SSL_FACTORY = SSL_FACTORY;
			return this;
		}

		public MailBox closeSSL() {
			SSLOn = false;
			return this;
		}

		public Session getMailSession() throws GeneralSecurityException {
			if (props == null) {
				props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", host);
				props.put("mail.user", user);
				// props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.password", password);
				if (SSLOn) {
					props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
					props.put("mail.smtp.socketFactory.fallback", "false");
					props.put("mail.smtp.socketFactory.port", sslPort);
					props.put("mail.smtp.port", sslPort); // google使用465或587端口
					MailSSLSocketFactory sf = new MailSSLSocketFactory();
					sf.setTrustAllHosts(true);
					props.put("mail.smtp.ssl.socketFactory", sf);
				}
				Authenticator authenticator = new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, password);
					}
				};

				session = Session.getInstance(props, authenticator);
			}
			return session;
		}

		public String getOwner() {
			return user;
		}

		public void send(Mail mail) throws MessagingException, GeneralSecurityException {
			MimeMessage message = new MimeMessage(getMailSession());
			InternetAddress from = new InternetAddress(getOwner());
			message.setFrom(from);

			message.setRecipients(RecipientType.TO, mail.getRecipients());

			message.setRecipients(RecipientType.CC, mail.getCcs());

			message.setRecipients(RecipientType.BCC, mail.getBccs());

			mail.setTitle(message);
			mail.setContent(message);

			Transport.send(message);
		}

	}
}
