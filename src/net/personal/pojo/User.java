package net.personal.pojo;

public class User extends SampleUser {
	private int id;
	private int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public static boolean validateUser(SampleUser user) {
		return validateMail(user.getMail()) && validatePassword(user.getPassword());
	}

	public static boolean validatePhone(String phone) {
		if (phone == null)
			return false;
		int len = phone.length();
		if (len == 0)
			return false;
		boolean result = false;
		for (int i = 0; i < len; i++) {
			char c = phone.charAt(i);
			if (c == '-') {
				if (result)
					result = false;
				else
					return false;
			} else if (c >= '0' && c <= '9') {
				if (!result)
					result = true;
			} else {
				return false;
			}
		}
		return true;
	}

	private static boolean validatePassword(String password) {
		if (password == null)
			return false;
		return true;
	}

	public static boolean validateMail(String mail) {
		int index = mail.indexOf("@");
		if (mail.indexOf("@", index + 1) != -1) {
			System.out.println(1);
			return false;
		}
		int len = mail.length();
		if (mail.codePointCount(index, len - 1) < 1) {
			System.out.println(2);
			return false;
		}
		for (int i = 0; i < index; i++) {
			char c = mail.charAt(i);
			if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '.'))
				return false;
		}
		for (int i = index + 1; i < len; i++) {
			char c = mail.charAt(i);
			if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '.'))
				return false;
		}
		return true;
	}
}
