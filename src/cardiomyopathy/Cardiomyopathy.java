package cardiomyopathy;

import java.awt.Color;
import java.util.Date;
import java.util.Vector;

import java.lang.reflect.Field;
import org.apache.log4j.Logger;

import patient.PatientModel;

import com.jgoodies.validation.Validator;

public class Cardiomyopathy extends PatientModel {
	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = 1828915232567002308L;

	private final static int num_anal_varsTXPL = 32;
	private final static int num_anal_varsSVR = 30;
	private final static int num_anal_varsCBGV = 30;
	private final static int num_anal_varsCABG = 31;

	private static Logger log = Logger.getLogger(Cardiomyopathy.class);

	// Names of the Bound Bean Properties *************************************
	/* Definitions of variables in model ; */

	public static String PROPERTYNAME_BIRTHDATE = "birthDate";

	private Date birthDate;

	/* Demography; */
	private double age = 62;

	public static String PROPERTYNAME_AGE = "age";

	// * Cardiac morbidity;
	private double iv_pmi = 3; // Interval (years) from MI to operation

	public static String PROPERTYNAME_IV_PMI = "iv_pmi";

	private double ef_com = 20; // LV ejection fraction (%)

	public static String PROPERTYNAME_EF_COM = "ef_com";

	private boolean nyha34 = false; // NYHA function class 1/II vs III/IV
	// (binary: 0, 1)

	public static String PROPERTYNAME_NYHA_GROUP = "nyha";

	private int cac_pr = 2; // Cadian Angina Class:possible values: (0, 1, 2, 3,
	// 4)

	public static String PROPERTYNAME_CAC_PR = "canadianAngina";

	private boolean afib_pr = false; // Atrial Fibrillation (no/yes) (0,1)

	public static String PROPERTYNAME_AFIB_PR = "afib_pr";

	private boolean chb_pr = false; // Complete heart block/pacer (no/yes) (0,1)

	public static String PROPERTYNAME_CHB_PR = "chb_pr";

	private boolean varr_pr = false; // Complete heart block/pacer (no/yes)
	// (0,1)

	public static String PROPERTYNAME_VARR_PR = "varr_pr";

	private boolean avrgrg = false; // AV regurgitation (no/yes) (0, 1)

	public static String PROPERTYNAME_AVRGRG = "avrgrg";

	private boolean lmtany = true; // LMT disease: stenosis>0% (no/yes) (0, 1)

	public static String PROPERTYNAME_LMTANY = "lmtany";

	private boolean rca50 = true; // RCA disease: stenosis>=50% (no/yes) (0, 1)

	public static String PROPERTYNAME_RCA50 = "rca50";

	private boolean lcx70 = true; // LCX disease: stenosis>=70% (no/yes) (0, 1)

	public static String PROPERTYNAME_LCX70 = "lcx70";

	private boolean vd3 = true; // 3-vessel disease all LCx, LAD, and RCA
	// stenosis >=50% (no/yes) (0, 1)
	public static String PROPERTYNAME_VD3 = "vd3";

	// Non-cardiac comorbidity;
	private boolean hx_cva = false; // History of stroke (no/yes) (0, 1)

	public static String PROPERTYNAME_HX_CVA = "hx_cva";

	private boolean hx_copd = false; // History of COPD (no/yes) (0, 1)

	public static String PROPERTYNAME_HX_COPD = "hx_copd";

	private boolean hx_popdz = false; // History of popliteal disease (no/yes)
	// (0, 1)

	public static String PROPERTYNAME_HX_POPDZ = "hx_popdz";

	private boolean hx_dmtrt = false; // History of treated diabetes (no/yes)
	// (0, 1)

	public static String PROPERTYNAME_DIABETES = "diabetes";

	private boolean hx_iddm = false; // History of insulin dependent diabetes
	// (no/yes)

	// (0, 1)
	public static String PROPERTYNAME_HX_PVD = "hx_pvd";

	private boolean hx_pvd = false; // History of peripheral vascular disease
	// (no/yes)
	// (0, 1)

	private double chol_pr = 168; // Cholesterol (mg/dL)

	public static String PROPERTYNAME_CHOL_PR = "chol_pr";

	private double hct_pr = 37; // Hematocrit (%)

	public static String PROPERTYNAME_HCT_PR = "hct_pr";

	private boolean hx_rnldz = false; // History of renal disease (no/yes) (0,
	// 1)

	public static String PROPERTYNAME_HX_RNLDZ = "hx_rnldz";

	private double bun_pr = 24; // Blood urea nitrogen (mg/dL)

	public static String PROPERTYNAME_BUN_PR = "bun_pr";
	private double blrbn_pr = 0.8; // Blood urea nitrogen (mg/dL)

	public static String PROPERTYNAME_BLRBN_PR = "blrbn_pr";
	/* Experience; */
	public static String PROPERTYNAME_SURGERYDATE = "surgeryDate";

	private Date surgeryDate;

	public static String PROPERTYNAME_MIDATE = "miDate";

	private Date miDate;

	public static String PROPERTYNAME_EXECUTE_CABG = "executeCABG";
	private boolean executeCABG = true;

	public static String PROPERTYNAME_EXECUTE_CBGV = "executeCBGV";
	private boolean executeCBGV = true;

	public static String PROPERTYNAME_EXECUTE_SVR = "executeSVR";
	private boolean executeSVR = true;

	public static String PROPERTYNAME_EXECUTE_TXPL = "executeTXPL";
	private boolean executeTXPL = true;

	public static String PROPERTYNAME_COLOR_CABG = "colorCABG";
	private String colorCABG = "GREEN";

	public static String PROPERTYNAME_COLOR_CBGV = "colorCBGV";
	private String colorCBGV = "BLUE";

	public static String PROPERTYNAME_COLOR_SVR = "colorSVR";
	private String colorSVR = "ORANGE";

	public static String PROPERTYNAME_COLOR_TXPL = "colorTXPL";
	private String colorTXPL = "RED";

	public final static String CABGfilename = "data/cardiomyopathy.CABG.xpt.dta";
	public final static String CBGVfilename = "data/cardiomyopathy.CABG+MVA.xpt.dta";
	public final static String SVRfilename = "data/cardiomyopathy.CABG+SVR.xpt.dta";
	public final static String TXPLfilename = "data/cardiomyopathy.LCTx.xpt.dta";

	/**
	 * Constructs an empty <code>TransplantSupport</code>.
	 */
	public Cardiomyopathy() {
		super();
		setupPropertyChangeListener(new CardiomyopathyPresentationModel(this));

		// This model has 4 curves... or actually we analyse four models
		dataFileName.add(CABGfilename);
		dataFileName.add(CBGVfilename);
		dataFileName.add(SVRfilename);
		dataFileName.add(TXPLfilename);

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
		} else if (filename.equals(CBGVfilename) && !isExecuteCBGV()) {
			return null;
		} else if (filename.equals(SVRfilename) && !isExecuteSVR()) {
			return null;
		} else if (filename.equals(TXPLfilename) && !isExecuteTXPL()) {
			return null;
		}

		String ans = "";
		/*
		 * The input variables...
		 */
		double agels = age;

		// * Missing value indicator variables;
		boolean ms_pmi = false; // missing value flag
		boolean ms_cadsy = false;// missing value flag
		boolean ms_efc = false;// missing value flag
		boolean ms_cholp = false; // missing value flag
		boolean ms_hctp = false;
		boolean ms_blrbp = false;// missing value flag

		// Transformations.
		double agee = Math.exp(agels / 50.0);
		double in_age = 50.0 / agels;
		double ln_age = Math.log(agels);

		double bun2 = Math.pow((bun_pr / 20.0), 2);

		double ln_ef = Math.log(ef_com);
		double ef2 = Math.pow((ef_com / 20.0), 2);

		double ivpmi2 = Math.pow((iv_pmi / 4.0), 2);
		double in_ivpmi = (4 / (iv_pmi + 1.0));
		double ln_ivpmi = Math.log(iv_pmi + 1.0);

		double in_chol = (100 / chol_pr);
		double in_hct = (1 / hct_pr);
		double ln_bun = Math.log(bun_pr);
		double ln_hct = Math.log(hct_pr);
		double ln_blrbn = Math.log(blrbn_pr);

		// Time is in years...
		// We should test for the application now. This way we can post a
		// message if we're on an uncompiled platform.
		log.debug(application);

		if (filename.equals(TXPLfilename)) {

			// The transplant model is different.
			ans = application + " " + filename + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_varsTXPL + " "
					+ ln_age + " " + (nyha34 ? 1 : 0) + " " + cac_pr + " "
					+ ln_ef + " " + (varr_pr ? 1 : 0) + " " + (vd3 ? 1 : 0)
					+ " " + (hx_cva ? 1 : 0) + " " + bun2 + " " + blrbn_pr
					+ " " + (ms_cadsy ? 1 : 0) + " " + (ms_efc ? 1 : 0) + " "
					+ agee + " " + ef2 + " " + (afib_pr ? 1 : 0) + " "
					+ (chb_pr ? 1 : 0) + " " + (lmtany ? 1 : 0) + " "
					+ (rca50 ? 1 : 0) + " " + (lcx70 ? 1 : 0) + " "
					+ (avrgrg ? 1 : 0) + " " + ivpmi2 + " "
					+ (hx_popdz ? 1 : 0) + " " + (hx_copd ? 1 : 0) + " "
					+ (hx_dmtrt ? 1 : 0) + " " + (hx_iddm ? 1 : 0) + " "
					+ in_chol + " " + (hx_rnldz ? 1 : 0) + " " + ln_bun + " "
					+ (hx_pvd ? 1 : 0) + " " + ln_hct + " " + (ms_pmi ? 1 : 0)
					+ " " + (ms_cholp ? 1 : 0) + " " + (ms_hctp ? 1 : 0);

		} else if (filename.equals(SVRfilename)) {
			// Build the string.
			ans = application + " " + filename + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_varsSVR + " "
					+ ln_age + " " + (nyha34 ? 1 : 0) + " " + cac_pr + " "
					+ ln_ef + " " + (varr_pr ? 1 : 0) + " " + (vd3 ? 1 : 0)
					+ " " + (hx_cva ? 1 : 0) + " " + bun2 + " " + ln_blrbn
					+ " " + (ms_blrbp ? 1 : 0) + " " + agee + " " + ef2 + " "
					+ (afib_pr ? 1 : 0) + " " + (chb_pr ? 1 : 0) + " "
					+ (lmtany ? 1 : 0) + " " + (rca50 ? 1 : 0) + " "
					+ (lcx70 ? 1 : 0) + " " + (avrgrg ? 1 : 0) + " " + ln_ivpmi
					+ " " + (hx_popdz ? 1 : 0) + " " + (hx_copd ? 1 : 0) + " "
					+ (hx_dmtrt ? 1 : 0) + " " + (hx_iddm ? 1 : 0) + " "
					+ in_chol + " " + (hx_rnldz ? 1 : 0) + " " + ln_bun + " "
					+ (hx_pvd ? 1 : 0) + " " + ln_hct + " " + (ms_pmi ? 1 : 0)
					+ " " + (ms_cholp ? 1 : 0);
		} else if (filename.equals(CBGVfilename)) {
			// Build the string.
			ans = application + " " + filename + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_varsCBGV + " "
					+ in_age + " " + (nyha34 ? 1 : 0) + " " + cac_pr + " "
					+ ln_ef + " " + (varr_pr ? 1 : 0) + " " + (vd3 ? 1 : 0)
					+ " " + (hx_cva ? 1 : 0) + " " + bun2 + " " + blrbn_pr
					+ " " + agee + " " + ef2 + " " + (afib_pr ? 1 : 0) + " "
					+ (chb_pr ? 1 : 0) + " " + (lmtany ? 1 : 0) + " "
					+ (rca50 ? 1 : 0) + " " + (lcx70 ? 1 : 0) + " "
					+ (avrgrg ? 1 : 0) + " " + ivpmi2 + " "
					+ (hx_popdz ? 1 : 0) + " " + (hx_copd ? 1 : 0) + " "
					+ (hx_dmtrt ? 1 : 0) + " " + (hx_iddm ? 1 : 0) + " "
					+ in_chol + " " + (hx_rnldz ? 1 : 0) + " " + ln_bun + " "
					+ (hx_pvd ? 1 : 0) + " " + ln_hct + " " + (ms_pmi ? 1 : 0)
					+ " " + (ms_cholp ? 1 : 0) + " " + (ms_hctp ? 1 : 0);
		} else {
			// Build the string.
			ans = application + " " + filename + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_varsCABG + " "
					+ ln_age + " " + (nyha34 ? 1 : 0) + " " + cac_pr + " "
					+ ln_ef + " " + (varr_pr ? 1 : 0) + " " + (vd3 ? 1 : 0)
					+ " " + (hx_cva ? 1 : 0) + " " + bun2 + " " + blrbn_pr
					+ " " + (ms_efc ? 1 : 0) + " " + agee + " " + ef2 + " "
					+ (afib_pr ? 1 : 0) + " " + (chb_pr ? 1 : 0) + " "
					+ (lmtany ? 1 : 0) + " " + (rca50 ? 1 : 0) + " "
					+ (lcx70 ? 1 : 0) + " " + (avrgrg ? 1 : 0) + " " + in_ivpmi
					+ " " + (hx_popdz ? 1 : 0) + " " + (hx_copd ? 1 : 0) + " "
					+ (hx_dmtrt ? 1 : 0) + " " + (hx_iddm ? 1 : 0) + " "
					+ in_chol + " " + (hx_rnldz ? 1 : 0) + " " + ln_bun + " "
					+ (hx_pvd ? 1 : 0) + " " + in_hct + " " + (ms_pmi ? 1 : 0)
					+ " " + (ms_cholp ? 1 : 0) + " " + (ms_hctp ? 1 : 0);
		}
		log.debug(ans);
		/*
		 * log.debug("--------- inputs ----------");
		 */

		return (ans);
	}

	public Validator<Cardiomyopathy> getValidator() {
		return (Validator<Cardiomyopathy>) new CardiomyopathyValidator(this);
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
	public double getIv_pmi() {
		return iv_pmi;
	}

	public void setIv_pmi(double iv_pmi) {
		double oldIv_pmi = getIv_pmi();
		this.iv_pmi = iv_pmi;
		firePropertyChange(PROPERTYNAME_IV_PMI, oldIv_pmi, iv_pmi);
	}

	public double getEf_com() {
		return ef_com;
	}

	public void setEf_com(double ef_com) {
		double oldValue = getEf_com();
		this.ef_com = ef_com;
		firePropertyChange(PROPERTYNAME_EF_COM, oldValue, ef_com);
	}

	String nyha = "";

	public String getNyha() {
		return nyha;
	}

	public void setNyha(String nyha) {
		String oldValue = this.nyha;
		this.nyha = nyha;
		nyha34 = false;

		log.debug(nyha);

		if (nyha.compareTo("III") == 0 || nyha.compareTo("IV") == 0)
			nyha34 = true;

		log.debug(nyha34);
		firePropertyChange(PROPERTYNAME_NYHA_GROUP, oldValue, nyha);
	}

	String diabetes = "";

	public String getDiabetes() {
		return diabetes;
	}

	public void setDiabetes(String diabetes) {
		String oldDiabetes = this.diabetes;
		this.diabetes = diabetes;
		hx_dmtrt = false; // History of treated diabetes (no/yes) (0, 1)
		hx_iddm = false;

		if (diabetes.compareTo("Treated") == 0)
			hx_dmtrt = true;
		if (diabetes.compareTo("Insulin") == 0) {
			hx_dmtrt = true;
			hx_iddm = true;
		}

		firePropertyChange(PROPERTYNAME_DIABETES, oldDiabetes, diabetes);
	}

	String canadianAngina = "0";

	public String getCanadianAngina() {
		Integer cac = new Integer(cac_pr);
		canadianAngina = cac.toString();
		return canadianAngina;
	}

	public void setCanadianAngina(String canadianAngina) {

		log.debug("Set canadian angina: " + canadianAngina);
		String oldValue = this.canadianAngina;
		this.canadianAngina = canadianAngina;
		cac_pr = Integer.valueOf(canadianAngina).intValue();

		firePropertyChange(PROPERTYNAME_CAC_PR, oldValue, canadianAngina);
	}

	public boolean isAfib_pr() {
		return (afib_pr);
	}

	public boolean isChb_pr() {
		return (chb_pr);
	}

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
	 * @return the hx_dmtrt
	 */
	public boolean isHx_dmtrt() {
		return hx_dmtrt;
	}

	/**
	 * @return the hx_popdz
	 */
	public boolean isHx_popdz() {
		return hx_popdz;
	}

	/**
	 * @param afib_pr
	 *            the afib_pr to set
	 */
	public void setAfib_pr(boolean afib_pr) {
		boolean oldValue = this.afib_pr;
		this.afib_pr = afib_pr;
		firePropertyChange(PROPERTYNAME_AFIB_PR, oldValue, afib_pr);
	}

	/**
	 * @param avrgrg
	 *            the avrgrg to set
	 */
	public void setAvrgrg(boolean avrgrg) {
		boolean oldValue = this.avrgrg;
		this.avrgrg = avrgrg;
		firePropertyChange(PROPERTYNAME_AVRGRG, oldValue, avrgrg);
	}

	/**
	 * @param chb_pr
	 *            the chb_pr to set
	 */
	public void setChb_pr(boolean chb_pr) {
		boolean oldValue = this.chb_pr;
		this.chb_pr = chb_pr;
		firePropertyChange(PROPERTYNAME_CHB_PR, oldValue, chb_pr);
	}

	/**
	 * @param chol_pr
	 *            the chol_pr to set
	 */
	public void setChol_pr(double chol_pr) {
		double oldValue = this.chol_pr;
		this.chol_pr = chol_pr;
		firePropertyChange(PROPERTYNAME_CHOL_PR, oldValue, chol_pr);
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
	 * @param hx_popdz
	 *            the hx_popdz to set
	 */
	public void setHx_popdz(boolean hx_popdz) {
		boolean oldValue = this.hx_popdz;
		this.hx_popdz = hx_popdz;
		firePropertyChange(PROPERTYNAME_HX_POPDZ, oldValue, hx_popdz);
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

	/**
	 * @param lcx70
	 *            the lcx70 to set
	 */
	public void setLcx70(boolean lcx70) {
		boolean oldValue = this.lcx70;
		this.lcx70 = lcx70;
		firePropertyChange(PROPERTYNAME_LCX70, oldValue, lcx70);
	}

	/**
	 * @param lmtany
	 *            the lmtany to set
	 */
	public void setLmtany(boolean lmtany) {
		boolean oldValue = this.lmtany;
		this.lmtany = lmtany;
		firePropertyChange(PROPERTYNAME_LMTANY, oldValue, lmtany);
	}

	/**
	 * @param rca50
	 *            the rca50 to set
	 */
	public void setRca50(boolean rca50) {
		boolean oldValue = this.rca50;
		this.rca50 = rca50;
		firePropertyChange(PROPERTYNAME_RCA50, oldValue, rca50);
	}

	public boolean isAvrgrg() {
		return (avrgrg);
	}

	public boolean isLmtany() {
		return (lmtany);
	}

	public boolean isRca50() {
		return (rca50);
	}

	public boolean isLcx70() {
		return (lcx70);
	}

	// Non-cardiac comorbidity;
	public boolean getHx_cva() {
		return (hx_cva);
	}

	public boolean getHx_copd() {
		return (hx_copd);
	}

	public boolean getHx_popdz() {
		return (hx_popdz);
	}

	public double getChol_pr() {
		return (chol_pr);
	}

	public double getHct_pr() {
		return (hct_pr);
	}

	public boolean getHx_rnldz() {
		return (hx_rnldz);
	}

	/* Experience; */
	public Date getSurgeryDate() {
		return (surgeryDate);

	}

	public void setSurgeryDate(Date newDate) {
		Date oldDate = getSurgeryDate();
		surgeryDate = newDate;

		firePropertyChange(PROPERTYNAME_SURGERYDATE, oldDate, newDate);

		if (birthDate != null && surgeryDate != null) {
			setAge(datediff(surgeryDate, birthDate) / 365.2425);
		}
		if (surgeryDate != null && miDate != null) {
			setIv_pmi(datediff(miDate, surgeryDate) / 365.2425);
		}
	}

	public Date getMiDate() {
		return (miDate);
	}

	public void setMiDate(Date newDate) {
		Date oldDate = getMiDate();
		miDate = newDate;
		firePropertyChange(PROPERTYNAME_MIDATE, oldDate, newDate);
		if (surgeryDate != null && miDate != null)
			setIv_pmi(datediff(miDate, surgeryDate) / 365.2425);
	}

	public Date getBirthDate() {
		return (birthDate);
	}

	public void setBirthDate(Date newDate) {
		Date oldDate = getBirthDate();
		birthDate = newDate;
		firePropertyChange(PROPERTYNAME_BIRTHDATE, oldDate, newDate);
		if (birthDate != null && surgeryDate != null) {
			setAge(datediff(surgeryDate, birthDate) / 365.2425);
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

	public boolean isExecuteCABG() {
		return executeCABG;
	}

	public void setExecuteCABG(boolean executeCABG) {
		boolean oldExec = this.executeCABG;
		this.executeCABG = executeCABG;
		firePropertyChange(PROPERTYNAME_EXECUTE_CABG, oldExec, executeCABG);
	}

	public boolean isExecuteCBGV() {
		return executeCBGV;
	}

	public void setExecuteCBGV(boolean executeCBGV) {
		boolean oldExec = this.executeCBGV;
		this.executeCBGV = executeCBGV;
		firePropertyChange(PROPERTYNAME_EXECUTE_CBGV, oldExec, executeCBGV);
	}

	public boolean isExecuteSVR() {
		return executeSVR;
	}

	public void setExecuteSVR(boolean executeSVR) {
		boolean oldExec = this.executeSVR;
		this.executeSVR = executeSVR;
		firePropertyChange(PROPERTYNAME_EXECUTE_SVR, oldExec, executeSVR);
	}

	public boolean isExecuteTXPL() {
		return executeTXPL;
	}

	public void setExecuteTXPL(boolean executeTXPL) {
		boolean oldExec = this.executeTXPL;
		this.executeTXPL = executeTXPL;
		firePropertyChange(PROPERTYNAME_EXECUTE_TXPL, oldExec, executeTXPL);
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

	public String getColorCBGV() {
		log.debug("Getting ColorCBGV: " + colorCBGV);
		if (colorCBGV == null)
			setColorCBGV("blue");
		return colorCBGV;
	}

	public void setColorCBGV(String patientColor) {
		log.debug("colorCBGV: " + colorCBGV);
		String oldColor = this.colorCBGV;
		this.colorCBGV = patientColor;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_COLOR_CBGV, oldColor, patientColor);
	}

	public String getColorSVR() {
		log.debug("Getting ColorSVR: " + colorSVR);
		if (colorSVR == null)
			setColorSVR("orange");
		return colorSVR;
	}

	public void setColorSVR(String patientColor) {
		log.debug("colorSVR: " + colorSVR);
		String oldColor = this.colorSVR;
		this.colorSVR = patientColor;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_COLOR_SVR, oldColor, patientColor);
	}

	public String getColorTXPL() {
		log.debug("Getting ColorTXPL: " + colorTXPL);
		if (colorTXPL == null)
			setColorTXPL("red");
		return colorTXPL;
	}

	public void setColorTXPL(String patientColor) {
		log.debug("colorTXPL: " + colorTXPL);
		String oldColor = this.colorTXPL;
		this.colorTXPL = patientColor;
		setPatientTitle();
		firePropertyChange(PROPERTYNAME_COLOR_TXPL, oldColor, patientColor);
	}

	public Vector<Color> getColor() {
		Vector<Color> clr = new Vector<Color>();
		try {
			Field field = Class.forName("org.jfree.chart.ChartColor").getField(
					getColorCABG());

			clr.add((Color) field.get(null));
			field = Class.forName("org.jfree.chart.ChartColor").getField(
					getColorCBGV());
			clr.add((Color) field.get(null));
			field = Class.forName("org.jfree.chart.ChartColor").getField(
					getColorSVR());
			clr.add((Color) field.get(null));
			field = Class.forName("org.jfree.chart.ChartColor").getField(
					getColorTXPL());
			clr.add((Color) field.get(null));
		} catch (Exception e) {

		}
		log.debug(clr.toString());

		return clr;
	}

	public boolean isVarr_pr() {
		return varr_pr;
	}

	public void setVarr_pr(boolean varr_pr) {
		boolean oldValue = this.varr_pr;
		this.varr_pr = varr_pr;
		firePropertyChange(PROPERTYNAME_VARR_PR, oldValue, varr_pr);
	}

	public boolean isVd3() {
		return vd3;
	}

	public void setVd3(boolean vd3) {
		boolean oldValue = this.vd3;
		this.vd3 = vd3;
		firePropertyChange(PROPERTYNAME_VD3, oldValue, vd3);
	}

	public boolean isHx_pvd() {
		return hx_pvd;
	}

	public void setHx_pvd(boolean hx_pvd) {
		boolean oldValue = this.hx_pvd;
		this.hx_pvd = hx_pvd;
		firePropertyChange(PROPERTYNAME_HX_PVD, oldValue, hx_pvd);
	}

	public double getBlrbn_pr() {
		return blrbn_pr;
	}

	public void setBlrbn_pr(double blrbn_pr) {
		double oldValue = this.blrbn_pr;
		this.blrbn_pr = blrbn_pr;
		firePropertyChange(PROPERTYNAME_BLRBN_PR, oldValue, blrbn_pr);
	}

	@Override
	public String getPatientColor() {
		// TODO Auto-generated method stub

		// First we'll return the CABG color.
		return getColorCABG();
	}

}
