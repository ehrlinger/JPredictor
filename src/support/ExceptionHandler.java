/**
 * ExceptionHandler, Version 2.0
 *
 * Copyright (c) 2002 The Cleveland Clinic Foundation.
 *  All rights reserved. 
 */
package support;

import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * ExceptionHandler consolidates duplicated code for handling exception
 * reporting.
 * 
 * @author John Ehrlinger
 * @version 2.0, 03/22/04
 * @see support.SupportRequest
 * @see org.apache.log4j.Logger
 */
public class ExceptionHandler {

	/**
	 * ExceptionHandler.logger is a static method for dumping exception
	 * information into the log, as well as handle autosubmitting a bug on the
	 * exception
	 * 
	 * @param e
	 *          Exception that was thrown, has all exception debugging info in it.
	 * @param log
	 *          Logger instance that indicates which class/object the exception
	 *          occured.
	 */
	public static void logger(Exception e, Logger log) {
		logger(e, log, System.getProperty("user.name"));
	}

	public static void logger(Exception e, Logger log, String user) {
		if (e.getClass() == SQLException.class) {
			log.error("Error Code :" + ((SQLException) e).getErrorCode());
		}
		log.error("Error Message :" + e.getMessage());
		log.error("Error Cause :" + e.getCause());

		// print the trace to the standard output
		e.printStackTrace();

		// Print the exception info to the log
		log.error(e.toString());

		// print the stack trace to array.
		StackTraceElement[] ste = e.getStackTrace();
		for (int i = 0; i < ste.length; i++) {
			log.error(ste[i].toString());
		}

		// Send that array to the bug tracking system.
		SupportRequest.getInstance().sendBug(e, ste, user);
	}

	public ExceptionHandler() {
	}

}
