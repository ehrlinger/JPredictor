package transplantSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import patient.PatientModel;
import support.ExceptionHandler;

import com.jgoodies.validation.Validator;

public class TransplantSupport extends PatientModel {
	// Instance Creation ******************************************************

	private final static int num_anal_vars = 52;

	private static Logger log = Logger.getLogger(TransplantSupport.class);

	// Names of the Bound Bean Properties *************************************

	public static final String PROPERTYNAME_PATIENT_COLOR = "patientColor";

	protected String patientColor = null;
	/* Definitions of variables in model ; */

	/* Demographics; */
	/* r_female=0; Female sex vs male */
	public static String PROPERTYNAME_FEMALE = "female";

	private boolean female = false;

	/* npregpr=0; Number of pregnancies (women) */
	public static String PROPERTYNAME_NUMPREG = "numPreg";

	private int numPreg = 0;

	/* ht=173; Height (cm) (lo 110 - hi 210) */
	public static String PROPERTYNAME_HEIGHT = "height";

	private double height = 173;

	/* race_wh=1; Race: white vs other */
	/* race_bl=0; Race: black vs other */
	public static String PROPERTYNAME_RACE = "race";

	private String race = "White";

	private boolean race_wh = true;

	private boolean race_bl = false;

	/* Cardiomyopathy; */
	/* diag_dcm=1; Dilated (vs. other) */
	/* diag_rcm=0; Restrictive (vs. other) */
	/* diag_icm=0; Ischemic (vs. other) */
	public static String PROPERTYNAME_CARDIOMYOPATHY = "cardiomyopathy";

	private String cardiomyopathy = "Dialated";

	private boolean diag_rcm = false;

	private boolean diag_dcm = true;

	private boolean diag_icm = false;

	/* Cardic comorbidity; */
	/* lvefpr=16; LV ejection fraction (lo 5, high 57) */
	public static String PROPERTYNAME_LVEFPR = "lvefpr";

	private double lvefpr = 16;

	/* ms_lvef=0; Missing value indicator for LVEF (0) */

	/* chb_pr=0; Complete heart block */
	public static String PROPERTYNAME_CHB_PR = "chb_pr";

	private boolean chb_pr = false;

	/* hx_csurg=0; Previous cardiac surgery */
	public static String PROPERTYNAME_HX_CSURG = "hx_csurg";

	private boolean hx_csurg = false;

	/* ptcapr=0; Previous PCI (percutaneous coronary intervention) */
	public static String PROPERTYNAME_PTCAPR = "ptcapr";

	private boolean ptcapr = false;

	/* Hemodynamics */
	/* paspr=49; PA systolic pressure (mmHg) */
	public static String PROPERTYNAME_PASPR = "paspr";

	private double paspr = 49;

	/* ms_pasr=0; Missing value for pulmonary artery pressures */
	/* ms_papr=0; Missing value for right heart cath */
	/* padpr=25; PA systolic pressure (mmHg) */
	public static String PROPERTYNAME_PADPR = "padpr";

	private double padpr = 25;

	/* pcwpr=24; Pulmonary capillary wedge pressure (mmHg) */
	public static String PROPERTYNAME_PCWPR = "pcwpr";

	private double pcwpr = 24;

	/* Circulatory support at listing; */
	/* vadls=0; /* On VAD at listing */
	public static String PROPERTYNAME_VADLS = "vadls";

	private boolean vadls = false;

	/* ecmols=0; /* On ECMO at listing */
	public static String PROPERTYNAME_ECMOLS = "ecmols";

	private boolean ecmols = false;

	/* iabpls=0; /* On IABP at listing */
	public static String PROPERTYNAME_IABPLS = "iabpls";

	private boolean iabpls = false;

	/* aicdls=0; /* On ICD at listing */
	public static String PROPERTYNAME_AICDLS = "aicdls";

	private boolean aicdls = false;

	/* Immunogenetics; */

	public static String PROPERTYNAME_BLOODGROUP = "bloodgroup";

	private String bloodgroup = "A";

	/* rabo_a=1; ABO blood group: A vs AB */
	/* rabo_b=0; ABO blood group: B vs AB */
	/* rabo_o=0; ABO blood group: O vs AB */

	private boolean rabo_a = true;

	private boolean rabo_b = false;

	private boolean rabo_o = false;

	/* init_t=0; T-cell PRA (%) */
	public static String PROPERTYNAME_INIT_T = "init_t";

	private double init_t = 0;

	/* init_b=0; B-cell PRA (%) */
	public static String PROPERTYNAME_INIT_B = "init_b";

	private double init_b = 0;

	/* rcmvgpos=1; CMV positive */
	public static String PROPERTYNAME_RCMVGPOS = "rcmvgpos";

	private boolean rcmvgpos = true;

	/* rhbcopos=0; Hepatitis B core antibody */
	public static String PROPERTYNAME_RHBCOPOS = "rhbcopos";

	private boolean rhbcopos = false;

	/* hcab=0; Hepatitis C antibody */
	public static String PROPERTYNAME_HCAB = "hcab";

	private boolean hcab = false;

	/* tranpr=0; Prior blood transfusion */
	public static String PROPERTYNAME_TRANPR = "tranpr";

	private boolean tranpr = false;

	/* Comorbid conditions and history; */
	/* hx_cva=0; CVA */
	public static String PROPERTYNAME_HX_CVA = "hx_cva";

	private boolean hx_cva = false;

	/* ms_cva=0; Missing value for CVA */
	/* hx_chol=0; History of Cholesteremia */
	public static String PROPERTYNAME_HX_CHOL = "hx_chol";

	private boolean hx_chol = false;

	/* hx_diedm=0; Diabetes (diet treated) */
	/* hx_diedm=0; Diabetes (drug treated) */
	/* hx_niddm=0; Diabetes (insulin treated) */
	/* hx_iddm=0; Diabetes (pharmacologically treated) */
	public static String PROPERTYNAME_DIABETES = "diabetes";

	private String diabetes = "None";

	private boolean hx_diedm = false;

	private boolean hx_iddm = false;

	private boolean hx_niddm = false;

	// private int hx_dmtrt = 0;

	/* hx_copd=0; Chronic obstructive pulmonary disease */
	public static String PROPERTYNAME_HX_COPD = "hx_copd";

	private boolean hx_copd = false;

	/* hxcigl6=0; Cigarette use last 6 months before listing */
	public static String PROPERTYNAME_HXCIGL6 = "hxcigl6";

	private boolean hxcigl6 = false;

	/* hx_ulcr=0; History of peptic ulcer disease */
	public static String PROPERTYNAME_HX_ULCR = "hx_ulcr";

	private boolean hx_ulcr = false;

	/* hxmalig=0; History of malignancy */
	public static String PROPERTYNAME_HXMALIG = "hxmalig";

	private boolean hxmalig = false;

	/* Laboratory values; */
	/* bilils=1.5; Bilirubin (mg/dL) */
	public static String PROPERTYNAME_BILILS = "bilils";

	private double bilils;

	/* ms_bilil=0; Missing value for bilirubin */
	/* bunls=29; BUN (mg/dL) */
	public static String PROPERTYNAME_BUNLS = "bunls";

	private double bunls = 1.5;

	/* creals=1.4; Creatinine (mg/dL) */
	public static String PROPERTYNAME_CREALS = "creals";

	private double creals = 1.4;

	/* hgbls=12.5; Hemoglobin (mg/dL) */
	public static String PROPERTYNAME_HGBLS = "hgbls";

	private double hgbls = 12.5;

	/* Experience; */
	/* dt_list= mdy(1,1,2004); On ICD at listing */
	public static String PROPERTYNAME_LISTDATE = "listDate";

	private Date listDate;

	public static String PROPERTYNAME_BIRTHDATE = "birthDate";

	private Date birthDate;

	// *************************************************************************************
	// deprecated variables. From previous version.
	// public static String PROPERTYNAME_ALBLS = "albls";

	// private double albls;

	// public static String PROPERTYNAME_ANGPR_S = "angpr_s";

	// private boolean angpr_s;

	// public static String PROPERTYNAME_BPRAFLLS = "bpraflls";

	// private double bpraflls;

	// public static String PROPERTYNAME_HX_VASC = "hx_vasc";

	// private boolean hx_vasc;

	// public static String PROPERTYNAME_HXTIA = "hxtia";

	// private boolean hxtia;

	// public static String PROPERTYNAME_PRE_PVR = "pre_pvr";

	// private double pre_pvr;

	// *************************************************************************************

	/**
	 */
	private static final long serialVersionUID = 842427244021913408L;

	/**
	 * Constructs an empty <code>TransplantSupport</code>.
	 */
	public TransplantSupport() {
		super();
		setupPropertyChangeListener(new TransplantSupportPresentationModel(this));
		// The name of the hazard output file
		dataFileName.add("data/TransplantSupport.xpt.dta");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see predictor.IEntryForm#getExecuteString()
	 */
	public String getExecuteString(String fileName) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		String ans = "";
		/*
		 * The input variables...
		 * 
		 */
		// Demographics
		try {

			// * Missing value indicator variables;
			boolean ms_bilil = false;
			boolean ms_lvef = false;
			boolean ms_pasr = false;
			boolean ms_cva = false;
			// Transformations.
			/* Demographics; */
			double ln_ht = Math.log(height);
			/* Cardic comorbidity; */
			double lvefpr2 = Math.pow((lvefpr / 16.), 2);
			double in_lvef = 16.0 / lvefpr;
			/* Hemodynamics; */
			double paspr2 = Math.pow((paspr / 48.), 2);
			double padpr2 = Math.pow((padpr / 25.), 2);
			double pcwpr2 = Math.pow((pcwpr / 24.), 2);
			/* Immunogenetics; */
			double ln_tpra = Math.log(init_t + 1);
			boolean prat1_10 = false;
			if (init_t > 0 && init_t <= 10) {
				prat1_10 = true;
			}
			boolean prab0_ls = false;
			if (init_b == 0)
				prab0_ls = true;
			/* Laboratory values; */
			double in_bilil = 1.0 / bilils;
			double in2bilil = Math.pow(in_bilil, 2);
			double creals2 = Math.pow(creals, 2);
			double ln_bunls = Math.log(bunls);
			double ln_hgbls = Math.log(hgbls);
			/*
			 * Calculate creatinine clearance at listing using abbreviated MDRD
			 * method;
			 */
			double creal_ln = Math.log(creals);
			double agels = datediff(listDate, birthDate) / 365.2425;
			double agels_ln = Math.log(agels);

			double gfr_pr = Math.exp(5.228 - 1.154 * creal_ln - 0.203 * agels_ln
					- 0.299 * (female ? 1 : 0) + 0.192 * (race_bl ? 1 : 0));

			double in2gfrpr = Math.pow((68.0 / gfr_pr), 2);
			/* Date of listing; */
			double iv_lsyrs = datediff(formatter.parse("1/1/1984"), listDate) / 365.2425;
			double ln_lsyrs = Math.log(iv_lsyrs);
			double year_ls = 1984 + iv_lsyrs;

			// Time is in years...
			// We should test for the application now. This way we can post a message
			// if we're on an
			// uncompiled platform.
			log.debug(application);

			boolean pre1997 = false;
			if (year_ls < 1997) {
				pre1997 = true;
			}
			log.debug("filename: " + fileName);

			// Build the string.
			ans = application + " " + fileName + " " + n_time_steps + " "
					+ modelSet.getGraphTime() + " " + num_anal_vars + " " + height + " "
					+ (race_wh ? "1" : "0") + " " + numPreg + " "
					+ (diag_dcm ? "1" : "0") + " " + (diag_rcm ? "1" : "0") + " "
					+ (diag_icm ? "1" : "0") + " " + (rabo_a ? "1" : "0") + " "
					+ (rabo_b ? "1" : "0") + " " + (rabo_o ? "1" : "0") + " "
					+ (chb_pr ? "1" : "0") + " " + (hxcigl6 ? "1" : "0") + " "
					+ (hx_cva ? "1" : "0") + " " + (hx_copd ? "1" : "0") + " "
					+ (hx_iddm ? "1" : "0") + " " + (hx_niddm ? "1" : "0") + " "
					+ (hx_diedm ? "1" : "0") + " " + (hx_ulcr ? "1" : "0") + " "
					+ (vadls ? "1" : "0") + " " + (ecmols ? "1" : "0") + " "
					+ (aicdls ? "1" : "0") + " " + (iabpls ? "1" : "0") + " " + in2bilil
					+ " " + creals + " " + in2gfrpr + " " + paspr2 + " " + pcwpr2 + " "
					+ lvefpr2 + " " + (rcmvgpos ? "1" : "0") + " " + ln_tpra + " "
					+ (prab0_ls ? "1" : "0") + " " + ln_lsyrs + " "
					+ (pre1997 ? "1" : "0") + " " + (ms_cva ? "1" : "0") + " "
					+ (ms_bilil ? "1" : "0") + " " + (ms_lvef ? "1" : "0") + " "
					+ (ms_pasr ? "1" : "0") + " " + (female ? "1" : "0") + " " + ln_ht
					+ " " + (race_bl ? "1" : "0") + " " + (hx_csurg ? "1" : "0") + " "
					+ (hx_chol ? "1" : "0") + " " + (tranpr ? "1" : "0") + " "
					+ (hxmalig ? "1" : "0") + " " + ln_bunls + " " + creals2 + " "
					+ ln_hgbls + " " + (ptcapr ? "1" : "0") + " " + padpr2 + " "
					+ in_lvef + " " + (rhbcopos ? "1" : "0") + " " + (hcab ? "1" : "0")
					+ " " + (prat1_10 ? "1" : "0");

			log.debug(ans);

			// log.debug("--------- inputs ----------");
			//
			// log.debug("height:" + height);
			// log.debug("aicdls:" + aicdls);
			// log.debug("hx_csurg:" + hx_csurg);
			// log.debug("rabo_a:" + rabo_a);
			// log.debug("bunls:" + bunls);
			// log.debug("bilils:" + bilils);
			// log.debug("creals:" + creals);
			// log.debug("hgbls:" + hgbls);
			// log.debug("paspr:" + paspr);
			// log.debug("padpr:" + padpr);
			// log.debug("pcwpr:" + pcwpr);
			// log.debug("lvefpr:" + lvefpr);
			// log.debug("rabo_o:" + rabo_o);
			// log.debug("birthdate:" + birthDate);
			// log.debug("listDate:" + listDate);
			// log.debug("female:" + female);
			// log.debug("npreg:" + numPreg);
			// log.debug("race_wh:" + race_wh);
			// log.debug("race_bl:" + race_bl);
			// log.debug("diag_dcm:" + diag_dcm);
			// log.debug("diag_rcm:" + diag_rcm);
			// log.debug("diag_icm:" + diag_icm);
			// log.debug("chb_pr:" + chb_pr);
			// log.debug("ptcapr:" + ptcapr);
			// log.debug("vadls:" + vadls);
			// log.debug("ecmols:" + ecmols);
			// log.debug("iabpls:" + iabpls);
			// log.debug("rabo_b:" + rabo_b);
			// log.debug("init_t:" + init_t);
			// log.debug("init_b:" + init_b);
			// log.debug("rcmvgpos:" + rcmvgpos);
			// log.debug("rhbcopos:" + rhbcopos);
			// log.debug("hcab:" + hcab);
			// log.debug("tranpr:" + tranpr);
			// log.debug("hx_cva:" + hx_cva);
			// log.debug("hx_chol:" + hx_chol);
			// log.debug("hx_diedm:" + hx_diedm);
			// log.debug("hx_niddm:" + hx_niddm);
			// log.debug("hx_iddm:" + hx_iddm);
			// log.debug("hx_copd:" + hx_copd);
			// log.debug("hxcigl6:" + hxcigl6);
			// log.debug("hx_ulcr:" + hx_ulcr);
			// log.debug("hxmalig:" + hxmalig);
			//
			// log.debug("listDate:" + listDate);
			// log.debug("--------- transformations ----------");
			//
			// log.debug("ln_ht:" + ln_ht);
			// log.debug("lvefpr2:" + lvefpr2);
			// log.debug("in_lvef:" + in_lvef);
			// log.debug("paspr2:" + paspr2);
			// log.debug("padpr2:" + padpr2);
			// log.debug("pcwpr2:" + pcwpr2);
			// log.debug("ln_tpra:" + ln_tpra);
			// log.debug("prat1_10:" + prat1_10);
			// log.debug("prab0_ls:" + prab0_ls);
			// log.debug("in_bilil:" + in_bilil);
			// log.debug("in2bilil:" + in2bilil);
			// log.debug("creals2:" + creals2);
			// log.debug("ln_bunls:" + ln_bunls);
			// log.debug("ln_hgbls:" + ln_hgbls);
			// log.debug("creal_ln:" + creal_ln);
			// log.debug("agels:" + agels);
			// log.debug("agels_ln:" + agels_ln);
			// log.debug("gfr_pr:" + gfr_pr);
			//			log.debug("in2gfrpr:" + in2gfrpr);
			//			log.debug("iv_lsyrs:" + iv_lsyrs);
			//			log.debug("ln_lsyrs:" + ln_lsyrs);
			//			log.debug("year_ls:" + year_ls);
			//			log.debug("pre1997:" + pre1997);

		} catch (ParseException ex) {
			ExceptionHandler.logger(ex, log);
		}
		return (ans);
	}

	public Validator<TransplantSupport> getValidator() {
		return (new TransplantSupportValidator(this));
	}

	public boolean getAicdls() {
		return aicdls;
	}

	public void setAicdls(boolean aicdls) {
		boolean oldAicdls = getAicdls();
		this.aicdls = aicdls;
		firePropertyChange(PROPERTYNAME_AICDLS, oldAicdls, aicdls);
	}

	public double getBilils() {
		return bilils;
	}

	public void setBilils(double bilils) {
		log.debug("setBilils: " + bilils);
		double oldBilils = this.bilils;
		this.bilils = bilils;
		firePropertyChange(PROPERTYNAME_BILILS, oldBilils, bilils);
	}

	public Date getBirthDate() {
		return (birthDate);

	}

	public void setBirthDate(Date newDate) {
		Date oldDate = getBirthDate();
		birthDate = newDate;
		firePropertyChange(PROPERTYNAME_BIRTHDATE, oldDate, newDate);
	}

	public String getBloodgroup() {
		return bloodgroup;
	}

	public void setBloodgroup(String bloodgroup) {
		if (this.bloodgroup == null)
			this.bloodgroup = "AB";
		if (bloodgroup == null)
			bloodgroup = "AB";
		String oldBloodgroup = this.bloodgroup;
		this.bloodgroup = bloodgroup;
		rabo_a = false;
		rabo_b = false;
		rabo_o = false;
		if (bloodgroup.equals("A"))
			rabo_a = true;
		if (bloodgroup.equals("B"))
			rabo_b = true;
		if (bloodgroup.equals("O"))
			rabo_o = true;
		firePropertyChange(PROPERTYNAME_BLOODGROUP, oldBloodgroup, bloodgroup);
	}

	/**
	 * @deprecated
	 */

	public void setBpraflls(double bpraflls) {
		// double oldBpraflls = this.bpraflls;
		// this.bpraflls = bpraflls;
		// firePropertyChange(PROPERTYNAME_BPRAFLLS, oldBpraflls, bpraflls);
	}

	public double getBunls() {
		return bunls;
	}

	public void setBunls(double bunls) {
		double oldBunls = this.bunls;
		this.bunls = bunls;
		firePropertyChange(PROPERTYNAME_BUNLS, oldBunls, bunls);
	}

	public boolean getChb_pr() {
		return chb_pr;
	}

	public void setChb_pr(boolean chb_pr) {
		boolean oldChb_pr = this.chb_pr;
		this.chb_pr = chb_pr;
		firePropertyChange(PROPERTYNAME_CHB_PR, oldChb_pr, chb_pr);
	}

	public double getCreals() {
		return creals;
	}

	public void setCreals(double creals) {
		double oldCreals = this.creals;
		this.creals = creals;
		firePropertyChange(PROPERTYNAME_CREALS, oldCreals, creals);
	}

	public String getDiabetes() {
		return diabetes;
	}

	public void setDiabetes(String diabetes) {
		String oldDiabetes = this.diabetes;
		this.diabetes = diabetes;
		hx_diedm = false;
		hx_niddm = false; /* Diabetes (insulin treated) */
		hx_iddm = false;

		if (diabetes.compareTo("Diet") == 0)
			hx_diedm = true;
		if (diabetes.compareTo("Oral") == 0)
			hx_iddm = true;
		if (diabetes.compareTo("Insulin") == 0)
			hx_niddm = true;

		firePropertyChange(PROPERTYNAME_DIABETES, oldDiabetes, diabetes);
	}

	public String getCardiomyopathy() {
		return cardiomyopathy;
	}

	public void setCardiomyopathy(String cardiomyopathy) {
		String oldCardio = this.cardiomyopathy;
		this.cardiomyopathy = cardiomyopathy;
		diag_dcm = false;
		diag_rcm = false;
		diag_icm = false;
		if (cardiomyopathy.equals("Dialated")) {
			diag_dcm = true;
		}
		if (cardiomyopathy.equals("Restrictive")) {
			diag_rcm = true;
		}
		if (cardiomyopathy.equals("Ischemic")) {
			diag_icm = true;
		}

		firePropertyChange(PROPERTYNAME_CARDIOMYOPATHY, oldCardio, cardiomyopathy);
	}

	public boolean getEcmols() {
		return ecmols;
	}

	public void setEcmols(boolean ecmols) {
		boolean oldEcmols = this.ecmols;
		this.ecmols = ecmols;
		firePropertyChange(PROPERTYNAME_ECMOLS, oldEcmols, ecmols);
	}

	public boolean getFemale() {
		return female;
	}

	public void setFemale(boolean female) {
		boolean oldFemale = this.female;
		this.female = female;
		firePropertyChange(PROPERTYNAME_FEMALE, oldFemale, female);
	}

	public boolean getHcab() {
		return hcab;
	}

	public void setHcab(boolean hcab) {
		boolean oldHcab = this.hcab;
		this.hcab = hcab;
		firePropertyChange(PROPERTYNAME_HCAB, oldHcab, hcab);
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		double oldHeight = this.height;
		this.height = height;
		firePropertyChange(PROPERTYNAME_HEIGHT, oldHeight, height);
	}

	public double getHgbls() {
		return hgbls;
	}

	public void setHgbls(double hgbls) {
		double oldHgbls = this.hgbls;
		this.hgbls = hgbls;
		firePropertyChange(PROPERTYNAME_HGBLS, oldHgbls, hgbls);
	}

	public boolean getHx_chol() {
		return hx_chol;
	}

	public void setHx_chol(boolean hx_chol) {
		boolean oldHx_chol = this.hx_chol;
		this.hx_chol = hx_chol;
		firePropertyChange(PROPERTYNAME_HX_CHOL, oldHx_chol, hx_chol);
	}

	public boolean getHx_copd() {
		return hx_copd;
	}

	public void setHx_copd(boolean hx_copd) {
		boolean oldHx_copd = this.hx_copd;
		this.hx_copd = hx_copd;
		firePropertyChange(PROPERTYNAME_HX_COPD, oldHx_copd, hx_copd);
	}

	public boolean getHx_csurg() {
		return hx_csurg;
	}

	public void setHx_csurg(boolean hx_csurg) {
		boolean oldHx_csurg = this.hx_csurg;
		this.hx_csurg = hx_csurg;
		firePropertyChange(PROPERTYNAME_HX_CSURG, oldHx_csurg, hx_csurg);
	}

	public boolean getHx_cva() {
		return hx_cva;
	}

	public void setHx_cva(boolean hx_cva) {
		boolean oldHx_cva = this.hx_cva;
		this.hx_cva = hx_cva;
		firePropertyChange(PROPERTYNAME_HX_CVA, oldHx_cva, hx_cva);
	}

	public boolean getHx_ulcr() {
		return hx_ulcr;
	}

	public void setHx_ulcr(boolean hx_ulcr) {
		boolean oldHx_ulcr = this.hx_ulcr;
		this.hx_ulcr = hx_ulcr;
		firePropertyChange(PROPERTYNAME_HX_ULCR, oldHx_ulcr, hx_ulcr);
	}

	/**
	 * @deprecated
	 */
	public void setHx_vasc(boolean hx_vasc) {
		// boolean oldHx_vasc = this.hx_vasc;
		// this.hx_vasc = hx_vasc;
		// firePropertyChange(PROPERTYNAME_HX_VASC, oldHx_vasc, hx_vasc);
	}

	public boolean getHxmalig() {
		return hxmalig;
	}

	public void setHxmalig(boolean hxmalig) {
		boolean oldHxmalig = this.hxmalig;
		this.hxmalig = hxmalig;
		firePropertyChange(PROPERTYNAME_HXMALIG, oldHxmalig, hxmalig);
	}

	/**
	 * @deprecated
	 */

	public void setHxtia(boolean hxtia) {
		// boolean oldHxtia = this.hxtia;
		// this.hxtia = hxtia;
		// firePropertyChange(PROPERTYNAME_HXTIA, oldHxtia, hxtia);
	}

	public boolean getIabpls() {
		return iabpls;
	}

	public void setIabpls(boolean iabpls) {
		boolean oldIabpls = this.iabpls;
		this.iabpls = iabpls;
		firePropertyChange(PROPERTYNAME_IABPLS, oldIabpls, iabpls);
	}

	public double getInit_b() {
		return init_b;
	}

	public void setInit_b(double init_b) {
		double oldInit_b = this.init_b;
		this.init_b = init_b;
		firePropertyChange(PROPERTYNAME_INIT_B, oldInit_b, init_b);
	}

	public double getInit_t() {
		return init_t;
	}

	public void setInit_t(double init_t) {
		double oldInit_t = this.init_t;
		this.init_t = init_t;
		firePropertyChange(PROPERTYNAME_INIT_T, oldInit_t, init_t);
	}

	public Date getListDate() {
		return listDate;
	}

	public void setListDate(Date newDate) {
		Date oldDate = getListDate();
		listDate = newDate;
		firePropertyChange(PROPERTYNAME_LISTDATE, oldDate, newDate);
	}

	public double getLvefpr() {
		return lvefpr;
	}

	public void setLvefpr(double lvefpr) {
		double oldLvefpr = this.lvefpr;
		this.lvefpr = lvefpr;
		firePropertyChange(PROPERTYNAME_LVEFPR, oldLvefpr, lvefpr);
	}

	public int getNumPreg() {
		return numPreg;
	}

	public void setNumPreg(int numPreg) {
		int oldNumPreg = this.numPreg;
		this.numPreg = numPreg;
		firePropertyChange(PROPERTYNAME_NUMPREG, oldNumPreg, numPreg);
	}

	public double getPaspr() {
		return paspr;
	}

	public void setPaspr(double paspr) {
		double oldPaspr = this.paspr;
		this.paspr = paspr;
		firePropertyChange(PROPERTYNAME_PASPR, oldPaspr, paspr);
	}

	public double getPcwpr() {
		return pcwpr;
	}

	public void setPcwpr(double pcwpr) {
		double oldPcwpr = this.pcwpr;
		this.pcwpr = pcwpr;
		firePropertyChange(PROPERTYNAME_PCWPR, oldPcwpr, pcwpr);
	}

	/**
	 * @deprecated
	 */

	public void setPre_pvr(double pre_pvr) {
		// double oldPre_pvr = this.pre_pvr;
		// this.pre_pvr = pre_pvr;
		// firePropertyChange(PROPERTYNAME_PRE_PVR, oldPre_pvr, pre_pvr);
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		String oldRace = this.race;
		this.race = race;
		race_bl = false;
		race_wh = false;

		if (race.equals("Black")) {
			race_bl = true;
		}
		if (race.equals("White")) {
			race_wh = true;
		}
		firePropertyChange(PROPERTYNAME_RACE, oldRace, race);
	}

	public boolean getRcmvgpos() {
		return rcmvgpos;
	}

	public void setRcmvgpos(boolean rcmvgpos) {
		boolean oldRcmvgpos = this.rcmvgpos;
		this.rcmvgpos = rcmvgpos;
		firePropertyChange(PROPERTYNAME_RCMVGPOS, oldRcmvgpos, rcmvgpos);
	}

	public boolean getRhbcopos() {
		return rhbcopos;
	}

	public void setRhbcopos(boolean rhbcopos) {
		boolean oldRhbcopos = this.rhbcopos;
		this.rhbcopos = rhbcopos;
		firePropertyChange(PROPERTYNAME_RHBCOPOS, oldRhbcopos, rhbcopos);
	}

	/**
	 * @deprecated
	 * @param smoke
	 */
	public void setSmoke(String smoke) {
	}

	public String getTitleString() {
		String titleString = "Transplant Support Survival Curve";
		String prepend = patientName;

		if (patientID != null && patientID.length() > 0) {
			prepend += " : " + patientID;
		}

		if (prepend != null && prepend.length() > 0) {
			titleString = "[" + prepend + "] " + titleString;
		}

		return (titleString);
	}

	public boolean getVadls() {
		return vadls;
	}

	public void setVadls(boolean vadls) {
		boolean oldVadls = this.vadls;
		this.vadls = vadls;
		firePropertyChange(PROPERTYNAME_VADLS, oldVadls, vadls);
	}

	public boolean isPtcapr() {
		return ptcapr;
	}

	public void setPtcapr(boolean ptcapr) {
		boolean oldPtcapr = this.ptcapr;
		this.ptcapr = ptcapr;
		firePropertyChange(PROPERTYNAME_PTCAPR, oldPtcapr, ptcapr);
	}

	public double getPadpr() {
		return padpr;
	}

	public void setPadpr(double padpr) {
		double oldPadpr = this.padpr;
		this.padpr = padpr;
		firePropertyChange(PROPERTYNAME_PADPR, oldPadpr, padpr);
	}

	public boolean isTranpr() {
		return tranpr;
	}

	public void setTranpr(boolean tranpr) {
		boolean oldTranpr = this.tranpr;
		this.tranpr = tranpr;
		firePropertyChange(PROPERTYNAME_TRANPR, oldTranpr, tranpr);
	}

	public boolean isHxcigl6() {
		return hxcigl6;
	}

	public void setHxcigl6(boolean hxcigl6) {
		boolean oldHxcigl6 = this.hxcigl6;
		this.hxcigl6 = hxcigl6;
		firePropertyChange(PROPERTYNAME_HXCIGL6, oldHxcigl6, hxcigl6);
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
