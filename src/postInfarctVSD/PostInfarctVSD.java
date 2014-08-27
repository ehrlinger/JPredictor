package postInfarctVSD;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import patient.PatientModel;

public class PostInfarctVSD extends PatientModel {
	// Instance Creation ******************************************************

	// Names of the Bound Bean Properties *************************************

	public static final String PROPERTYNAME_PATIENT_COLOR = "patientColor";

	protected String patientColor = null;

	private static Logger log = Logger.getLogger(PostInfarctVSD.class);

	public static String PROPERTYNAME_BUN = "bun";

	public static String PROPERTYNAME_IV_MISG = "iv_misg";

	// Constants **************************************************************

	public static String PROPERTYNAME_OPDATE = "opDate";

	public static String PROPERTYNAME_PEREFF = "pereff";

	public static String PROPERTYNAME_PVD = "pvd";

	public static String PROPERTYNAME_RV_ABNOR = "rv_abnor";

	public static String PROPERTYNAME_URINEOUTPUT = "urineOutput";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3806714762859491437L;

	public static String URINE_LABEL_0 = "Anuria (no urine output)"; /* uo_grd=0 */

	public static String URINE_LABEL_1 = "Oliguria, output < 20 mL/hr"; /* uo_grd=1 */

	public static String URINE_LABEL_2 = "Urine output > 20 mL/hr"; /* uo_grd=2 */

	private double bun;

	private Date epochDate;

	// Instance Fields ********************************************************
	private int iv_misg; /* Interval (days) from MI to operation */

	private Date opDate;

	private double opyrs;

	private boolean pereff = false; /* Presence of pericardial effusion */

	private boolean pvd = false;

	private boolean rv_abnor = false; /* Right ventricular dysfunction */

	private int uo_grd = 0;

	private String urineOutput;

	/**
	 * Constructs an empty <code>PostInfarctVSD</code>.
	 */
	public PostInfarctVSD() {
		super();

		// Set the start date of the study, so we can find iv_opyrs.
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(1969, 0, 1);
		epochDate = cal.getTime();
		cal.set(1996, 0, 1);
		opDate = cal.getTime();

		setUrineOutput(URINE_LABEL_0);
		setupPropertyChangeListener(new PostInfarctVSDPresentationModel(this));

		// The name of the hazard output file
		dataFileName.add("data/hmprdead.dta");

	}

	public double getBun() {
		return bun;
	}

	public Date getEpochDate() {
		return (epochDate);
		/*
		 * return birthDate == NO_DATE ? null : new Date(birthDate);
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see predictor.IEntryForm#getExecuteString()
	 */
	public String getExecuteString(String filename) {
		int num_anal_vars = 8;

		String ans = "";

		// Transformations.
		double in_bun = 30. / bun;
		double in_misg = 1. / iv_misg;
		double opyrs2 = Math.pow(opyrs / 10., 2.);
		double op2_misi = opyrs2 * in_misg;
		double in_uogrd = 1. / (uo_grd + 1);

		// We should test for the application now. This way we can post a message
		// if we're on an
		// uncompiled platform.
		log.debug(application);

		// Build the string.
		ans = application + " " + filename + " " + n_time_steps + " "
				+ modelSet.getGraphTime() + " " + num_anal_vars + " " + in_misg + " "
				+ opyrs2 + " " + (rv_abnor ? "1" : "0") + " " + op2_misi + " "
				+ in_uogrd + " " + in_bun + " " + (pereff ? "1" : "0") + " "
				+ (pvd ? "1" : "0");
		return (ans);
	}

	// Access to Bound Properties *********************************************
	public int getIv_misg() {
		return iv_misg;
	}

	public Date getOpDate() {
		return (opDate);
		/*
		 * return birthDate == NO_DATE ? null : new Date(birthDate);
		 */
	}

	public boolean getPereff() {
		return pereff;
	}

	public boolean getPvd() {
		return pvd;
	}

	public boolean getRv_abnor() {
		return rv_abnor;
	}

	public String getUrineOutput() {
		return urineOutput;
	}

	public void setBun(double bun) {
		double oldbun = this.bun;
		this.bun = bun;
		firePropertyChange(PROPERTYNAME_BUN, oldbun, bun);
	}

	public void setIv_misg(int iv_misg) {
		int oldiv_misg = this.iv_misg;
		this.iv_misg = iv_misg;
		firePropertyChange(PROPERTYNAME_IV_MISG, oldiv_misg, iv_misg);
	}

	/**
	 * Sets this patients birth date.
	 * 
	 * @param newDate
	 *          the birth date to be set
	 * 
	 * @see #getBirthDate()
	 * @see #getListDate()
	 */
	public void setOpDate(Date newDate) {
		Date oldDate = getOpDate();
		opDate = newDate;

		opyrs = datediff(opDate, epochDate) / 365.2524;
		firePropertyChange(PROPERTYNAME_OPDATE, oldDate, newDate);
	}

	public void setPereff(boolean pereff) {
		boolean oldpereff = this.pereff;
		this.pereff = pereff;
		firePropertyChange(PROPERTYNAME_PEREFF, oldpereff, this.pereff);
	}

	public void setPvd(boolean pvd) {
		boolean oldpvd = this.pvd;
		this.pvd = pvd;
		firePropertyChange(PROPERTYNAME_PVD, oldpvd, this.pvd);
	}

	public void setRv_abnor(boolean rv_abnor) {
		boolean oldrv_abnor = this.rv_abnor;
		this.rv_abnor = rv_abnor;
		firePropertyChange(PROPERTYNAME_RV_ABNOR, oldrv_abnor, this.rv_abnor);
	}

	public void setUrineOutput(String urineOutput) {
		String oldUrineOutput = this.urineOutput;
		this.urineOutput = urineOutput;

		if (urineOutput.equals(URINE_LABEL_0)) {
			uo_grd = 0;
		} else if (urineOutput.equals(URINE_LABEL_1)) {
			uo_grd = 1;
		} else if (urineOutput.equals(URINE_LABEL_2)) {
			uo_grd = 2;
		}
		firePropertyChange(PROPERTYNAME_URINEOUTPUT, oldUrineOutput, urineOutput);
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

}
