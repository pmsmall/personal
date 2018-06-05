package net.personal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.personal.dao.MailAuthMapper;
import net.personal.pojo.MailConfig;
import net.personal.pojo.MailTemplet;
import net.personal.service.interfaces.AuthenticationService;

@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private MailAuthMapper mailAuthMapper;

	@Override
	public MailTemplet getMailTemplet() {
		return mailAuthMapper.selectFirstMailTemplet();
	}

	@Override
	public MailConfig getMailConfig() {
		return mailAuthMapper.selectFirstMailConfig();
	}

}
