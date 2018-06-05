package net.personal.pojo;

import java.util.concurrent.ConcurrentHashMap;

public class MailTemplet {
	private String title;
	private String content;
	private ConcurrentHashMap<String, String[]> fragments = new ConcurrentHashMap<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public String getContent(String flag, String placement) {
		String[] fragment = fragments.get(flag);
		if (fragment == null) {
			fragment = content.split(flag);
			fragments.put(flag, fragment);
		}
		String content = "";
		int len = fragment.length;
		for (int i = 0; i < len; i++) {
			content += fragment[i] + placement;
		}
		if (len > 0)
			content += fragment[len];
		return content;
	}

	public void dispose() {
		fragments.clear();
	}

	public void setContent(String content) {
		this.content = content;
	}

}
