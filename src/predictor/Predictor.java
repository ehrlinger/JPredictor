/*
 * Predictor, Version 1.0
 * 
 * Copyright (c) 2006 The Cleveland Clinic Foundation. All rights reserved.
 */

package predictor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.Timer;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import support.ExceptionHandler;
import support.Property;
import transplantSupport.TransplantSupport;
import util.AboutDialog;
import util.SwingWorker;

import com.jgoodies.looks.FontPolicies;
import com.jgoodies.looks.FontPolicy;
import com.jgoodies.looks.FontSet;
import com.jgoodies.looks.FontSets;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

/**
 * 
 * 
 * @author John Ehrlinger
 * @version 1.0 - 3/3/2006
 */
public class Predictor {

	protected static AboutDialog aboutDialog;

	protected static String classload = TransplantSupport.class.getName();

	private static final String DefaultModel = "DefaultModel";

	private static Logger log = Logger.getLogger(Predictor.class);

	protected static PredictorFrame pFrame;

	/**
	 * main function
	 * 
	 * @param args
	 *          command line arguments
	 */
	public static void main(String[] args) {

		Preferences _prefs = Preferences.userNodeForPackage(Predictor.class);

		// Setup logging using a properties file.
		try {
			PropertyConfigurator.configure("config/log4j.properties");
			log.debug("");
			log.debug("");
			log.debug("");
			log.debug(" STARTUP!!!! ");
			log
					.debug(".........................  main function ..................................");
			log.debug("");

			try {

				FontSet fontSet = FontSets.createDefaultFontSet(new Font("Tahoma",
						Font.PLAIN, 11), // control font
						new Font("Tahoma", Font.PLAIN, 12), // menu font
						new Font("Tahoma", Font.BOLD, 11) // title font
						);
				FontPolicy fixedPolicy = FontPolicies.createFixedPolicy(fontSet);
				PlasticLookAndFeel.setFontPolicy(fixedPolicy);
				UIManager
						.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");

				UIManager.put("Application.useSystemFontSettings", Boolean.TRUE);
				// UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
				log.debug("Using Plastic L&F");
			} catch (Exception e) {
				// Likely Plastic is not in the classpath; ignore it.
				log.debug("Using System L&F");
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					ExceptionHandler.logger(e, log);
				}
			}

			// We could dynamically load a UI Class, similar to a plugin.
			// Place the name of the UI class in the properties file.... or
			// on the command line (?)
			//
			// program up the standard tools... like graphics, and external blackbox
			// program runner.
			Property.load("predictor"); // loading properties
			log.debug("Loading predictor.properties file");

			// Post a splash that checks for updates.
			// Dump an application header to the log.
			log.info("JPredictor program");

			if (args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					log.debug("Args[" + i + "]:=" + args[i]);
					classload = args[i];
					_prefs.put(DefaultModel, classload);
				}

			} else {
				classload = _prefs.get(DefaultModel, classload);

			}
			log.debug("Classload:" + classload);
			log.debug("--------------------------------------------------------");
			// TODO
			// We've changed the name of the some patient classes... this will avoid
			// an error on loading. Very rare occurance and we can probably remove
			// this after a few releases.
			classload = classload.replace("TransplantSupport.TransplantSupport",
					"transplantSupport.TransplantSupport");
			classload = classload.replace("PostInfarctVSD.PostInfarctVSD",
					"postInfarctVSD.PostInfarctVSD");
			log.debug("Classload:" + classload);
			log.debug("--------------------------------------------------------");
	
			// Create and load the default properties.
			log.debug("Show a splash while we work.");
			aboutDialog = new AboutDialog(null, "predictor", Property.getTitle());

			// Splash the screen.
			// Create an AboutDialog box for the application.
			final SwingWorker worker = new SwingWorker() {
				Logger log = Logger.getLogger(SwingWorker.class);

				PredictorFrame parentFrame;

				public Object construct() {
					try {
						log.debug(classload);
						parentFrame = new PredictorFrame(classload);
					} catch (Exception e) {
						ExceptionHandler.logger(e, log);
					}
					return parentFrame;
				}

				public void finished() {
			
					pFrame = (PredictorFrame) get();
					pFrame.setTitle(Property.getTitle() + ":"
							+ classload.substring(classload.lastIndexOf(".") + 1) + " - "
							+ Property.getVersion());
					// Create the SSDI interface.

					log.debug("Showing parent frame");
					pFrame.setVisible(true);
					// Set a timer to shut the about dialog off in 100ms.
					Timer timer = new Timer(5000, new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							log.debug("----------------------------");
							log.debug("");
							log.debug("Timer event finished!! Shut the splash off");
							log.debug("");
							log.debug("----------------------------");
							aboutDialog.setVisible(false);

						}
					});
					timer.setRepeats(false);
					timer.start();
					// To bring the dialog to the front.
					aboutDialog.setVisible(false);
					aboutDialog.setEnable(true);
					aboutDialog.setVisible(true);

				}
			};
			// Create the SSDI interface.
			log.debug("Create the application.");
			worker.start();
			aboutDialog.setEnable(false);
			aboutDialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.logger(e, log);
		}
		_prefs.put(DefaultModel, classload);
	}

	public static void writeDefaultClass(String classString) {
		Preferences _prefs = Preferences.userNodeForPackage(Predictor.class);
		_prefs.put(DefaultModel, classString);
	}

}
