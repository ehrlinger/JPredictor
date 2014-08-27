/**
 * @ (#) Version.java
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import support.ExceptionHandler;

/**
 * Version maintains versioning information. On release of the application,
 * update these variables and the values maintained in the webpage
 * http://www.ssdi.dyndns.org
 * 
 * The program can checks it's release date through the isUpToDate method.
 * 
 * @author John Ehrlinger
 * @version 1.0 28 Mar 2000
 */
public final class Version {
	private static Version version;

	private static String versionNumber;

	/**
	 * Implements the Singleton design pattern.
	 */
	public static Version getInstance(final String packName) {
		if (version == null) {
			version = new Version();
		}
		versionNumber = Package.getPackage(packName).getImplementationVersion();
		return (version);
	}

	// Get a Version logger.
	private final Logger logger = Logger.getLogger(util.Version.class);

	private final String versionURL = "http://ctresearch.ccf.org/";

	/**
	 * Return the version number for the application.
	 */
	public String getVersionNumber() {
		return (versionNumber);
	}

	/**
	 * Return the URL we checked to determine if the program was up to date
	 */
	public String getVersionURL() {
		return (versionURL);
	}

	/**
	 * Checks the application version against the version reported as the latest
	 * release from the application website. If the version and release date match
	 * the current values, this returns a true value. If access is denied to the
	 * web page, the method also returns true.
	 */
	public boolean isUpToDate(final String location) {
		// Parse the specified URL for the
		// latest release date. If the current app
		// version and release date are the same as
		// those on the webpage return true.

		final StringBuffer results = new StringBuffer();

		// For some reason, I can't read the current release version from the wiki,
		// :P
		// So I maintain a Joomla page with the version information.
		final String versURL = versionURL + location;

		logger.debug(versURL);

		try {
			final URL url = new URL(versURL);
			final URLConnection ser = url.openConnection();
			logger.debug(ser.toString());
			final InputStream in = ser.getInputStream();
			int c = 0;
			while ((c = in.read()) != -1) {
				results.append((char) c);
			}
			in.close();
		} catch (final MalformedURLException e) {
			logger.error(e);
			// We couldn't get to the net, so let's pretend everything is OK
			return (true);
		} catch (final UnknownHostException e) {
			logger.error(e);
			// We could get net, but not find the host, so let's pretend everything is
			// OK
			return (true);
		} catch (final IOException e) {
			ExceptionHandler.logger(e, logger);
			// We got site, but couldn't find the page????, so let's pretend
			// everything is OK
			return (true);
		}

		// Now parse the StringBuffer for the version number
		// string "Current Release Version:
		// " (length of this string is 25 including the
		// trailing space)
		String vers = results.toString();
		// logger.debug(results);
		logger.debug(results.indexOf("Current Release Version: "));

		if (vers.indexOf("Current Release Version: ") >= 0) {
			vers = vers.substring(vers.indexOf("Current Release Version: ") + 25,
					vers.length());

			// logger.debug(vers);

			vers = vers.substring(0, vers.indexOf("</p>"));
			logger.debug("Released version: " + vers + " Current Version: "
					+ versionNumber);
		}
		if (versionNumber != null && vers.contains(versionNumber)) {
			logger.debug("isUpToDate = true");
			return (true);
		}
		logger.debug("isUpToDate = false");
		return (false);

	}

} // Version

