package net.personal.controller;

import java.io.IOException;
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
import net.personal.pojo.SampleUser;
import net.personal.pojo.User;
import net.personal.service.interfaces.LoginService;
import net.personal.staticData.WebDictionary;

@Controller
public class LoginController {

	@Resource
	LoginService loginService;

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

	@RequestMapping("signup")
	public void signup(@RequestBody SampleUser user, HttpServletResponse response, HttpSession session)
			throws IOException {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		if (loginService.register(user, time)) {
			login(user, response, session);
			return;
		}
		sendFalse(response);
	}

	@RequestMapping("signupCheck")
	public void signupCheck(String phone, @RequestBody String param, HttpServletResponse response, HttpSession session)
			throws IOException {
		if (phone == null)
			phone = ((JSONObject) (JSONObject.parse(URLDecoder.decode(param, "utf8"))))
					.getString(WebDictionary.PHONE_FIELD);
		JSONHelper.sendJSONBoolean(loginService.registerCheck(phone), response);
	}

	private void loginSuccessfully(User user, HttpServletResponse response, HttpSession session) throws IOException {
		session.setAttribute(WebDictionary.USER_FIELD, user);
		JSONObject json = new JSONObject();
		json.put(WebDictionary.STATUS_FIELD, true);
		json.put(WebDictionary.PHONE_FIELD, user.getPhone());
		json.put(WebDictionary.STATE_FIELD, user.getState());
		JSONHelper.sendJSON(json, response);
	}

	private void sendFalse(HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		json.put(WebDictionary.STATUS_FIELD, false);
		JSONHelper.sendJSON(json, response);
	}
}
