package patient;

import graph.GraphModel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.data.xy.XYSeries;

import patientModelSet.PatientModelSet;
import support.ExceptionHandler;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.validation.ValidationResultModel;

public abstract class PatientModel extends Model implements Serializable,
		Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7663155030743095480L;

	// So all graphs use the same number of time steps.
	// TODO Move this to the patient model set so it can be manipulated.
	protected int n_time_steps = 150;

	// The name of the hazard output file
	protected final Vector<String> dataFileName = new Vector<String>();

	private static Logger log = Logger.getLogger(PatientModel.class);

	protected static int keyNumber = 0;

	protected int curveNumber = 0;

	public static String PROPERTYNAME_PATIENT_COLOR;

	public static final String PROPERTYNAME_PATIENT_ID = "patientID";

	public static final String PROPERTYNAME_PATIENT_NAME = "patientName";

	public static final String PROPERTYNAME_PATIENT_NOTE = "patientNote";

	public static final String PROPERTYNAME_CONFIDENCE = "confidenceLimits";

	protected String application;

	protected GraphModel graphics = null;

	protected Vector<XYSeries> hazardSeries = new Vector<XYSeries>();

	protected Vector<XYSeries> lowerCLHazSeries = new Vector<XYSeries>();

	protected Vector<XYSeries> lowerCLSurvSeries = new Vector<XYSeries>();

	// Names of the Bound Bean Properties *************************************

	/**
	 * The modelSet tells each patient HOW to plot itself... time,
	 * survival/hazard.
	 */
	protected PatientModelSet modelSet;

	protected String patientID = null;

	protected String patientName = null;

	protected String patientNote = null;

	protected boolean confidenceLimits = true;

	protected PatientPresentationModel presentModel;

	protected Vector<XYSeries> survivalSeries = new Vector<XYSeries>();

	protected Vector<XYSeries> upperCLHazSeries = new Vector<XYSeries>();

	protected Vector<XYSeries> upperCLSurvSeries = new Vector<XYSeries>();

	public PatientModel() {
		keyNumber++;

		String OSName = System.getProperty("os.name");
		String lcOSName = OSName.toLowerCase();
		boolean MAC_OS_X = lcOSName.startsWith("mac os x");
		boolean WINDOWS = lcOSName.startsWith("windows");

		log.debug("Operating system  " + OSName);
		// Find out what platform we are on, so we can determine what binary
		// program
		// we need to run.
		application = "binaries/blackbox";

		if (MAC_OS_X) {
			application += ".MAC_OS_X";
		} else if (WINDOWS) {
			application += ".exe";
		} else {
			application += "." + OSName;
		}

		log.debug("Found " + application);
	}

	/**
	 * Safest way to duplicate an object... serialize it to a string, and
	 * deserialize the string to a new object.
	 * 
	 */
	public PatientModel clone() {
		OutputStream stringOut = new ByteArrayOutputStream();
		XMLEncoder encode = new XMLEncoder(stringOut);
		encode.writeObject(this);
		encode.close();
		String newString = stringOut.toString();
		InputStream is = new ByteArrayInputStream(newString.getBytes());
		XMLDecoder decode = new XMLDecoder(is);
		return ((PatientModel) decode.readObject());
	}

	/**
	 * 
	 * @param d1
	 * @param d2
	 * @return d1 - d2 in days.
	 */
	protected int datediff(Date date1, Date date2) {

		long d1 = date1.getTime();
		long d2 = date2.getTime();
		log.debug("datediff: " + d1 + " " + d2);

		/** The larger of the two dates */

		// Get msec from each, and subtract.
		long diff = ((d1 > d2) ? d1 - d2 : d2 - d1);
		return (Math.round(diff / (1000 * 60 * 60 * 24)));
		// System.out.println("The 21st century (up to " + today + ") is "
		// + (diff / (1000 * 60 * 60 * 24)) + " days old.");
	}

	/**
	 * Each subclass knows how to build the string for the specific model. This
	 * function actually does the analysis.
	 * 
	 */
	public void execute() throws ModelExecuteException {
		log.debug("Execute " + this.getClass().getName());

		if (graphics == null) {
			log.debug("NULL Graphics object.");
			return;
		}
		try {
			// Clear the survivalSeries and hazardSeries
			// Clear any previous values.
			graphics.removeDataset(this);
			graphics.revalidate();
			// graphics.repaint();

			survivalSeries.removeAllElements();
			upperCLSurvSeries.removeAllElements();
			lowerCLSurvSeries.removeAllElements();
			hazardSeries.removeAllElements();
			upperCLHazSeries.removeAllElements();
			lowerCLHazSeries.removeAllElements();
		} catch (NullPointerException ex) {
			log.debug("PatientModel clear error");
			ExceptionHandler.logger(ex, log);
			// return;
		}
		// Given we have multiple dataFileNames, to run multiple models.
		log.debug("Number of datafiles: " + dataFileName.size());

		String title = null;

		curveNumber = 0;
		for (String fileName : dataFileName) {
			log.debug("Running the file: " + fileName);

			log.debug(fileName);

			survivalSeries.addElement(new XYSeries("% Survival"));
			if (dataFileName.size() > 1) {
				title = fileName.substring(fileName.indexOf(".") + 1, fileName
						.indexOf(".xpt"));

			} else {
				title = new String("Survival");
			}
			survivalSeries.lastElement().setKey(title);

			upperCLSurvSeries.addElement(new XYSeries(""));
			upperCLSurvSeries.lastElement().setKey(
					"upperSurvival" + keyNumber + "_" + curveNumber);

			lowerCLSurvSeries.addElement(new XYSeries(""));
			lowerCLSurvSeries.lastElement().setKey(
					"lowerSurvival" + keyNumber + "_" + curveNumber);

			hazardSeries.addElement(new XYSeries("Hazard"));
			upperCLHazSeries.addElement(new XYSeries(""));
			upperCLHazSeries.lastElement().setKey(
					"upperHazard" + keyNumber + "_" + curveNumber);

			lowerCLHazSeries.addElement(new XYSeries(""));
			lowerCLHazSeries.lastElement().setKey(
					"lowerHazard" + keyNumber + "_" + curveNumber);

			curveNumber++;

			String[] subStrings;
			// Get the variable string from the entry form.
			// log.debug("Model: " + dModel.getExecuteString());

			// Setup the string.
			String cmdline = getExecuteString(fileName);

			if (cmdline == null) {
				log.debug("Null command for " + fileName);
				continue;
			}
			// Sanity checks.
			if (cmdline.contains(" true ") || cmdline.contains(" false ")) {
				throw new ModelExecuteException(
						"SANITY CHECK: found boolean value (true or false): "
								+ cmdline);
			}

			StringTokenizer st = new StringTokenizer(cmdline);
			log.debug("Number of tokens: " + st.countTokens());

			// The first 5 tokens are fixed... the fifth, tells us how many
			// variables
			// we
			// are working with.
			for (int ii = 0; ii < 4; ii++) {
				log.debug("Token " + ii + ":" + st.nextToken());
			}
			int count = Integer.parseInt(st.nextToken());

			if (count != st.countTokens()) {
				throw new ModelExecuteException(
						"SANITY CHECK: argument count mismatch, you say we need "
								+ count
								+ " variable values, but have provided "
								+ (st.countTokens()) + cmdline);
			}

			// "./blackbox hmdeadls_nonparsimoneous.dta 0.1 10 56 183 0 0 0 0 0
			// 0 0
			// 0
			// 0 0 0 0 0 0 1 0 0 1 0.390625 1 0.700909175664 2.00694444444
			// 1.77777777778 1.5625 1 0 0 0 2.99575280765 0 0 0 0 0 0 0 1
			// 2.61006979274 0 5.20948615284 0 0 0 0 0 0.625 2.83321334406 1 1
			// 0.8 0
			// 0
			// 1 0 0 ";

			log.debug("Running : " + cmdline);

			// Run the program
			try {
				String line;
				Process p = Runtime.getRuntime().exec(cmdline);
				BufferedReader input = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				// Add the 100% at t=0 points.

				survivalSeries.lastElement().add(Double.valueOf("0"),
						Double.valueOf("100"));
				upperCLSurvSeries.lastElement().add(Double.valueOf("0"),
						Double.valueOf("100"));
				lowerCLSurvSeries.lastElement().add(Double.valueOf("0"),
						Double.valueOf("100"));

				log.debug("ran file:" + fileName);
				while ((line = input.readLine()) != null) {
					// Stuff the variables from the line into the XYdata series
					// that
					// we'll plot.
					log.debug(line);
					subStrings = line.split(" ");
					survivalSeries.lastElement().add(
							Double.valueOf(subStrings[0]),
							Double.valueOf(subStrings[1]));
					hazardSeries.lastElement().add(
							Double.valueOf(subStrings[0]),
							Double.valueOf(subStrings[4]));
					if (confidenceLimits) {
						upperCLSurvSeries.lastElement().add(
								Double.valueOf(subStrings[0]),
								Double.valueOf(subStrings[3]));
						lowerCLSurvSeries.lastElement().add(
								Double.valueOf(subStrings[0]),
								Double.valueOf(subStrings[2]));

						upperCLHazSeries.lastElement().add(
								Double.valueOf(subStrings[0]),
								Double.valueOf(subStrings[6]));
						lowerCLHazSeries.lastElement().add(
								Double.valueOf(subStrings[0]),
								Double.valueOf(subStrings[5]));
					}
				}
				input.close();
			} catch (Exception err) {
				log.error("!!!Exception!!!");
				ExceptionHandler.logger(err, log);
			}
		}

		log.debug(curveNumber);
		log.debug(survivalSeries.size());
		graphics.addDataset(this);

	}

	public Vector<Color> getColor() {
		Vector<Color> clr = new Vector<Color>();

		// This works if there is only 1 curve for the patient.... else we need
		// to override.
		try {
			// Find the field and value of colorName
			Field field = Class.forName("org.jfree.chart.ChartColor").getField(
					getPatientColor());

			clr.add((Color) field.get(null));
		} catch (Exception e) {

		}
		return clr;
	}

	public abstract String getExecuteString(String filename);

	public String getPatientID() {
		return patientID;
	}

	public String getPatientName() {
		return patientName;
	}

	public String getPatientNote() {
		return patientNote;
	}

	/**
	 * Tells the application how to execute a set of patients.
	 */

	public ActionListener getPatientRunAction() {
		log.debug("Adding the analysis execution command");
		return (new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				log.debug("PatientSetRunAction performed.");
				try {
					execute();
				} catch (ModelExecuteException ex) {
					ExceptionHandler.logger(ex, log);
				}
			}
		});
	}

	public void removeFromGraph() {
		log.debug("Removing " + getPatientName());
		graphics.removeDataset(this);
	}

	public void setGraph(GraphModel graphics) {
		this.graphics = graphics;
		addPropertyChangeListener(graphics);
	}

	public void setModelSet(PatientModelSet set) {
		modelSet = set;
	}

	public PatientModelSet getModelSet() {
		return (modelSet);
	}

	public void setGraphTime(double time) {
		if (modelSet != null)
			modelSet.setGraphTime(time);
	}

	public void setPatientID(String patientID) {
		String old = this.patientID;
		this.patientID = patientID;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_PATIENT_ID, old, patientID);
	}

	public void setPatientName(String patientName) {
		String old = this.patientName;
		this.patientName = patientName;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_PATIENT_NAME, old, patientName);
	}

	public void setPatientNote(String patientNote) {
		String old = this.patientNote;
		this.patientNote = patientNote;

		setPatientTitle();
		firePropertyChange(PROPERTYNAME_PATIENT_NOTE, old, patientNote);
	}

	protected void setPatientTitle() {

		String title = (String) this.getClass().getName();
		title = title.substring(title.lastIndexOf(".") + 1, title.length());
		log.debug(title);

		if (patientNote != null && patientNote.length() > 0) {
			modelSet.changeTabTitle(this, title + "(" + getPatientColor()
					+ "):" + patientName + ":" + patientID + " " + patientNote);
		} else if (patientName != null && patientName.length() > 0) {
			modelSet.changeTabTitle(this, title + "(" + getPatientColor()
					+ "):" + patientName + ":" + patientID);
		} else {
			modelSet.changeTabTitle(this, title + "(" + getPatientColor()
					+ "):" + patientID);
		}
	}

	// Implemented by the specific patientModels
	abstract public String getPatientColor();

	protected void setupPropertyChangeListener(
			PatientPresentationModel presenter) {
		presentModel = presenter;
		// Add a propertyChangeListener to run an
		addPropertyChangeListener(new PropertyChangeListener() {

			private ValidationResultModel valid;

			public void propertyChange(PropertyChangeEvent event) {
				log.debug("PropertyChanged:" + event.toString());
				valid = presentModel.getValidationResultModel();
				if (!valid.hasErrors())
					try {
						execute();
					} catch (ModelExecuteException ex) {
						ExceptionHandler.logger(ex, log);
					}
			}

		});
	}

	public boolean writeData(FileOutputStream writeFile) {
		// Write the file name into the outputFilename Field.
		// and write the responses it into the given file.
		try {
			XMLEncoder encoder = new XMLEncoder(writeFile);

			encoder.writeObject(this);
			log.debug("Write object");
			encoder.close();
		} catch (Exception ex) {
			ExceptionHandler.logger(ex, log);
		}
		return (true);

	}

	public Vector<Vector<XYSeries>> getHazardDatasets() {
		if (hazardSeries.size() < 1)
			return null;
		Vector<Vector<XYSeries>> data = new Vector<Vector<XYSeries>>();

		data.add(hazardSeries);
		data.add(upperCLHazSeries);
		data.add(lowerCLHazSeries);

		return data;

	}

	public Vector<Vector<XYSeries>> getSurvivalDatasets() {
		Vector<Vector<XYSeries>> data = new Vector<Vector<XYSeries>>();
		if (survivalSeries.size() < 1)
			return null;
		data.add(survivalSeries);

		data.add(upperCLSurvSeries);
		data.add(lowerCLSurvSeries);
		return data;
	}

	public boolean isConfidenceLimits() {
		return confidenceLimits;
	}

	public void setConfidenceLimits(boolean confidenceLimits) {
		boolean oldCL = this.confidenceLimits;
		this.confidenceLimits = confidenceLimits;

		firePropertyChange(PROPERTYNAME_CONFIDENCE, oldCL, confidenceLimits);
	}
}