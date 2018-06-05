package net.personal.service.interfaces;

import java.sql.Timestamp;

import net.personal.pojo.SampleUser;
import net.personal.pojo.User;

public interface LoginService {
	public User login(SampleUser user, Timestamp time);

	public boolean registerCheck(String phone);

	public boolean register(SampleUser user, Timestamp time);

	public User getLoginedUser(SampleUser user);

	public User getLoginedUserByMail(String mail);
}
