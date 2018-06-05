package net.personal.mail.test;

import java.sql.Timestamp;

import javax.mail.internet.InternetAddress;

import net.personal.mail.Mail;
import net.personal.mail.MailBox;
import net.personal.mail.MailDevice;
import net.personal.mail.service.SMTPService;

public class Main {

	public static void main(String[] args) throws Exception {
		MailBox mailBox = SMTPService.getMailBox("547277206@qq.com", "rcltfmdpxzbgbbea", "smtp.qq.com").openSSL(465);
		MailDevice device = new MailDevice(mailBox);
		long startTime = System.currentTimeMillis();
//		for (int i = 0; i < 20; i++) {
			Mail mail = new Mail("验证", new Timestamp(System.currentTimeMillis()) + "此邮件是验证，请勿回复");
			mail.addRecipients(new InternetAddress("547277206@qq.com"));
			device.send(mail);
//		}
		long endTime = System.currentTimeMillis();
		System.out.println("succussfully in " + (endTime - startTime) + "ms");
	}

}
