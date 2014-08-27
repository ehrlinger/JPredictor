package transplantSupport;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import patient.ModelExecuteException;
import patient.PatientModel;
import patient.PatientModelForm;
import support.ExceptionHandler;
import util.ComponentFactory;
import util.IconFeedbackPanel;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class TransplantSupportForm extends PatientModelForm {
	private static Logger log = Logger.getLogger(TransplantSupportForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319950740880016103L;

	private JCheckBox aicdlsBox;

	private JFormattedTextField bililsField;

	private JFormattedTextField birthDateField;

	private JComboBox bloodGroupList;

	private JFormattedTextField bunlsField;

	private JCheckBox chb_prBox;

	private JFormattedTextField crealsField;

	private JCheckBox ptcaprBox;

	private JCheckBox ecmolsBox;

	private JCheckBox hcabBox;

	private JFormattedTextField heightField;

	private JFormattedTextField hgblsField;

	private JCheckBox hx_cholBox;

	private JCheckBox hx_copdBox;

	private JCheckBox hx_csurgBox;

	private JCheckBox hx_cvaBox;

	private JComboBox hx_diabetesList;

	private JCheckBox hx_smokeBox;

	private JCheckBox hx_ulcrBox;

	private JCheckBox hxmaligBox;

	private JCheckBox iabplsBox;

	private JFormattedTextField init_bField;

	private JFormattedTextField init_tField;

	private JFormattedTextField listDateField;

	private JFormattedTextField lvefprField;

	private JFormattedTextField npregprField;

	private JFormattedTextField pasprField;

	private JFormattedTextField padprField;

	private JFormattedTextField pcwprField;

	private JComboBox raceList;

	private JCheckBox rcmvgposBox;

	private JCheckBox rhbcoposBox;

	private JPanel transplantSupportPanel;

	private TransplantSupportPresentationModel transSupportPresModel;

	private JCheckBox vadlsBox;

	private JComboBox cardiomyopathyList;

	private JCheckBox tranprBox;
	
	private JComboBox colorList;

	/*
	 * The input variables...
	 * 
	 */

	/**
	 * EntryForm constructor
	 * 
	 * Initialize the class and creates actions for use in menus.
	 */
	public TransplantSupportForm() {
		super();
	}

	// Component Creation and Initialization **********************************

	// Building *************************************************************

	/*
	 * (non-Javadoc)
	 * 
	 * @see predictor.IEntryForm#buildPanel()
	 */
		public JComponent buildPanel(PatientModel tsData) {
		super.buildPanel(tsData);
		log.debug("Building TransplantSupport panel");
		if (tsData == null)
			return (null);
		log.debug("patientModel");
		transSupportPresModel = new TransplantSupportPresentationModel(
				(TransplantSupport) patientModel);
		// Create a FormLayout instance on the given column and row specs.
		// For almost all forms you specify the columns; sometimes rows are
		// created dynamically. In this case the labels are right aligned.
		layout = new FormLayout("max(40dlu;pref), 1dlu, max(40dlu;pref)" // 1st
				// major
				// colum
				+ ", 2dlu, max(40dlu;pref), 1dlu, max(40dlu;pref)", // 2nd major
																	// column
				""); // add rows dynamically

		// Specify that columns 1 & 5 as well as 3 & 7 have equal widths.
		// layout.setColumnGroups(new int[][]{{1, 5}, {3, 7}});

		// Create a builder that assists in adding components to the container.
		// Wrap the panel with a standardized border.
		builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		// Fill the grid with components; the builder offers to create
		// frequently used components, e.g. separators and labels.

		// -----------------------------------------------------------
		// Start with patient Demographics. Naming/labeling is not required,
		// though
		// suggested for legends.
		//
		// Build the demographics with 2 columns.
		buildPatientDemographicsSection(transSupportPresModel, 2);

		ValueModel birthDateModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_BIRTHDATE);
		birthDateField = ComponentFactory.createDateField(birthDateModel);
		birthDateField.setToolTipText("Birth Date: [mm/dd/yyyy]");
		ValidationComponentUtils.setMessageKey(birthDateField,
				"TransplantSupport.Birth Date");
		ValidationComponentUtils.setMandatory(birthDateField, true);
		builder.append("Birth Date:", birthDateField);

		// builder.nextRow();
		ValueModel listDateModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_LISTDATE);
		listDateField = ComponentFactory.createDateField(listDateModel);
		listDateField.setToolTipText("Listing Date [mm/dd/yyyy]");
		ValidationComponentUtils.setMandatory(listDateField, true);
		ValidationComponentUtils.setMessageKey(listDateField,
				"TransplantSupport.Listing Date");

		builder.append("Listing Date:", listDateField);

		// builder.nextRow();
		ValueModel heightModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HEIGHT);
		heightField = ComponentFactory.createFormattedTextField(heightModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils.setMessageKey(heightField,
				"TransplantSupport.Height");
		heightField.setToolTipText("Height in centimeters between 100-225");
		builder.append("Height: [cm]", heightField);

		// Race is a boolean, true/false.

		ValueModel raceModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_RACE);
		List<String> possibleValues = new ArrayList<String>();
		possibleValues.add("Black");
		possibleValues.add("White");
		possibleValues.add("Other");
		raceList = new JComboBox(new ComboBoxAdapter<String>(possibleValues, raceModel));
		builder.append("Race:", raceList);

		// female is a boolean, true/false.
		ValueModel femaleModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_FEMALE);
		JRadioButton femaleBox = ComponentFactory.createRadioButton(
				femaleModel, true, "Female");
		JRadioButton maleBox = ComponentFactory.createRadioButton(femaleModel,
				false, "Male");
		builder.append(maleBox);
		builder.append(femaleBox);
		// builder.nextRow();

		ValueModel npregprModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_NUMPREG);
		npregprField = ComponentFactory.createIntegerField(npregprModel);
		npregprField.setToolTipText("0-15 pregnancies");
		ValidationComponentUtils.setMessageKey(npregprField,
				"TransplantSupport.Number of Pregnancies");
		builder.append("Number of Pregnancies:", npregprField);
		// builder.nextRow();

		// ---------------------------------------------------------------------------
		builder.appendSeparator("Cardiac Conditions");
		ValueModel cardiomyopathyModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_CARDIOMYOPATHY);
		possibleValues = new ArrayList<String>();
		possibleValues.add("Other");
		possibleValues.add("Dialated");
		possibleValues.add("Restrictive");
		possibleValues.add("Ischemic");
		cardiomyopathyList = new JComboBox(new ComboBoxAdapter<String>(possibleValues,
				cardiomyopathyModel));
		builder.append("Cardiomyopathy:", cardiomyopathyList);

		ValueModel lvefprModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_LVEFPR);
		lvefprField = ComponentFactory.createFormattedTextField(lvefprModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils.setMessageKey(lvefprField,
				"TransplantSupport.LV ejection fraction");
		lvefprField.setToolTipText("Left ventricular ejection fraction (%)");
		builder.append("LV ejection fraction:", lvefprField);

		ValueModel chb_prModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_CHB_PR);
		chb_prBox = ComponentFactory.createCheckBox(chb_prModel,
				"Complete Heart Block");
		chb_prBox.setToolTipText("Complete heart block, requiring pacemaker");
		builder.append(chb_prBox);

		ValueModel ptcaprModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_PTCAPR);
		ptcaprBox = ComponentFactory.createCheckBox(ptcaprModel, "HX PCI");
		ptcaprBox
				.setToolTipText("Previous PCI (Percutaneous coronary intervention");
		builder.append(ptcaprBox);

		ValueModel hx_csurgModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HX_CSURG);
		hx_csurgBox = ComponentFactory.createCheckBox(hx_csurgModel,
				"HX card surgery");
		hx_csurgBox
				.setToolTipText("Previous cardiac surgery such as CABG, valve, etc");
		builder.append(hx_csurgBox);

		builder.nextRow();

		// ---------------------------------------------------------------------------
		builder.appendSeparator("Hemodynamics (from pre-listing)");

		ValueModel pasprModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_PASPR);
		pasprField = ComponentFactory.createFormattedTextField(pasprModel,
				NumberFormat.getNumberInstance());
		pasprField.setToolTipText("Pulmonary artery systolic pressure (mmHg)");
		ValidationComponentUtils.setMessageKey(pasprField,
				"TransplantSupport.PA systolic pressure");
		builder.append("PA systolic pressure:", pasprField);

		ValueModel padprModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_PADPR);
		padprField = ComponentFactory.createFormattedTextField(padprModel,
				NumberFormat.getNumberInstance());
		padprField.setToolTipText("Pulmonary artery diastolic pressure (mmHg)");
		ValidationComponentUtils.setMessageKey(padprField,
				"TransplantSupport.PA diastolic pressure");
		builder.append("PA diastolic pressure:", padprField);

		ValueModel pcwprModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_PCWPR);
		pcwprField = ComponentFactory.createFormattedTextField(pcwprModel,
				NumberFormat.getNumberInstance());
		pcwprField.setToolTipText("Pulmonary capillary wedge pressure (mmHg)");
		ValidationComponentUtils.setMessageKey(pcwprField,
				"TransplantSupport.Pulmonary capillary wedge pressure");
		builder.append("Wedge pressure:", pcwprField);
		builder.nextRow();

		// ---------------------------------------------------------------------------
		builder.appendSeparator("Circulatory Support (at listing)");
		ValueModel aicdlsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_AICDLS);
		aicdlsBox = ComponentFactory.createCheckBox(aicdlsModel, "ICD");
		aicdlsBox.setToolTipText("Implantable cardioverter-defibrillator");
		builder.append(aicdlsBox);

		ValueModel ecmolsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_ECMOLS);
		ecmolsBox = ComponentFactory.createCheckBox(ecmolsModel, "ECMO");
		ecmolsBox.setToolTipText("Extracorporeal membrane oxygenation");
		builder.append(ecmolsBox);

		ValueModel iabplsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_IABPLS);
		iabplsBox = ComponentFactory.createCheckBox(iabplsModel, "IABP");
		iabplsBox.setToolTipText("Intra-aortic balloon pump");
		builder.append(iabplsBox);

		ValueModel vadlsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_VADLS);
		vadlsBox = ComponentFactory.createCheckBox(vadlsModel, "VAD");
		vadlsBox.setToolTipText("Ventricular assist device");
		builder.append(vadlsBox);

		// builder.nextRow();

		// ---------------------------------------------------------------------------
		builder.appendSeparator("Immunogenetics");

		ValueModel bloodGroupModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_BLOODGROUP);
		possibleValues = new ArrayList<String>();
		possibleValues.add("A");
		possibleValues.add("B");
		possibleValues.add("AB");
		possibleValues.add("O");
		bloodGroupList = new JComboBox(new ComboBoxAdapter<String>(possibleValues,
				bloodGroupModel));
		builder.append("Blood Group:", bloodGroupList);
		builder.nextRow();
		ValueModel init_tModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_INIT_T);
		init_tField = ComponentFactory.createFormattedTextField(init_tModel,
				NumberFormat.getNumberInstance());
		init_tField.setToolTipText("Cytotoxic T-cell PRA (%)");
		ValidationComponentUtils.setMessageKey(init_tField,
				"TransplantSupport.Cytotoxic T-cell PRA");
		builder.append("T-cell PRA [%]:", init_tField);

		ValueModel init_bModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_INIT_B);
		init_bField = ComponentFactory.createFormattedTextField(init_bModel,
				NumberFormat.getNumberInstance());
		init_bField.setToolTipText("Cytotoxic B-cell PRA [%]");
		ValidationComponentUtils.setMessageKey(init_bField,
				"TransplantSupport.Cytotoxic B-cell PRA");
		builder.append("B-cell PRA [%]:", init_bField);

		ValueModel rcmvgposModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_RCMVGPOS);
		rcmvgposBox = ComponentFactory.createCheckBox(rcmvgposModel, "CMV");
		rcmvgposBox.setToolTipText("CMV positive");
		builder.append(rcmvgposBox);
		// builder.nextRow();
		ValueModel rhbcoposModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_RHBCOPOS);
		rhbcoposBox = ComponentFactory.createCheckBox(rhbcoposModel,
				"Hepatitis B");
		rhbcoposBox.setToolTipText("Hepatitis B antibody");
		builder.append(rhbcoposBox);

		ValueModel hcabModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HCAB);
		hcabBox = ComponentFactory.createCheckBox(hcabModel, "Hepatitis C");
		hcabBox.setToolTipText("Hepatitis C antibody");
		builder.append(hcabBox);

		ValueModel tranprModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_TRANPR);
		tranprBox = ComponentFactory.createCheckBox(tranprModel,
				"HX Transfusion");
		tranprBox.setToolTipText("Prior Blood Transfusion");
		builder.append(tranprBox);
		// builder.nextRow();

		// ---------------------------------------------------------------------------
		builder.appendSeparator("Comorbid Conditions and History");

		ValueModel hx_diabetesModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_DIABETES);
		possibleValues = new ArrayList<String>();
		possibleValues.add("None");
		possibleValues.add("Diet");
		possibleValues.add("Oral");
		possibleValues.add("Insulin");
		hx_diabetesList = new JComboBox(new ComboBoxAdapter<String>(possibleValues,
				hx_diabetesModel));
		builder.append("HX of diabetes:", hx_diabetesList);
		// builder.nextRow();

		ValueModel hx_smokeModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HXCIGL6);
		hx_smokeBox = ComponentFactory.createCheckBox(hx_smokeModel,
				"HX smoking");
		hx_smokeBox
				.setToolTipText("Cigarette use last 6 months before listing");
		builder.append(hx_smokeBox);
		// builder.nextRow();

		ValueModel hx_cvaModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HX_CVA);
		hx_cvaBox = ComponentFactory.createCheckBox(hx_cvaModel, "HX stroke");
		hx_cvaBox.setToolTipText("Previous Stroke");
		builder.append(hx_cvaBox);

		ValueModel hx_cholModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HX_CHOL);
		hx_cholBox = ComponentFactory.createCheckBox(hx_cholModel,
				"HX of Cholestermia");
		// hx_cholBox.setToolTipText("");
		builder.append(hx_cholBox);

		ValueModel hxmaligModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HXMALIG);
		hxmaligBox = ComponentFactory.createCheckBox(hxmaligModel,
				"HX of cancer");
		// hxmaligBox.setToolTipText("");
		builder.append(hxmaligBox);
		// builder.nextRow();

		ValueModel hx_ulcrModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HX_ULCR);
		hx_ulcrBox = ComponentFactory.createCheckBox(hx_ulcrModel,
				"HX of peptic ulcer");
		hx_ulcrBox.setToolTipText("History of peptic ulcer disease");
		builder.append(hx_ulcrBox);

		ValueModel hx_copdModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HX_COPD);
		hx_copdBox = ComponentFactory
				.createCheckBox(hx_copdModel, "HX of COPD");
		hx_copdBox
				.setToolTipText("History of chronic obstructive pulmonary disease(COPD)");
		builder.append(hx_copdBox);
		// builder.nextRow();

		// ---------------------------------------------------------------------------
		builder.appendSeparator("Clinical Laboratory Values");

		ValueModel bunlsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_BUNLS);
		bunlsField = ComponentFactory.createFormattedTextField(bunlsModel,
				NumberFormat.getNumberInstance());
		bunlsField.setToolTipText("BUN (blood urea nitrogen) (mg/dL)");
		ValidationComponentUtils.setMessageKey(bunlsField,
				"TransplantSupport.BUN");
		builder.append("BUN:", bunlsField);

		ValueModel bililsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_BILILS);
		bililsField = ComponentFactory.createFormattedTextField(bililsModel,
				NumberFormat.getNumberInstance());
		bililsField.setToolTipText("Bilirubin (mg/dL)");
		ValidationComponentUtils.setMessageKey(bililsField,
				"TransplantSupport.Bilirubin");
		builder.append("Bilirubin:", bililsField);

		ValueModel crealsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_CREALS);
		crealsField = ComponentFactory.createFormattedTextField(crealsModel,
				NumberFormat.getNumberInstance());
		crealsField.setToolTipText("Creatinine (mg/dL)");
		ValidationComponentUtils.setMessageKey(crealsField,
				"TransplantSupport.Creatinine");
		builder.append("Creatinine:", crealsField);

		ValueModel hgblsModel = transSupportPresModel
				.getModel(TransplantSupport.PROPERTYNAME_HGBLS);
		hgblsField = ComponentFactory.createFormattedTextField(hgblsModel,
				NumberFormat.getNumberInstance());
		hgblsField.setToolTipText("Hemoglobin (g/dL)");
		ValidationComponentUtils.setMessageKey(hgblsField,
				"TransplantSupport.Hemoglobin");
		builder.append("Hemoglobin:", hgblsField);
		
		
		ValueModel colorModel = transSupportPresModel.getModel(TransplantSupport.PROPERTYNAME_PATIENT_COLOR);
		colorList = new JComboBox(new ComboBoxAdapter<String>(colorValues, colorModel));		
	
		// builder.nextRow();
		addGraphProperties(true);

		// The builder holds the layout container that we now return.
		transplantSupportPanel = builder.getPanel();

		updateComponentTreeMandatoryAndSeverity(transSupportPresModel
				.getValidationResultModel().getResult());
		
	
		// The builder holds the layout container that we now return.
		initEventHandling();
	
		return new IconFeedbackPanel(transSupportPresModel
				.getValidationResultModel(), transplantSupportPanel);

	}

	@Override
	protected void initEventHandling() {
		transSupportPresModel.getValidationResultModel()
				.addPropertyChangeListener(
						ValidationResultModel.PROPERTYNAME_RESULT,
						new ValidationChangeHandler());
	}

	@Override
	protected void updateComponentTreeMandatoryAndSeverity(
			ValidationResult result) {
		log.debug("UpdateCompTree: " + result);
		log.debug("keys contain:" + result.keyMap().keySet());
		ValidationComponentUtils
				.updateComponentTreeMandatoryAndBlankBackground(transplantSupportPanel);
		ValidationComponentUtils.updateComponentTreeSeverityBackground(
				transplantSupportPanel, result);
		if (!result.hasErrors())
			try {
				patientModel.execute();
			} catch (ModelExecuteException e) {
				ExceptionHandler.logger(e, log);
			}
	}

	@Override
	protected void addGraphProperties(boolean color) {
		// TODO Auto-generated method stub
		builder.appendSeparator("Graph Properties");
		// These come from the patientModel.
		builder.append("Patient Graph Color: ", colorList);
		builder.append(confidenceLimitBox);

	}

}
// colorModel = adapter.getModel("patientColor");

// colorList = new JComboBox(new ComboBoxAdapter(colorValues, colorModel));
