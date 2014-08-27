package picabg;

import org.apache.log4j.Logger;

import patient.PatientModel;

public class PICABG extends PatientModel {
	// Instance Creation ******************************************************

	// Names of the Bound Bean Properties *************************************

	public static final String PROPERTYNAME_PATIENT_COLOR = "patientColor";

	protected String patientColor = null;

	public static final String PROPERTYNAME_AFIB_PR = "afib_pr";

	public static final String PROPERTYNAME_AGE = "age";

	public static final String PROPERTYNAME_CREAT_PR = "creat_pr";;

	// Constants **************************************************************

	public static final String PROPERTYNAME_DIABETES = "diabetes";

	public static final String PROPERTYNAME_FEMALE = "female";

	public static final String PROPERTYNAME_HCT_PR = "hct_pr";

	public static final String PROPERTYNAME_HEIGHT = "height";

	public static final String PROPERTYNAME_HX_COPD = "hx_copd";

	public static final String PROPERTYNAME_HX_DMTRT = "hx_dmtrt";

	public static final String PROPERTYNAME_HX_HTN = "hx_htn";

	public static final String PROPERTYNAME_HX_MI = "hx_mi";

	public static final String PROPERTYNAME_HX_PVD = "hx_pvd";

	public static final String PROPERTYNAME_HX_SMOKE = "hx_smoke";

	public static final String PROPERTYNAME_ITA = "ita";

	public static final String PROPERTYNAME_IV_OPYRS = "iv_opyrs";

	public static final String PROPERTYNAME_LAD = "lad";

	public static final String PROPERTYNAME_LCX = "lcx";

	public static final String PROPERTYNAME_LMT = "lmt";

	public static final String PROPERTYNAME_LVCATH = "lvcath";

	public static final String PROPERTYNAME_MALE = "male";

	public static final String PROPERTYNAME_RCA = "rca";

	public static final String PROPERTYNAME_TRIG_PR = "trig_pr";

	public static final String PROPERTYNAME_WEIGHT = "weight";

	/**
	 * 
	 */
	private static final long serialVersionUID = 5810494233195589656L;

	private boolean afib_pr;

	private int age;

	// Instance Fields ********************************************************

	private double creat_pr;

	private String diabetes = "None";

	private boolean female;

	private double hct_pr;

	private double height;

	private boolean hx_copd;

	private int hx_dcdm = 0;

	private int hx_dmtrt = 0;

	private boolean hx_htn;

	private int hx_iddm = 0;

	private boolean hx_mi;

	private int hx_omdm = 0;

	private boolean hx_pvd;

	private boolean hx_smoke;

	private String ita;

	private int ita_bil = 0;

	private int ita_none = 1;

	private int ita_sin = 0;

	private int iv_opyrs = 27;

	private double lad;

	private double lcx;

	private double lmt;

	/**
	 * Logger log;
	 */
	protected Logger log = Logger.getLogger(PICABG.class);

	private String lvcath = "None";

	private int lvf_gr2 = 0;

	private int lvf_gr3 = 0;

	private int lvf_gr4 = 0;

	private boolean male;

	private double rca;

	private double trig_pr;

	private double weight;

	/**
	 * Constructs an empty <code>PICABG</code>.
	 */
	public PICABG() {
		super();
		setIta("None");
		setDiabetes("None");
		setupPropertyChangeListener(new PICABGPresentationModel(this));
		// The name of the hazard output file
		dataFileName.add("data/hmdead.dta");

	}

	public boolean getAfib_pr() {
		return afib_pr;
	}

	// Access to Bound Properties *********************************************
	public int getAge() {
		return age;
	}

	public double getCreat_pr() {
		return creat_pr;
	}

	public String getDiabetes() {
		return diabetes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see predictor.IEntryForm#getExecuteString()
	 */
	public String getExecuteString(String filename) {
		int num_anal_vars = 37;
		String ans = "";
		// Time is in years...

		// Split up the time int even time steps
		// double timeStep = modelSet.getGraphTime() / n_time_steps;

		// * Missing value indicator variables;
		int ms_size = 0;
		int ms_creap = 0;
		int ms_trigp = 0;
		int ms_dmtx = 0;

		// Transformations.
		// The derived variables.
		double in_age = 50.0 / age;

		double age2 = Math.pow(age / 50., 2);

		// print($age2);
		double agee = Math.exp(age / 50.);
		double bmi = weight / (Math.pow(height / 100., 2));
		double in_bmi = 25. / bmi;
		double wtht = weight / height;
		double w_creat_pr = creat_pr;
		if (creat_pr > 6) {
			w_creat_pr = 6;
		}

		double ln_creat = Math.log(w_creat_pr);
		double in_hct = 40. / hct_pr;

		int lad50 = 0;
		int lad70 = 0;
		int lcx50 = 0;
		int lcx70 = 0;
		int rca50 = 0;

		if (lad >= 70) {
			lad70 = 1;
		}
		if (lad >= 50) {
			lad50 = 1;
		}

		if (lcx >= 70) {
			lcx70 = 1;
		}
		if (lcx >= 50) {
			lcx50 = 1;
		}

		if (lcx >= 50) {
			rca50 = 1;
		}
		double rcaout = rca / 100.;
		int cad_sys = lad50 + lcx50 + rca50;
		int vd1 = 0;
		int vd3 = 0;

		if (cad_sys == 1) {
			vd1 = 1;
		}

		if (cad_sys == 3) {
			vd3 = 1;
		}
		double lmtpct = lmt / 100.;

		double in_opyrs = 1. / iv_opyrs;

		// We should test for the application now. This way we can post a message
		// if we're on an
		// uncompiled platform.
		log.debug(application);

		// Build the string.
		ans = application + " " + filename + " " + n_time_steps + " "
				+ modelSet.getGraphTime() + " " + num_anal_vars + " "
				+ (female ? "1" : "0") + " " + age2 + " " + in_bmi + " " + lvf_gr3
				+ " " + lvf_gr4 + " " + (hx_mi ? "1" : "0") + " "
				+ (afib_pr ? "1" : "0") + " " + (hx_smoke ? "1" : "0") + " "
				+ (hx_pvd ? "1" : "0") + " " + (hx_copd ? "1" : "0") + " "
				+ (hx_htn ? "1" : "0") + " " + hx_dmtrt + " " + ln_creat + " " + in_hct
				+ " " + lmtpct + " " + lad70 + " " + lcx70 + " " + rca50 + " "
				+ ita_none + " " + in_opyrs + " " + ms_size + " " + ms_creap + " "
				+ in_age + " " + agee + " " + wtht + " " + lvf_gr2 + " " + hx_iddm
				+ " " + hx_omdm + " " + hx_dcdm + " " + trig_pr + " " + vd1 + " " + vd3
				+ " " + rcaout + " " + ita_sin + " " + ita_bil + " " + ms_trigp + " "
				+ ms_dmtx;
		return (ans);
	}

	public boolean getFemale() {
		return female;
	}

	public double getHct_pr() {
		return hct_pr;
	}

	public double getHeight() {
		return height;
	}

	public boolean getHx_copd() {
		return hx_copd;
	}

	public boolean getHx_htn() {
		return hx_htn;
	}

	public boolean getHx_mi() {
		return hx_mi;
	}

	public boolean getHx_pvd() {
		return hx_pvd;
	}

	public boolean getHx_smoke() {
		return hx_smoke;
	}

	public String getIta() {
		return ita;
	}

	public double getLad() {
		return lad;
	}

	public double getLcx() {
		return lcx;
	}

	public double getLmt() {
		return lmt;
	}

	public String getLvcath() {
		return lvcath;
	}

	public boolean getMale() {
		return male;
	}

	public double getRca() {
		return rca;
	}

	public double getTrig_pr() {
		return trig_pr;
	}

	public double getWeight() {
		return weight;
	}

	public void setAfib_pr(boolean afib_pr) {
		boolean oldAfib_pr = this.afib_pr;
		this.afib_pr = afib_pr;
		firePropertyChange(PROPERTYNAME_AFIB_PR, oldAfib_pr, afib_pr);
	}

	public void setAge(int age) {
		int oldAge = this.age;
		this.age = age;
		firePropertyChange(PROPERTYNAME_AGE, oldAge, age);
	}

	public void setCreat_pr(double creat_pr) {
		double oldHeight = this.creat_pr;
		this.creat_pr = creat_pr;
		firePropertyChange(PROPERTYNAME_CREAT_PR, oldHeight, creat_pr);
	}

	public void setDiabetes(String diabetes) {
		String oldDiabetes = this.diabetes;
		this.diabetes = diabetes;
		hx_dcdm = 0;
		hx_omdm = 0;
		hx_iddm = 0;
		hx_dmtrt = 0;
		if (diabetes.compareTo("Diet") == 0)
			hx_dcdm = 1;
		if (diabetes.compareTo("Oral") == 0) {
			hx_dmtrt = 1;
			hx_iddm = 1;
		}
		if (diabetes.compareTo("Insulin") == 0) {
			hx_dmtrt = 1;
			hx_omdm = 1;
		}
		firePropertyChange(PROPERTYNAME_DIABETES, oldDiabetes, diabetes);
	}

	public void setFemale(boolean female) {
		boolean oldFemale = this.female;
		this.female = female;
		firePropertyChange(PROPERTYNAME_FEMALE, oldFemale, this.female);
		male = !female;
	}

	public void setHct_pr(double hct_pr) {
		double oldHeight = this.hct_pr;
		this.hct_pr = hct_pr;
		firePropertyChange(PROPERTYNAME_HCT_PR, oldHeight, hct_pr);
	}

	public void setHeight(double height) {
		double oldHeight = this.height;
		this.height = height;
		firePropertyChange(PROPERTYNAME_HEIGHT, oldHeight, height);
	}

	public void setHx_copd(boolean hx_copd) {
		boolean oldhx_copd = this.hx_copd;
		this.hx_copd = hx_copd;
		firePropertyChange(PROPERTYNAME_HX_COPD, oldhx_copd, hx_copd);
	}

	public void setHx_htn(boolean hx_htn) {
		boolean oldhx_htn = this.hx_htn;
		this.hx_htn = hx_htn;
		firePropertyChange(PROPERTYNAME_HX_HTN, oldhx_htn, hx_htn);
	}

	public void setHx_mi(boolean hx_mi) {
		boolean oldhxmi = this.hx_mi;
		this.hx_mi = hx_mi;
		firePropertyChange(PROPERTYNAME_HX_MI, oldhxmi, hx_mi);
	}

	public void setHx_pvd(boolean hx_pvd) {
		boolean oldhx_pvd = this.hx_pvd;
		this.hx_pvd = hx_pvd;
		firePropertyChange(PROPERTYNAME_HX_PVD, oldhx_pvd, hx_pvd);
	}

	public void setHx_smoke(boolean hx_smoke) {
		boolean oldSmoke = this.hx_smoke;
		this.hx_smoke = hx_smoke;
		firePropertyChange(PROPERTYNAME_HX_SMOKE, oldSmoke, hx_smoke);
	}

	public void setIta(String ita) {
		String oldIta = this.ita;
		this.ita = ita;
		ita_none = 0;
		ita_bil = 0;
		ita_sin = 0;

		if (ita.compareTo("None") == 0)
			ita_none = 1;
		if (ita.compareTo("Both") == 0)
			ita_bil = 1;
		if (ita.compareTo("Right") == 0)
			ita_sin = 1;
		if (ita.compareTo("Left") == 0)
			ita_sin = 1;

		firePropertyChange(PROPERTYNAME_ITA, oldIta, ita);
	}

	public void setLad(double lad) {
		double oldHeight = this.lad;
		this.lad = lad;
		firePropertyChange(PROPERTYNAME_LAD, oldHeight, lad);
	}

	public void setLcx(double lcx) {
		double oldHeight = this.lcx;
		this.lcx = lcx;
		firePropertyChange(PROPERTYNAME_LCX, oldHeight, lcx);
	}

	public void setLmt(double lmt) {
		double oldHeight = this.lmt;
		this.lmt = lmt;
		firePropertyChange(PROPERTYNAME_LMT, oldHeight, lmt);
	}

	public void setLvcath(String lvcath) {
		String oldlvcath = this.lvcath;
		this.lvcath = lvcath;
		lvf_gr2 = 0;
		lvf_gr3 = 0;
		lvf_gr4 = 0;

		if (lvcath.equals("Mild")) {
			lvf_gr2 = 1;
		} else if (lvcath.equals("Moderate")) {
			lvf_gr3 = 1;
		} else if (lvcath.equals("Severe")) {
			lvf_gr4 = 1;
		}
		firePropertyChange(PROPERTYNAME_LVCATH, oldlvcath, lvcath);
	}

	public void setMale(boolean male) {
		boolean oldmale = this.male;
		this.male = male;
		firePropertyChange(PROPERTYNAME_MALE, oldmale, this.male);
		female = !male;
	}

	public void setRca(double rca) {
		double oldHeight = this.rca;
		this.rca = rca;
		firePropertyChange(PROPERTYNAME_RCA, oldHeight, rca);
	}

	public void setTrig_pr(double trig_pr) {
		double oldHeight = this.trig_pr;
		this.trig_pr = trig_pr;
		firePropertyChange(PROPERTYNAME_TRIG_PR, oldHeight, trig_pr);
	}

	// ------------------------------------------------------------------------------------
	// methods
	// ------------------------------------------------------------------------------------

	public void setWeight(double weight) {
		double oldHeight = this.weight;
		this.weight = weight;
		firePropertyChange(PROPERTYNAME_WEIGHT, oldHeight, weight);
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
