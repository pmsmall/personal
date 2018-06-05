package net.personal.mail.tool;

public interface MailIdentifier {
	public AuthenticationCode getAuthenticationCode(String mailAddress) throws Exception;
}
