package in.co.rays.util;

import java.util.ResourceBundle;

/**
 * Read the property values from application properties file using Resource
 * Bundle
 *
 * @author Chetan Patidar
 * @version 1.0
 * @Copyright (c) Chetan Patidar
 *
 */
public class PropertyReader {

	private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

	/**
	 * Return value of key
	 *
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {

		String val = null;

		try {
			val = rb.getString(key); // {0} is required
		} catch (Exception e) {
			val = key;
		}
		return val;
	}

	/**
	 * Gets String after placing param values
	 *
	 * @param key
	 * @param param
	 * @return String
	 */
	public static String getValue(String key, String param) {
		String msg = getValue(key); // {0} is required
		msg = msg.replace("{0}", param);
		return msg;
	}

	/**
	 * Gets String after placing params values
	 *
	 * @param key
	 * @param params
	 * @return
	 */
	public static String getValue(String key, String[] params) {
		String msg = getValue(key);
		for (int i = 0; i < params.length; i++) {
			msg = msg.replace("{" + i + "}", params[i]);
		}
		return msg;
	}

	/**
	 * Test method
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Single key example:");
		System.out.println(PropertyReader.getValue("error.require"));

		System.out.println("\nSingle parameter replacement example:");
		System.out.println(PropertyReader.getValue("error.require", "loginId"));

		System.out.println("\nMultiple parameter replacement example:");
		String[] params = { "Roll No", "Student Name" };
		System.out.println(PropertyReader.getValue("error.multipleFields", params));

	}

}