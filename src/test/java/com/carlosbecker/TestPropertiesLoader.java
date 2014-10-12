package com.carlosbecker;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class TestPropertiesLoader {
	public static void load() throws Exception {
		Properties systemProperties = System.getProperties();
		String pathname = "./src/test/resources/test.properties";
		systemProperties.load(new FileInputStream(new File(pathname)));
		System.setProperties(systemProperties);
	}
}
