package net.personal.dao;

import net.personal.pojo.MailConfig;
import net.personal.pojo.MailTemplet;

public interface MailAuthMapper {
	public MailTemplet selectFirstMailTemplet();

	public MailConfig selectFirstMailConfig();
}
