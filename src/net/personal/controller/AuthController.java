package net.personal.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

import net.personal.controller.helper.JSONHelper;
import net.personal.mail.tool.AuthenticationCode;
import net.personal.mail.tool.MailIdentifier;
import net.personal.pojo.User;
import net.personal.staticData.WebDictionary;

@Controller
public class AuthController {

	@Resource
	private MailIdentifier mailIdentifier;

	@RequestMapping(path = "testMailAuth", method = RequestMethod.GET)
	public void testMailAuth(String mail, HttpServletResponse response, HttpSession session) throws IOException {
		requireMailAuth(WebDictionary.REGISTER_FILED, mail, response, session);
		String code = session.getAttribute(WebDictionary.AUTHENTICATIONCODE).toString();
		JSONHelper.sendJSON(code, response);
	}

	@RequestMapping(path = "requireMailAuthForReg", method = RequestMethod.POST)
	public void requireMailAuthForRegister(@RequestBody String mail, HttpServletResponse response,
			HttpSession session) {
		requireMailAuth(WebDictionary.REGISTER_FILED, mail, response, session);
	}

	@RequestMapping(path = "requireMailAuthForLog", method = RequestMethod.POST)
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
				JSONHelper.sendJSONBoolean(true, response);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		JSONObject json = new JSONObject();
		json.put(WebDictionary.STATUS_FIELD, false);
		json.put(WebDictionary.MESSAGE_FILED, "邮箱有误，请重新输入");
		try {
			JSONHelper.sendJSON(json, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
