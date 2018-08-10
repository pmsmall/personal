package net.personal.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import net.personal.controller.helper.JSONHelper;
import net.personal.mail.tool.AuthenticationCode;
import net.personal.pojo.SampleUser;
import net.personal.pojo.User;
import net.personal.pojo.UserInfo;
import net.personal.service.interfaces.LoginService;
import net.personal.service.interfaces.UserService;
import net.personal.staticData.WebDictionary;

@Controller
public class LoginController {

	@Resource
	LoginService loginService;

	@Resource
	UserService userService;

	@RequestMapping("loginCheck")
	public void login(@RequestBody(required = false) SampleUser user, HttpServletResponse response, HttpSession session)
			throws IOException {
		if (user != null) {
			Timestamp time = new Timestamp(System.currentTimeMillis());
			User loginUser = loginService.login(user, time);
			if (loginUser != null) {
				loginSuccessfully(loginUser, response, session);
				return;
			}
		}
		sendFalse(response);
	}

	@RequestMapping("loginCheckMail")
	public void loginByMail(@RequestBody String param, HttpServletResponse response, HttpSession session)
			throws IOException {
		String mail = verifyCode(WebDictionary.LOGIN_FILED, param, session);
		if (mail != null) {
			User loginUser = loginService.getLoginedUserByMail(mail);
			loginSuccessfully(loginUser, response, session);
		} else
			sendFalse(response);
	}

	@RequestMapping("signup")
	public void signup(@RequestBody String param, HttpServletResponse response, HttpSession session)
			throws IOException {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		JSONObject require = JSONObject.parseObject(URLDecoder.decode(param, "utf8"), JSONObject.class);
		String mail = verifyCode(WebDictionary.REGISTER_FILED, require, session);
		if (mail != null) {
			SampleUser user = new SampleUser();
			user.setMail(mail);
			user.setPassword(require.getString(WebDictionary.PASSWORD_FIELD));
			if (loginService.register(user, time)) {
				login(user, response, session);
				return;
			}
		}
		sendFalse(response);
	}

	@RequestMapping("signupCheck")
	public void signupCheck(String mail, @RequestBody String param, HttpServletResponse response, HttpSession session)
			throws IOException {
		if (mail == null)
			mail = ((JSONObject) (JSONObject.parse(URLDecoder.decode(param, "utf8"))))
					.getString(WebDictionary.MAIL_FIELD);
		JSONHelper.sendJSONBoolean(loginService.registerCheck(mail), response);
	}

	@RequestMapping("signupCheckCode")
	public void signupCheckCode(String reason, String authCode, String mail, @RequestBody String param,
			HttpServletResponse response, HttpSession session) throws IOException {
		if (reason == null && authCode == null) {
			JSONObject json = ((JSONObject) (JSONObject.parse(URLDecoder.decode(param, "utf8"))));
			reason = json.getString(WebDictionary.REASON_FILED);
			JSONHelper.sendJSONBoolean(verifyCode(reason, json, session) != null, response);
		} else {
			JSONHelper.sendJSONBoolean(verifyCode(mail, reason, authCode, session) != null, response);
		}

	}

	private String verifyCode(String reason, String jsonString, HttpSession session) {
		System.out.println(jsonString);
		JSONObject require;
		try {
			require = JSONObject.parseObject(URLDecoder.decode(jsonString, "utf8"), JSONObject.class);
			return verifyCode(reason, require, session);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String verifyCode(String reason, JSONObject require, HttpSession session) {
		return verifyCode(require.getString(WebDictionary.MAIL_FIELD), reason,
				require.getString(WebDictionary.AUTHENTICATIONCODE), session);
	}

	private String verifyCode(String mail, String reason, String code, HttpSession session) {
		String reason0 = (String) session.getAttribute(WebDictionary.REASON_FILED);
		if (reason.equals(reason0)) {
			String lastMail = (String) session.getAttribute(WebDictionary.MAIL_FIELD);
			AuthenticationCode code0 = (AuthenticationCode) session.getAttribute(WebDictionary.AUTHENTICATIONCODE);
			if (code0 == null)
				return null;
			if (lastMail != null && lastMail.equals(mail)) {
				if (code0.verify(code)) {
					return lastMail;
				}
			}
		}
		return null;
	}

	private void loginSuccessfully(User user, HttpServletResponse response, HttpSession session) throws IOException {
		session.setAttribute(WebDictionary.USER_FIELD, user);
		JSONObject json = new JSONObject();
		json.put(WebDictionary.STATUS_FIELD, true);
		json.put(WebDictionary.MAIL_FIELD, user.getMail());
		json.put(WebDictionary.STATE_FIELD, user.getState());
		UserInfo info = userService.lookUp(user.getId());
		info.setAuth(true);
		json.put(WebDictionary.USER_FIELD, info);
		JSONHelper.sendJSON(json, response);
	}

	private void sendFalse(HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		json.put(WebDictionary.STATUS_FIELD, false);
		JSONHelper.sendJSON(json, response);
	}
}
