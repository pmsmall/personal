package net.personal.mail.tool;

public interface AuthenticationCode {
	public boolean verify(String code);
}
