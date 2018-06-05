package net.personal.mail;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.InternetAddress;

public class MailDevice {
	private MailBox defaultMailBox;
	private ThreadPoolExecutor pool;
	private static int corePoolSize = 3;
	private static int maximumPoolSize = 50;
	private static int keepAliveTime = 3;
	private static TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;

	public MailDevice(MailBox defaultMailBox) {
		this(defaultMailBox, new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeUnit,
				new LinkedBlockingQueue<>()));
	}

	public MailDevice(MailBox defaultMailBox, ThreadPoolExecutor pool) {
		this.pool = pool;
		this.defaultMailBox = defaultMailBox;
	}

	public void send(Mail mail) {
		pool.submit(new Runnable() {

			@Override
			public void run() {
				try {
					defaultMailBox.send(mail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void send(String title, String content, String to) {
		pool.submit(new Runnable() {

			@Override
			public void run() {
				try {
					Mail mail = new Mail(title, content);
					mail.addRecipients(new InternetAddress(to));
					defaultMailBox.send(mail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void send(Mail mail, String to) {
		pool.submit(new Runnable() {

			@Override
			public void run() {
				try {
					mail.addRecipients(new InternetAddress(to));
					defaultMailBox.send(mail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
