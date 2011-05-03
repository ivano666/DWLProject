package DWLProject.utils;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * DLWProperties.java
 * 
 * @author Ivan C
 * 
 */
public class DWLProperties {
	private Properties dwlProperties;
	private static DWLProperties _instance = new DWLProperties();

	private DWLProperties() {
		loadProperties();
	}

	private void loadProperties() {
		final ResourceBundle rb = ResourceBundle.getBundle("DWLProject.utils.dwl");
		dwlProperties = new Properties();
		for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
			final String key = (String) keys.nextElement();
			final String value = rb.getString(key);

			dwlProperties.put(key, value);
		}
	}

	public static DWLProperties getInstance() {
		return _instance;
	}

	public String getValue(String name) {
		return dwlProperties.getProperty(name);
	}

	public void reload() {
		loadProperties(); 
	}
}
