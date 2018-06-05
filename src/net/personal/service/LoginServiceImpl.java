package net.personal.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.personal.dao.UserMapper;
import net.personal.pojo.SampleUser;
import net.personal.pojo.User;
import net.personal.service.interfaces.LoginService;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	UserMapper userMapper;

	@Override
	public User login(SampleUser user, Timestamp time) {
		User loginUser = null;
		if (User.validateUser(user))
			loginUser = userMapper.selectUserSamply(user);
		return loginUser;
	}

	@Override
	public boolean registerCheck(String mail) {
		return userMapper.selectMail(mail) == 0;
	}

	@Override
	public boolean register(SampleUser user, Timestamp time) {
		if (User.validateUser(user))
			return userMapper.insertUserSamply(user) == 1;
		return false;
	}

	@Override
	public User getLoginedUser(SampleUser user) {
		return userMapper.selectUserSamply(user);
	}

	@Override
	public User getLoginedUserByMail(String mail) {
		return userMapper.selectUserByMail(mail);
	}

}
