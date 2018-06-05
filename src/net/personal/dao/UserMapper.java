package net.personal.dao;

import net.personal.pojo.SampleUser;
import net.personal.pojo.User;

public interface UserMapper {
	public User selectUserSamply(SampleUser user);

	public int selectPhone(String phone);

	public int insertUserSamply(SampleUser user);
}
