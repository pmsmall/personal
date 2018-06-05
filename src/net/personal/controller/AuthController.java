package net.personal.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import net.personal.mail.tool.AuthenticationCode;
import net.personal.mail.tool.MailIdentifier;
import net.personal.pojo.User;
import net.personal.staticData.WebDictionary;

@Controller
public class AuthController {

	@Resource
	private MailIdentifier mailIdentifier;

	@RequestMapping("requireMailAuthForReg")
	public void requireMailAuthForRegister(@RequestBody String mail, HttpServletResponse response,
			HttpSession session) {
		requireMailAuth(WebDictionary.REGISTER_FILED, mail, response, session);
	}

	@RequestMapping("requireMailAuthForLog")
	public void requireMailAuthForLogin(@RequestBody String mail, HttpServletResponse response, HttpSession session) {
		requireMailAuth(WebDictionary.LOGIN_FILED, mail, response, session);
	}

	private void requireMailAuth(String reason, String mail, HttpServletResponse response, HttpSession session) {
		if (User.validateMail(mail))
			try {
				AuthenticationCode code = mailIdentifier.getAuthenticationCode(mail);
				session.setAttribute(WebDictionary.AUTHENTICATIONCODE, code);
				session.setAttribute(WebDictionary.REASON_FILED, reason);
				session.setAttribute(WebDictionary.MAIL_FIELD, mail);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		JSONObject json = new JSONObject();
		json.put(WebDictionary.STATUS_FIELD, false);
		json.put(WebDictionary.MESSAGE_FILED, "邮箱有误，请重新输入");
	}
}
