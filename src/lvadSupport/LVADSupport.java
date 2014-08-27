package lvadSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.data.xy.XYSeries;

import patient.ModelExecuteException;
import patient.PatientModel;
import support.ExceptionHandler;

import com.jgoodies.validation.Validator;

public class LVADSupport extends PatientModel {
	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = -6380646684272477919L;

	private final static int num_anal_vars = 8;

	private static Logger log = Logger.getLogger(LVADSupport.class);

	// Names of the Bound Bean Properties *************************************

	public static final String PROPERTYNAME_PATIENT_COLOR = "patientColor";

	protected String patientColor = null;
	/* Definitions of variables in model ; */

	/* Demographics; */
	/* ht=173; Height (cm) (lo 110 - hi 210) */
	public static String PROPERTYNAME_HEIGHT = "height";

	private double height = 177;

	/* wgt=88; Height (cm) (lo 110 - hi 210) */
	public static String PROPERTYNAME_WEIGHT = "weight";

	private double weight = 88;

	/* bun_pr=0; BUN (mg/dL) */
	public static String PROPERTYNAME_BUN_PR = "bun_pr";

	private double bun_pr = 9.0;

	/* hx_dial=0; history dialysis */
	public static String PROPERTYNAME_HX_DIAL = "hx_dial";

	private boolean hx_dial = false;

	/* hx_htn=0; History of hypertension */
	public static String PROPERTYNAME_HX_HTN = "hx_htn";

	private boolean hx_htn = false;

	/* ptcapr=0; Previous PCI (percutaneous coronary intervention) */
	public static String PROPERTYNAME_VENT_PR = "vent_pr";

	private boolean vent_pr = false;
	public static String PROPERTYNAME_MCS_DATE = "mcsDate";
	private Date mcsDate;

	public static String PROPERTYNAME_LVAD = "lvad";
	private boolean lvad;
	public static String PROPERTYNAME_MB_NUERO = "mb_nuero";
	private boolean mb_nuero=false;

	public static String PROPERTYNAME_MCS_2_DATE = "mcsDate2";
	private Date mcsDate2;

	public static String PROPERTYNAME_LVAD_2 = "lvad2";
	private boolean lvad2;
	public static String PROPERTYNAME_MB_NUERO_2 = "mb_nuero2";
	private boolean mb_nuero2;

	public static String PROPERTYNAME_MCS_3_DATE = "mcsDate3";
	private Date mcsDate3;

	public static String PROPERTYNAME_LVAD_3 = "lvad3";
	private boolean lvad3;
	public static String PROPERTYNAME_MB_NUERO_3 = "mb_nuero3";
	private boolean mb_nuero3;

	/* Experience; */
	/* dt_list= mdy(1,1,2004); On ICD at listing */
	public static String PROPERTYNAME_BIRTHDATE = "birthDate";

	private Date birthDate;

	/**
	 */

	/**
	 * Constructs an empty <code>lvadSupport</code>.
	 */
	public LVADSupport() {
		super();
		setupPropertyChangeListener(new LVADSupportPresentationModel(this));
		// The name of the hazard output file
		dataFileName.add("data/lvad.hmdead_mcs.dta");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see predictor.IEntryForm#getExecuteString()
	 */
	public Vector<String> getExecuteStrings(String fileName) {
		Vector<String> ans = new Vector<String>();
		/*
		 * The input variables...
		 */
		// Transformations.
		/* Demographics; */
		double ln_wtht = Math.log(weight / height);
		/* Cardic comorbidity; */
		double ln_bun = Math.log(bun_pr);

		double total_time = modelSet.getGraphTime();
		if(total_time < 1) n_time_steps = 50;
		double mcstime = total_time;
		/* Date of listing; */
		if (mcsDate2 != null) {
			mcstime = datediff(mcsDate, mcsDate2) / 365.2425;
			total_time = total_time - mcstime;
		}
		// Time is in years...
		// We should test for the application now. This way we can post a
		// message
		// if we're on an
		// uncompiled platform.
		log.debug(application);
		log.debug("filename: " + fileName);

		// Build the string.
		ans.add(application + " " + fileName + " " + n_time_steps + " "
				+ mcstime + " " + num_anal_vars + " " + ln_wtht + " " + ln_bun
				+ " " + (lvad ? "1" : "0") + " " + 1 + " "
				+ (hx_dial ? "1" : "0") + " " + (hx_htn ? "1" : "0") + " "
				+ (vent_pr ? "1" : "0") + " " + (mb_nuero ? "1" : "0"));

		if (mcsDate2 != null && mcsDate3 != null) {
			mcstime = datediff(mcsDate2, mcsDate3) / 365.2425;
			total_time = total_time - mcstime;
			// Build the string.
			ans.add(application + " " + fileName + " " + n_time_steps + " "
					+ mcstime + " " + num_anal_vars + " " + ln_wtht + " "
					+ ln_bun + " " + (lvad2 ? "1" : "0") + " " + 2 + " "
					+ (hx_dial ? "1" : "0") + " " + (hx_htn ? "1" : "0") + " "
					+ (vent_pr ? "1" : "0") + " " + (mb_nuero2 ? "1" : "0"));

		} else if (mcsDate2 != null && mcsDate3 == null) {
			mcstime = total_time;
			// Build the string.
			ans.add(application + " " + fileName + " " + n_time_steps + " "
					+ mcstime + " " + num_anal_vars + " " + ln_wtht + " "
					+ ln_bun + " " + (lvad2 ? "1" : "0") + " " + 2 + " "
					+ (hx_dial ? "1" : "0") + " " + (hx_htn ? "1" : "0") + " "
					+ (vent_pr ? "1" : "0") + " " + (mb_nuero2 ? "1" : "0"));

		}

		if (mcsDate3 != null) {
			mcstime = total_time;
			// Build the string.
			ans.add(application + " " + fileName + " " + n_time_steps + " "
					+ mcstime + " " + num_anal_vars + " " + ln_wtht + " "
					+ ln_bun + " " + (lvad3 ? "1" : "0") + " " + 3 + " "
					+ (hx_dial ? "1" : "0") + " " + (hx_htn ? "1" : "0") + " "
					+ (vent_pr ? "1" : "0") + " " + (mb_nuero3 ? "1" : "0"));

		}
		log.debug(ans);

		return (ans);
	}

	/**
	 * Each subclass knows how to build the string for the specific model. This
	 * function actually does the analysis.
	 * 
	 * @overrides PatientModel.execute(0
	 */
	public void execute() throws ModelExecuteException {
		double survScale = 1;
		double survLoc = 0;
		double upSurvScale = 1;
		double upSurvLoc = 0;
		double lowSurvScale = 1;
		double lowSurvLoc = 0;
		double hazScale = 1;
		double hazLoc = 0;
		double upHazScale = 1;
		double upHazLoc = 0;
		double lowHazScale = 1;
		double lowHazLoc = 0;
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
			Vector<String> cmd = getExecuteStrings(fileName);

			if (cmd == null) {
				log.debug("Null command for " + fileName);
				continue;
			}

			// Initialize the curves.
			// Add the 100% at t=0 points.
			survivalSeries.lastElement().add(Double.valueOf("0"),
					Double.valueOf("100"));
			upperCLSurvSeries.lastElement().add(Double.valueOf("0"),
					Double.valueOf("100"));
			lowerCLSurvSeries.lastElement().add(Double.valueOf("0"),
					Double.valueOf("100"));

			int seriesLength = cmd.size();
			log.debug("Plotting " + seriesLength + " sections");
			int seriesCount = 0;

			// We got a Vector of commandlines.
			for (String cmdline : cmd) {
				seriesCount++;

				if (seriesCount > 1) {
					
					log.debug(seriesCount + " series of size " + n_time_steps);
					survScale = survivalSeries.lastElement().getY(n_time_steps)
							.doubleValue() / 100.0;
					survLoc = survivalSeries.lastElement().getX(n_time_steps)
							.doubleValue();
					upSurvScale = upperCLSurvSeries.lastElement().getY(
							n_time_steps).doubleValue() / 100.0;
					upSurvLoc = upperCLSurvSeries.lastElement().getX(n_time_steps)
							.doubleValue();
					lowSurvScale = lowerCLSurvSeries.lastElement().getY(
							n_time_steps).doubleValue() / 100.0;
					lowSurvLoc = lowerCLSurvSeries.lastElement()
							.getX(n_time_steps).doubleValue();
					hazScale = hazardSeries.lastElement().getY(n_time_steps)
							.doubleValue();
					hazLoc = hazardSeries.lastElement().getX(n_time_steps)
							.doubleValue();
					upHazScale = upperCLHazSeries.lastElement().getY(n_time_steps)
							.doubleValue();
					upHazLoc = upperCLHazSeries.lastElement().getX(n_time_steps)
							.doubleValue();
					lowHazScale = lowerCLHazSeries.lastElement()
							.getY(n_time_steps).doubleValue();
					lowHazLoc = lowerCLHazSeries.lastElement().getX(n_time_steps)
							.doubleValue();
					
					log.debug("Scale: " + survScale + " location: " + survLoc );
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

				// "./blackbox hmdeadls_nonparsimoneous.dta 0.1 10 56 183 0 0 0
				// 0 0
				// 0 0
				// 0
				// 0 0 0 0 0 0 1 0 0 1 0.390625 1 0.700909175664 2.00694444444
				// 1.77777777778 1.5625 1 0 0 0 2.99575280765 0 0 0 0 0 0 0 1
				// 2.61006979274 0 5.20948615284 0 0 0 0 0 0.625 2.83321334406 1
				// 1
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
					log.debug("ran file:" + fileName);
					while ((line = input.readLine()) != null) {

						// Stuff the variables from the line into the XYdata
						// series
						// that
						// we'll plot.
						log.debug(line);
						subStrings = line.split(" ");
						survivalSeries.lastElement().add(
								survLoc + Double.valueOf(subStrings[0]),
								survScale * Double.valueOf(subStrings[1]));
						hazardSeries.lastElement().add(
								hazLoc + Double.valueOf(subStrings[0]),
								hazScale * Double.valueOf(subStrings[4]));
						if (confidenceLimits) {
							upperCLSurvSeries
									.lastElement()
									.add(
											upSurvLoc
													+ Double
															.valueOf(subStrings[0]),
											upSurvScale
													* Double
															.valueOf(subStrings[3]));
							lowerCLSurvSeries.lastElement().add(
									lowSurvLoc + Double.valueOf(subStrings[0]),
									lowSurvScale
											* Double.valueOf(subStrings[2]));

							upperCLHazSeries.lastElement().add(
									upHazLoc + Double.valueOf(subStrings[0]),
									upHazScale * Double.valueOf(subStrings[6]));
							lowerCLHazSeries
									.lastElement()
									.add(
											lowHazLoc
													+ Double
															.valueOf(subStrings[0]),
											lowHazScale
													* Double
															.valueOf(subStrings[5]));
						}
					}
					input.close();
				} catch (Exception err) {
					log.error("!!!Exception!!!");
					ExceptionHandler.logger(err, log);
				}
			}
		}
		log.debug(curveNumber);
		log.debug(survivalSeries.size());
		graphics.addDataset(this);

	}

	public Validator<LVADSupport> getValidator() {
		return (new LVADSupportValidator(this));
	}

	public Date getBirthDate() {
		return (birthDate);

	}

	public void setBirthDate(Date newDate) {
		Date oldDate = getBirthDate();
		birthDate = newDate;
		firePropertyChange(PROPERTYNAME_BIRTHDATE, oldDate, newDate);
	}

	public Date getMcsDate() {
		return (mcsDate);

	}

	public void setMcsDate(Date newDate) {
		Date oldDate = getMcsDate();
		mcsDate = newDate;
		log.debug("MCS Date Set:" + newDate + " |" + oldDate + "|");
		firePropertyChange(PROPERTYNAME_MCS_DATE, oldDate, newDate);
	}

	public String getPatientColor() {
		log.debug("Getting PatientColor: " + patientColor);
		if (patientColor == null)
			patientColor = "Red";
		return patientColor;
	}

	public void setPatientColor(String patientColor) {
		log.debug("PatientColor: " + patientColor);
		String oldColor = this.patientColor;
		this.patientColor = patientColor;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_PATIENT_COLOR, oldColor, patientColor);
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		double oldHeight = this.height;
		this.height = height;
		firePropertyChange(PROPERTYNAME_HEIGHT, oldHeight, height);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		double oldWeight = this.weight;
		this.weight = weight;
		firePropertyChange(PROPERTYNAME_HEIGHT, oldWeight, height);

	}

	public double getBun_pr() {
		return bun_pr;
	}

	public void setBun_pr(double bunPr) {
		double oldBun = this.bun_pr;
		bun_pr = bunPr;
		firePropertyChange(PROPERTYNAME_BUN_PR, oldBun, bunPr);
	}

	public boolean isHx_dial() {
		return hx_dial;
	}

	public void setHx_dial(boolean hxDial) {
		boolean oldHxDial = this.hx_dial;
		hx_dial = hxDial;
		firePropertyChange(PROPERTYNAME_HX_DIAL, oldHxDial, hxDial);
	}

	public boolean isHx_htn() {
		return hx_htn;
	}

	public void setHx_htn(boolean hxHtn) {
		boolean oldHxHtn = this.hx_htn;
		hx_htn = hxHtn;
		firePropertyChange(PROPERTYNAME_HX_HTN, oldHxHtn, hxHtn);

	}

	public boolean isVent_pr() {
		return vent_pr;
	}

	public void setVent_pr(boolean ventPr) {
		boolean oldventPr = this.vent_pr;
		vent_pr = ventPr;
		firePropertyChange(PROPERTYNAME_VENT_PR, oldventPr, ventPr);
	}

	public boolean getLvad() {
		return lvad;
	}

	public void setLvad(boolean lvad) {
		boolean oldLvad = this.lvad;
		this.lvad = lvad;
		firePropertyChange(PROPERTYNAME_LVAD, oldLvad, lvad);

	}

	public boolean getMb_nuero() {
		return mb_nuero;
	}

	public void setMb_nuero(boolean mbNuero) {
		boolean oldmbNuero = this.mb_nuero;
		mb_nuero = mbNuero;
		firePropertyChange(PROPERTYNAME_MB_NUERO, oldmbNuero, mbNuero);
	}

	public void setMcsDate2(Date mcsDate2) {
		Date oldDate = getMcsDate2();
		this.mcsDate2 = mcsDate2;
		firePropertyChange(PROPERTYNAME_MCS_2_DATE, oldDate, mcsDate2);
	}

	public Date getMcsDate2() {
		return mcsDate2;
	}

	public void setLvad2(boolean lvad2) {
		boolean oldLvad = this.lvad2;
		this.lvad2 = lvad2;
		firePropertyChange(PROPERTYNAME_LVAD_2, oldLvad, lvad2);
	}

	public boolean isLvad2() {
		return lvad2;
	}

	public boolean getMb_nuero2() {
		return mb_nuero2;
	}

	public void setMb_nuero2(boolean mbNuero) {
		boolean oldmbNuero = this.mb_nuero2;
		this.mb_nuero2 = mbNuero;
		firePropertyChange(PROPERTYNAME_MB_NUERO_2, oldmbNuero, mbNuero);
	}

	public void setMcsDate3(Date mcsDate3) {
		Date oldDate = getMcsDate3();
		this.mcsDate3 = mcsDate3;
		firePropertyChange(PROPERTYNAME_MCS_3_DATE, oldDate, mcsDate3);
	}

	public Date getMcsDate3() {
		return mcsDate3;
	}

	public void setLvad3(boolean lvad3) {
		boolean oldLvad = this.lvad3;
		this.lvad3 = lvad3;
		firePropertyChange(PROPERTYNAME_LVAD_3, oldLvad, lvad3);
	}

	public boolean isLvad3() {
		return lvad3;
	}

	public boolean getMb_nuero3() {
		return mb_nuero3;
	}

	public void setMb_nuero3(boolean mbNuero) {
		boolean oldmbNuero = this.mb_nuero3;
		this.mb_nuero3 = mbNuero;
		firePropertyChange(PROPERTYNAME_MB_NUERO_3, oldmbNuero, mbNuero);
	}

	@Override
	public String getExecuteString(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

}
