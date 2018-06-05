package net.personal.service.interfaces;

import net.personal.pojo.MailConfig;
import net.personal.pojo.MailTemplet;

public interface AuthenticationService {
	public MailTemplet getMailTemplet();

	public MailConfig getMailConfig();
}
