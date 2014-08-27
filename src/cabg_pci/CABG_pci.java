package cabg_pci;

import java.awt.Color;
import java.util.Date;
import java.util.Vector;

import java.lang.reflect.Field;
import org.apache.log4j.Logger;

import patient.PatientModel;

import com.jgoodies.validation.Validator;

public class CABG_pci extends PatientModel {
	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = 1828915232567002308L;

	private final static int num_anal_varsCABG = 28;
	private final static int num_anal_varsDES = 25;
	private final static int num_anal_varsBMS = 26;

	private static Logger log = Logger.getLogger(CABG_pci.class);

	// Names of the Bound Bean Properties *************************************
	/* Definitions of variables in model ; */

	public static String PROPERTYNAME_BIRTHDATE = "birthDate";

	private Date birthDate;

	/* Demography; */
	private double age = 62;

	public static String PROPERTYNAME_AGE = "age";
	private boolean female = false; // female (binary: 0, 1)

	public static String PROPERTYNAME_FEMALE = "female";

	private double ht = 170; // Height in cm

	public static String PROPERTYNAME_HT = "ht";

	private double wt = 84; // Height in kg

	public static String PROPERTYNAME_WT = "wt";
	// Status;
	private boolean emgsrg = false; /* (binary: 0, 1) Emergent status */
	public static String PROPERTYNAME_EMGSRG = "emgsrg";

	// Cardiac morbidity;

	private boolean afib_pr = false; // Atrial Fibrillation (no/yes) (0,1)

	public static String PROPERTYNAME_AFIB_PR = "afib_pr";

	private double lvef = 50; // LV ejection fraction (%)

	public static String PROPERTYNAME_LVEF = "lvef";

	private int mvrgsev = 1; /* MV regurgitation grade (0,1,2,3,4) */

	public static String PROPERTYNAME_MVRGSEV = "mvrgsev";

	private int cad_sys = 2; // Number of coronary systems>=50% (0,1,2,3)

	public static String PROPERTYNAME_CAD_SYS = "cad_sys";
	private double lmt = 0.10; // LMT disease: stenosis (0.00-1.00) */

	public static String PROPERTYNAME_LMT = "lmt";

	private boolean hx_htn = false; // History of Hypertension (no/yes) (0,1) */

	public static String PROPERTYNAME_HX_HTN = "hx_htn";
	private double bpsyst = 130; // Systolic Blood Pressure
	public static String PROPERTYNAME_BPSYST = "bpsyst";
	private double bpdias = 80; // Diastolic Blood Pressure

	public static String PROPERTYNAME_BPDIAS = "bpdias";
	// Non-cardiac comorbidity;

	private boolean hx_malig = false; // History of malignancy (no/yes) (0,1)
	public static String PROPERTYNAME_HX_MALIG = "hx_malig";

	private boolean hx_cva = false; // History of stroke (no/yes) (0, 1)

	public static String PROPERTYNAME_HX_CVA = "hx_cva";

	private boolean hx_chf = false; // History of CHF (no/yes) (0, 1)

	public static String PROPERTYNAME_HX_CHF = "hx_chf";

	private boolean hx_copd = false; // History of COPD (no/yes) (0, 1)

	public static String PROPERTYNAME_HX_COPD = "hx_copd";

	private boolean hx_diab = false; // History of DIAB (no/yes) (0, 1)

	public static String PROPERTYNAME_HX_DIAB = "hx_diab";

	private boolean hx_smoke = false; // History of smoking (no/yes) (0, 1)

	public static String PROPERTYNAME_HX_SMOKE = "hx_smoke";

	private boolean hx_rnldz = false; // History of renal disease (no/yes) (0,
	// 1)

	public static String PROPERTYNAME_HX_RNLDZ = "hx_rnldz";

	private boolean hx_fcad = false; // Family history of CAD (no/yes) (0,
	// 1)

	public static String PROPERTYNAME_HX_FCAD = "hx_fcad";
	private double creat_pr = 1.1; // 

	public static String PROPERTYNAME_CREAT_PR = "creat_pr";

	private double hct_pr = 37; // Hematocrit (%)

	public static String PROPERTYNAME_HCT_PR = "hct_pr";

	private double bun_pr = 20; // Blood urea nitrogen (mg/dL)

	public static String PROPERTYNAME_BUN_PR = "bun_pr";
	/* Experience; */

	public static String PROPERTYNAME_EXECUTE_BMS = "executeBMS";
	private boolean executeBMS = true;

	public static String PROPERTYNAME_EXECUTE_DES = "executeDES";
	private boolean executeDES = true;

	public static String PROPERTYNAME_EXECUTE_CABG = "executeCABG";
	private boolean executeCABG = true;

	public static String PROPERTYNAME_COLOR_CABG = "colorCABG";
	private String colorCABG = "BLACK";

	public static String PROPERTYNAME_COLOR_BMS = "colorBMS";
	private String colorBMS = "BLUE";

	public static String PROPERTYNAME_COLOR_DES = "colorDES";
	private String colorDES = "RED";

	public final static String BMSfilename = "data/cabg_pci.BMS.xpt.dta";
	public final static String CABGfilename = "data/cabg_pci.CABG.xpt.dta";
	public final static String DESfilename = "data/cabg_pci.DES.xpt.dta";

	/**
	 * Constructs an empty <code>TransplantSupport</code>.
	 */
	public CABG_pci() {
		super();
		setupPropertyChangeListener(new CABG_pciPresentationModel(this));

		// This model has 3 curves... or actually we analyse 3 models
		dataFileName.add(CABGfilename);
		dataFileName.add(DESfilename);
		dataFileName.add(BMSfilename);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see predictor.IEntryForm#getExecuteString()
	 */
	public String getExecuteString(String filename) {

		// SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		// If we are not supposed to execute this model, return a null.
		if (filename.equals(CABGfilename) && !isExecuteCABG()) {
			return null;
		} else if (filename.equals(BMSfilename) && !isExecuteBMS()) {
			return null;
		} else if (filename.equals(DESfilename) && !isExecuteDES()) {
			return null;
		}

		String ans = "";
		/*
		 * The input variables...
		 */
		double agels = age;

		// * Missing value indicator variables;

		// Transformations.
		double agee = Math.exp(agels / 50.0);
		double in_age = 50.0 / agels;
		double ln_age = Math.log(agels);
		double in2age = in_age * in_age;
		double bmi = wt / Math.pow((ht / 100.), 2);
		double in_bmi = 40 / bmi;
		double ln_bmi = Math.log(bmi);
		double ln_creat = Math.log(creat_pr);
		double ln_bun = Math.log(bun_pr);
		double ln_hct = Math.log(hct_pr);
		double crcl_pr = (140 - agels) * wt / (72 * creat_pr);
		if (female) {
			crcl_pr = 0.85 * crcl_pr;
		}
		double gfr_pr = Math.exp(5.228 - 1.154 * ln_creat - 0.203 * ln_age
				- 0.299 * (female ? 1 : 0));

		boolean vd3 = (cad_sys == 3);
		boolean lmt70 = (lmt >= 0.70);
		boolean mvrgrg = (mvrgsev > 0);
		/*
		 * double bun2 = Math.pow((bun_pr / 20.0), 2); double in_chol = (100 /
		 * chol_pr); double in_hct = (1 / hct_pr); double ln_bun =
		 * Math.log(bun_pr); double ln_hct = Math.log(hct_pr); double ln_blrbn =
		 * Math.log(blrbn_pr);
		 */
		// Time is in years...
		// We should test for the application now. This way we can post a
		// message if we're on an uncompiled platform.
		log.debug(application);

		if (filename.equals(DESfilename)) {
			// Build the string.
			ans = application + " " + filename + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_varsDES + " "
					+ agee + " " + (emgsrg ? 1 : 0) + " " + in_bmi + " "
					+ (hx_cva ? 1 : 0) + " " + (hx_chf ? 1 : 0) + " "
					+ (hx_rnldz ? 1 : 0) + " " + (hx_copd ? 1 : 0) + " "
					+ (hx_diab ? 1 : 0) + " " + crcl_pr + " "
					+ (afib_pr ? 1 : 0) + " " + (mvrgrg ? 1 : 0) + " " + lvef
					+ " " + (vd3 ? 1 : 0) + " " + bpsyst + " " + bpdias + " "
					+ (hx_smoke ? 1 : 0) + " " + (hx_malig ? 1 : 0) + " "
					+ gfr_pr + " " + cad_sys + " " + (female ? 1 : 0) + " "
					+ ln_bun + " " + ln_hct + " " + lmt + " "
					+ (hx_fcad ? 1 : 0) + " " + ln_creat;
		} else if (filename.equals(BMSfilename)) {
			// Build the string.
			ans = application + " " + filename + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_varsBMS + " "
					+ agee + " " + (emgsrg ? 1 : 0) + " " + lvef + " " + bpsyst
					+ " " + (afib_pr ? 1 : 0) + " " + (hx_copd ? 1 : 0) + " "
					+ (hx_cva ? 1 : 0) + " " + (hx_rnldz ? 1 : 0) + " "
					+ (vd3 ? 1 : 0) + " " + (hx_diab ? 1 : 0) + " " + bpdias
					+ " " + ln_bmi + " " + (mvrgrg ? 1 : 0) + " "
					+ (hx_chf ? 1 : 0) + " " + (hx_malig ? 1 : 0) + " "
					+ (hx_smoke ? 1 : 0) + " " + (female ? 1 : 0) + " "
					+ ln_creat + " " + ln_bun + " " + cad_sys + " "
					+ (hx_fcad ? 1 : 0) + " " + lmt + " " + gfr_pr + " "
					+ (hx_htn ? 1 : 0) + " " + (lmt70 ? 1 : 0) + " " + in2age;
		} else {
			// Build the string.
			ans = application + " " + filename + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_varsCABG + " "
					+ agee + " " + (emgsrg ? 1 : 0) + " " + in_bmi + " "
					+ ln_bmi + " " + (hx_cva ? 1 : 0) + " " + (hx_chf ? 1 : 0)
					+ " " + (hx_copd ? 1 : 0) + " " + (hx_rnldz ? 1 : 0) + " "
					+ (hx_diab ? 1 : 0) + " " + hct_pr + " "
					+ (afib_pr ? 1 : 0) + " " + (mvrgrg ? 1 : 0) + " " + lvef
					+ " " + bpsyst + " " + bpdias + " " + age + " " + ln_age
					+ " " + (female ? 1 : 0) + " " + wt + " "
					+ (hx_malig ? 1 : 0) + " " + (hx_smoke ? 1 : 0) + " "
					+ ln_bun + " " + creat_pr + " " + gfr_pr + " " + crcl_pr
					+ " " + cad_sys + " " + lmt + " " + (hx_fcad ? 1 : 0);
		}
		log.debug(ans);
		/*
		 * log.debug("--------- inputs ----------");
		 */

		return (ans);
	}

	public Validator<CABG_pci> getValidator() {
		return (Validator<CABG_pci>) new CABG_pciValidator(this);
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		double oldAge = getAge();
		this.age = age;
		firePropertyChange(PROPERTYNAME_AGE, oldAge, age);

	}

	// * Cardiac morbidity;

	/**
	 * @return the hx_cva
	 */
	public boolean isHx_cva() {
		return hx_cva;
	}

	/**
	 * @return the hx_rnldz
	 */
	public boolean isHx_rnldz() {
		return hx_rnldz;
	}

	/**
	 * @return the hx_copd
	 */
	public boolean isHx_copd() {
		return hx_copd;
	}

	/**
	 * @param hct_pr
	 *            the hct_pr to set
	 */
	public void setHct_pr(double hct_pr) {
		double oldValue = this.hct_pr;
		this.hct_pr = hct_pr;
		firePropertyChange(PROPERTYNAME_HCT_PR, oldValue, hct_pr);
	}

	/**
	 * @param hx_copd
	 *            the hx_copd to set
	 */
	public void setHx_copd(boolean hx_copd) {
		boolean oldValue = this.hx_copd;
		this.hx_copd = hx_copd;
		firePropertyChange(PROPERTYNAME_HX_COPD, oldValue, hx_copd);
	}

	/**
	 * @param hx_cva
	 *            the hx_cva to set
	 */
	public void setHx_cva(boolean hx_cva) {
		boolean oldValue = this.hx_cva;
		this.hx_cva = hx_cva;
		firePropertyChange(PROPERTYNAME_HX_CVA, oldValue, hx_cva);
	}

	/**
	 * @param hx_rnldz
	 *            the hx_rnldz to set
	 */
	public void setHx_rnldz(boolean hx_rnldz) {
		boolean oldValue = this.hx_rnldz;
		this.hx_rnldz = hx_rnldz;
		firePropertyChange(PROPERTYNAME_HX_RNLDZ, oldValue, hx_rnldz);
	}

	// Non-cardiac comorbidity;
	public boolean getHx_cva() {
		return (hx_cva);
	}

	public boolean getHx_copd() {
		return (hx_copd);
	}

	public double getHct_pr() {
		return (hct_pr);
	}

	public boolean getHx_rnldz() {
		return (hx_rnldz);
	}

	public Date getBirthDate() {
		return (birthDate);
	}

	public void setBirthDate(Date newDate) {
		Date oldDate = getBirthDate();
		birthDate = newDate;
		Date today = new Date();

		firePropertyChange(PROPERTYNAME_BIRTHDATE, oldDate, newDate);
		if (birthDate != null) {
			setAge(datediff(today, birthDate) / 365.2425);
		}
	}

	public double getBun_pr() {
		return bun_pr;
	}

	public void setBun_pr(double bun_pr) {
		double oldBun_pr = this.bun_pr;
		this.bun_pr = bun_pr;
		firePropertyChange(PROPERTYNAME_BUN_PR, oldBun_pr, bun_pr);
	}

	public boolean isFemale() {
		return female;
	}

	public void setFemale(boolean female) {
		boolean fml = this.female;
		this.female = female;
		firePropertyChange(PROPERTYNAME_FEMALE, fml, female);
	}

	public double getHt() {
		return ht;
	}

	public void setHt(double ht) {
		double oHt = this.ht;
		this.ht = ht;
		firePropertyChange(PROPERTYNAME_HT, oHt, ht);
	}

	public double getWt() {
		return wt;
	}

	public void setWt(double wt) {

		double oHt = this.wt;
		this.wt = wt;
		firePropertyChange(PROPERTYNAME_WT, oHt, wt);
	}

	public boolean isEmgsrg() {
		return emgsrg;
	}

	public void setEmgsrg(boolean emgsrg) {
		boolean old = this.emgsrg;
		this.emgsrg = emgsrg;
		firePropertyChange(PROPERTYNAME_EMGSRG, old, emgsrg);
	}

	public boolean isAfib_pr() {
		return afib_pr;
	}

	public void setAfib_pr(boolean afib_pr) {
		boolean old = this.afib_pr;
		this.afib_pr = afib_pr;
		firePropertyChange(PROPERTYNAME_AFIB_PR, old, afib_pr);
	}

	public double getLvef() {
		return lvef;
	}

	public void setLvef(double lvef) {
		double old = this.lvef;
		this.lvef = lvef;
		firePropertyChange(PROPERTYNAME_LVEF, old, lvef);
	}

	public String getMvrgsev() {
		return Integer.toString(mvrgsev);
	}

	public void setMvrgsev(String mvrgsev) {
		log.debug("mvrgsev:" + mvrgsev);
		int old = this.mvrgsev;
		this.mvrgsev = Integer.parseInt(mvrgsev);
		firePropertyChange(PROPERTYNAME_MVRGSEV, old, this.mvrgsev);
	}

	public String getCad_sys() {
		return Integer.toString(cad_sys);
	}

	public void setCad_sys(String cad_sys) {
		log.debug("cad_sys:" + cad_sys);
		int old = this.cad_sys;
		this.cad_sys = Integer.parseInt(cad_sys);
		firePropertyChange(PROPERTYNAME_CAD_SYS, old, this.cad_sys);
	}

	public double getLmt() {
		return lmt;
	}

	public void setLmt(double lmt) {
		double old = this.lmt;
		this.lmt = lmt;
		firePropertyChange(PROPERTYNAME_LMT, old, lmt);
	}

	public boolean isHx_htn() {
		return hx_htn;
	}

	public void setHx_htn(boolean hx_htn) {
		boolean old = this.hx_htn;
		this.hx_htn = hx_htn;
		firePropertyChange(PROPERTYNAME_HX_HTN, old, hx_htn);
	}

	public double getBpsyst() {
		return bpsyst;
	}

	public void setBpsyst(double bpsyst) {
		double old = this.bpsyst;
		this.bpsyst = bpsyst;
		firePropertyChange(PROPERTYNAME_BPSYST, old, bpsyst);
	}

	public double getBpdias() {
		return bpdias;
	}

	public void setBpdias(double bpdias) {
		double old = this.bpdias;
		this.bpdias = bpdias;
		firePropertyChange(PROPERTYNAME_BPDIAS, old, bpdias);
	}

	public boolean isHx_malig() {
		return hx_malig;
	}

	public void setHx_malig(boolean hx_malig) {
		boolean old = this.hx_malig;
		this.hx_malig = hx_malig;
		firePropertyChange(PROPERTYNAME_HX_MALIG, old, hx_malig);
	}

	public boolean isHx_chf() {
		return hx_chf;
	}

	public void setHx_chf(boolean hx_chf) {
		boolean old = this.hx_chf;
		this.hx_chf = hx_chf;
		firePropertyChange(PROPERTYNAME_HX_CHF, old, hx_chf);
	}

	public boolean isHx_diab() {
		return hx_diab;
	}

	public void setHx_diab(boolean hx_diab) {
		boolean old = this.hx_diab;
		this.hx_diab = hx_diab;
		firePropertyChange(PROPERTYNAME_HX_DIAB, old, hx_diab);
	}

	public boolean isHx_smoke() {
		return hx_smoke;
	}

	public void setHx_smoke(boolean hx_smoke) {
		boolean old = this.hx_smoke;
		this.hx_smoke = hx_smoke;
		firePropertyChange(PROPERTYNAME_HX_SMOKE, old, hx_smoke);
	}

	public boolean isHx_fcad() {
		return hx_fcad;
	}

	public void setHx_fcad(boolean hx_fcad) {
		boolean old = this.hx_fcad;
		this.hx_fcad = hx_fcad;
		firePropertyChange(PROPERTYNAME_HX_FCAD, old, hx_fcad);
	}

	public double getCreat_pr() {
		return creat_pr;
	}

	public void setCreat_pr(double creat_pr) {
		double old = this.creat_pr;
		this.creat_pr = creat_pr;
		firePropertyChange(PROPERTYNAME_CREAT_PR, old, creat_pr);
	}

	public boolean isExecuteCABG() {
		return executeCABG;
	}

	public void setExecuteCABG(boolean executeCABG) {
		boolean oldExec = this.executeCABG;
		this.executeCABG = executeCABG;
		firePropertyChange(PROPERTYNAME_EXECUTE_CABG, oldExec, executeCABG);
	}

	public boolean isExecuteBMS() {
		return executeBMS;
	}

	public void setExecuteBMS(boolean executeBMS) {
		boolean oldExec = this.executeBMS;
		this.executeBMS = executeBMS;
		firePropertyChange(PROPERTYNAME_EXECUTE_BMS, oldExec, executeBMS);
	}

	public boolean isExecuteDES() {
		return executeDES;
	}

	public void setExecuteDES(boolean executeDES) {
		boolean oldExec = this.executeDES;
		this.executeDES = executeDES;
		firePropertyChange(PROPERTYNAME_EXECUTE_DES, oldExec, executeDES);
	}

	public String getColorCABG() {
		log.debug("Getting ColorCABG: " + colorCABG);
		if (colorCABG == null) {
			setColorCABG("GREEN");
		}
		return colorCABG;
	}

	public void setColorCABG(String patientColor) {
		log.debug("colorCABG: " + colorCABG);
		String oldColor = this.colorCABG;
		this.colorCABG = patientColor;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_COLOR_CABG, oldColor, patientColor);
	}

	public String getColorBMS() {
		log.debug("Getting ColorBMS: " + colorBMS);
		if (colorBMS == null)
			setColorBMS("blue");
		return colorBMS;
	}

	public void setColorBMS(String patientColor) {
		log.debug("colorBMS: " + colorBMS);
		String oldColor = this.colorBMS;
		this.colorBMS = patientColor;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_COLOR_BMS, oldColor, patientColor);
	}

	public String getColorDES() {
		log.debug("Getting ColorDES: " + colorDES);
		if (colorDES == null)
			setColorDES("orange");
		return colorDES;
	}

	public void setColorDES(String patientColor) {
		log.debug("colorDES: " + colorDES);
		String oldColor = this.colorDES;
		this.colorDES = patientColor;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_COLOR_DES, oldColor, patientColor);
	}

	public Vector<Color> getColor() {
		Vector<Color> clr = new Vector<Color>();
		try {
			Field field = Class.forName("org.jfree.chart.ChartColor").getField(
					getColorCABG());
			clr.add((Color) field.get(null));
			field = Class.forName("org.jfree.chart.ChartColor").getField(
					getColorBMS());
			clr.add((Color) field.get(null));
			field = Class.forName("org.jfree.chart.ChartColor").getField(
					getColorDES());
			clr.add((Color) field.get(null));
		} catch (Exception e) {

		}
		log.debug(clr.toString());

		return clr;
	}

	@Override
	public String getPatientColor() {
		// TODO Auto-generated method stub

		// First we'll return the CABG color.
		return getColorCABG();
	}

}
