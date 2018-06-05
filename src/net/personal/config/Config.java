package net.personal.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1879421738364779839L;

	public Config(String path) throws IOException {
		InputStream finput = new FileInputStream(path);
		load(finput);
	}
}
