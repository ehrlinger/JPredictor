/*
 * OR Scheduler, Version 1.0
 *
 * Copyright (c) 2002 The Cleveland Clinic Foundation.
 *  All rights reserved.
 */

package support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * ORProps class loads the ORScheduler properties from orsch.properties file.
 * 
 * @author Sangeeta Huria
 * @version 1.0, 08/12/02
 * @see java.util.Properties
 */
public class Property {

	/**
	 * 
	 */
	private static String connectedToServer;

	/**
	 * 
	 */
	private static String db;

	/**
	 * 
	 */
	private static String fontAdjust; // font adjust for html font sizing

	/**
	 * 
	 */
	private static int fontSize; // font size

	/**
	 * 
	 */
	private static String groups;

	/**
	 * 
	 */
	private static Logger log = Logger.getLogger(Property.class);

	/**
	 * 
	 */
	private static String product;

	/**
	 * 
	 */
	private static int screenHeight;

	/**
	 * 
	 */
	private static int screenWidth;

	/**
	 * 
	 */
	private static String TITLE;

	/**
	 * 
	 */
	private static int uid;

	/**
	 * 
	 */
	private static String uinfo;

	private static String VENDOR;

	private static String VERSION;

	/**
	 * getConnectedToServer()
	 * 
	 * @return connectedToServer
	 */
	public static String getConnectedToServer() {
		return connectedToServer;
	}

	/**
	 * getDB()
	 * 
	 * @return db
	 */
	public static String getDB() {
		return db;
	}

	/**
	 * getFontAdjust()
	 * 
	 * @return fontAdjust
	 */
	public static String getFontAdjust() {
		return fontAdjust;
	}

	/**
	 * getFontSize()
	 * 
	 * @return fontSize
	 */
	public static int getFontSize() {
		return fontSize;
	}

	/**
	 * getGroups()
	 * 
	 * @return groups
	 */
	public static String getGroups() {
		return (groups);
	}

	/**
	 * getServer()
	 * 
	 * @return server
	 */
	public static String getProduct() {
		return product;
	}

	/**
	 * getScreenHeight()
	 * 
	 * @return screenHeight
	 */
	public static int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * getScreenWidth()
	 * 
	 * @return screenWidth
	 */
	public static int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * getTitle()
	 * 
	 * @return TITLE
	 */
	public static String getTitle() {
		return (TITLE);
	}

	/**
	 * getUid()
	 * 
	 * @return uid
	 */
	public static int getUid() {
		return uid;
	}

	/**
	 * getUinfo()
	 * 
	 * @return uinfo
	 */
	public static String getUinfo() {
		return uinfo;
	}

	/**
	 * getVendor()
	 * 
	 * @return VENDOR
	 */
	public static String getVendor() {
		return (VENDOR);
	}

	/**
	 * getVersion()
	 * 
	 * @return VERSION
	 */
	public static String getVersion() {
		return (VERSION);
	}

	/**
	 * Static function which loads the properties from orsch.properties file. It
	 * stores the retrieved values in static member variables.
	 */
	public static void load(String programName) {
		try {
			// By convention.
			String filename = "config/" + programName + ".properties";
			InputStream input;
			File file = new File(filename);
			Properties props = new Properties();
			props.load(input = new FileInputStream(file));
			input.close();

			// Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

			screenWidth = Integer.parseInt(props.getProperty("width"));
			screenHeight = Integer.parseInt(props.getProperty("height"));
			fontSize = Integer.parseInt(props.getProperty("fontsize"));
			fontAdjust = props.getProperty("fontadjustment");
			product = props.getProperty("product");

			// We removed this to allow it in the properties file...
			// easier for on the fly options. Someday we may want to
			// create a menu to save this back to the properties file.
			TITLE = Package.getPackage(programName).getImplementationTitle();
			VENDOR = Package.getPackage(programName).getImplementationVendor();
			VERSION = Package.getPackage(programName).getImplementationVersion();
		} catch (IOException e) {
			ExceptionHandler.logger(e, log);
		}
	}

	/**
	 * setConnectedToServer()
	 * 
	 * @param connection
	 *          connection input
	 */
	public static void setConnectedToServer(String connection) {
		connectedToServer = connection;
	}

	/**
	 * setUid()
	 * 
	 * @param userID
	 *          input
	 */
	public static void setUid(int userID) {
		uid = userID;
	}

	/**
	 * setUinfo(String)
	 * 
	 * @param userInfo
	 *          input
	 */
	public static void setUinfo(String userInfo) {
		uinfo = userInfo;
	}

	public Property() {
	}
}
