package net.personal.mail.tool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Service;

import net.personal.mail.Mail;
import net.personal.mail.MailBox;
import net.personal.mail.MailDevice;
import net.personal.mail.service.SMTPService;
import net.personal.pojo.MailConfig;
import net.personal.pojo.MailTemplet;
import net.personal.service.interfaces.AuthenticationService;

@Service("mailIdentifier")
public class AsynchronousMailIdentifier implements MailIdentifier {
	private MailBox mailBox;
	private MailDevice device;

	@Resource
	private AuthenticationService authenticationService;

	public AsynchronousMailIdentifier() {
		if (authenticationService != null)
			init();
		else {
			mailBox = null;
			device = null;
		}
	}

	@PostConstruct
	public void init() {
		MailConfig config = authenticationService.getMailConfig();
		mailBox = SMTPService.getMailBox(config.getUser(), config.getPassword(), config.getHost());
		if (config.isUseSSL())
			mailBox.openSSL(config.getSSLPort());
		device = new MailDevice(mailBox, config.getCorePoolSize(), config.getMaximumPoolSize(),
				config.getKeepAliveTime(), config.getUnit());
	}

	private String getAuthenticationCode() {
		String code = "";
		for (int i = 0; i < 6; i++) {
			code += (int) (Math.random() * 10);
		}
		return code;
	}

	public AuthenticationCode getAuthenticationCode(String mailAddress) throws AddressException {
		String code = getAuthenticationCode();
		MailTemplet templet = authenticationService.getMailTemplet();
		Mail mail = new Mail(templet.getTitle(), templet.getContent("\\{\\{code\\}\\}", code));
		mail.addRecipients(new InternetAddress(mailAddress));
		device.send(mail);
		return new MailAuthenticationCode(code);
	}

	private class MailAuthenticationCode implements AuthenticationCode {
		private String code;

		MailAuthenticationCode(String code) {
			this.code = code;
		}

		@Override
		public boolean verify(String code) {
			return this.code.equals(code);
		}

		@Override
		public String toString() {
			return code;
		}
	}

}
