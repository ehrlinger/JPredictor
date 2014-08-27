package picabg;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
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
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class PICABGForm extends PatientModelForm {

	private static Logger log = Logger.getLogger(PICABGForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319950740880016103L;

	private JCheckBox afib_prBox;

	private JFormattedTextField ageField;

	private JFormattedTextField creat_prField;

	private JRadioButton femaleButton;

	private JFormattedTextField hct_prField;

	private JFormattedTextField heightField;

	private JCheckBox hx_copdBox;

	private JComboBox hx_diabetesList;

	private JCheckBox hx_htnBox;

	private JCheckBox hx_miBox;

	private JCheckBox hx_pvdBox;

	private JCheckBox hx_smokeBox;

	private JComboBox itaList;

	private JFormattedTextField ladField;

	private JFormattedTextField lcxField;

	private JFormattedTextField lmtField;

	private JComboBox lvcathList;

	private JRadioButton maleButton;

	private PICABGPresentationModel patientPresModel;

	private JComponent picabgPanel;

	private JFormattedTextField rcaField;

	private JFormattedTextField trig_prField;

	private JFormattedTextField weightField;

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
	public PICABGForm() {
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
		log.debug("Building PICABG panel");
		patientPresModel = new PICABGPresentationModel((PICABG) patientModel);
		// Fill the grid with components; the builder offers to create
		// frequently used components, e.g. separators and labels.
		buildPatientDemographicsSection(patientPresModel);

		// female is a boolean, true/false. Constrained variables do not need
		// validation.
		ValueModel femaleModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_FEMALE);

		maleButton = ComponentFactory.createRadioButton(femaleModel, false, "male");

		femaleButton = ComponentFactory.createRadioButton(femaleModel, true,
				"female");

		builder.append(maleButton);
		builder.append(femaleButton);

		// Patient Age
		ValueModel ageModel = patientPresModel.getModel(PICABG.PROPERTYNAME_AGE);
		ageField = ComponentFactory.createIntegerField(ageModel);
		builder.append(" Patient Age (>18):", ageField);
		ValidationComponentUtils.setMessageKey(ageField, "picabg.Patient Age");

		// Patient height
		ValueModel heightModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_HEIGHT);
		heightField = ComponentFactory.createFormattedTextField(heightModel,
				NumberFormat.getNumberInstance());
		builder.append("Height: [cm]", heightField);
		ValidationComponentUtils
				.setMessageKey(heightField, "picabg.Patient Height");

		// Patient weight
		ValueModel weightModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_WEIGHT);
		weightField = ComponentFactory.createFormattedTextField(weightModel,
				NumberFormat.getNumberInstance());
		builder.append("weight: [KG]", weightField);
		ValidationComponentUtils.setMandatory(weightField, true);
		ValidationComponentUtils
				.setMessageKey(weightField, "picabg.Patient Weight");

		// builder.nextRow();

		builder.appendSeparator("Cardiac History");

		ValueModel lvcathModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_LVCATH);
		List<String> possibleValues = new ArrayList<String>();
		possibleValues.add("None");
		possibleValues.add("Mild");
		possibleValues.add("Moderate");
		possibleValues.add("Severe");
		lvcathList = new JComboBox(new ComboBoxAdapter<String>(possibleValues, lvcathModel));
		builder.append("Grade of LV dysfunction :", lvcathList);
		// builder.nextRow();

		ValueModel hx_diabetesModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_DIABETES);
		possibleValues = new ArrayList<String>();
		possibleValues.add("None");
		possibleValues.add("Diet");
		possibleValues.add("Oral");
		possibleValues.add("Insulin");
		hx_diabetesList = new JComboBox(new ComboBoxAdapter<String>(possibleValues,
				hx_diabetesModel));
		builder.append("HX of diabetes: ", hx_diabetesList);

		// builder.nextRow();

		ValueModel hx_smokeModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_HX_SMOKE);
		hx_smokeBox = ComponentFactory.createCheckBox(hx_smokeModel, "HX smoking");
		builder.append(hx_smokeBox);

		ValueModel hx_miModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_HX_MI);
		hx_miBox = ComponentFactory.createCheckBox(hx_miModel,
				"HX myocardial infarction");
		builder.append(hx_miBox);

		ValueModel hx_pvdModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_HX_PVD);
		hx_pvdBox = ComponentFactory.createCheckBox(hx_pvdModel, "HX PVD");
		builder.append(hx_pvdBox);

		ValueModel hx_copdModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_HX_COPD);
		hx_copdBox = ComponentFactory.createCheckBox(hx_copdModel, "HX COPD");
		builder.append(hx_copdBox);
		ValueModel hx_htnModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_HX_HTN);
		hx_htnBox = ComponentFactory.createCheckBox(hx_htnModel, "HX hypertension");
		builder.append(hx_htnBox);
		builder.nextColumn(2);

		builder.appendSeparator("Preoperative Lab Values");
		ValueModel afib_prModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_AFIB_PR);
		afib_prBox = ComponentFactory.createCheckBox(afib_prModel, "AFIB");
		builder.append(afib_prBox);
		builder.nextColumn(2);

		// Creatinine
		ValueModel creat_prModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_CREAT_PR);
		creat_prField = ComponentFactory.createFormattedTextField(creat_prModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils.setMessageKey(creat_prField, "picabg.Creatinine");
		builder.append("Creatinine (mg/dL):", creat_prField);

		ValueModel hct_prModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_HCT_PR);
		hct_prField = ComponentFactory.createFormattedTextField(hct_prModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils.setMessageKey(hct_prField, "picabg.Hematocrit");
		builder.append("Hematocrit [%]:", hct_prField);

		ValueModel trig_prModel = patientPresModel
				.getModel(PICABG.PROPERTYNAME_TRIG_PR);
		trig_prField = ComponentFactory.createFormattedTextField(trig_prModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils
				.setMessageKey(trig_prField, "picabg.Triglycerides");
		builder.append("Triglycerides (mg/dL):", trig_prField);

		ValueModel lmtModel = patientPresModel.getModel(PICABG.PROPERTYNAME_LMT);
		lmtField = ComponentFactory.createFormattedTextField(lmtModel, NumberFormat
				.getNumberInstance());
		ValidationComponentUtils.setMessageKey(lmtField, "picabg.LMT Disease");
		builder.append("LMT Disease [% stenosis]:", lmtField);

		ValueModel ladModel = patientPresModel.getModel(PICABG.PROPERTYNAME_LAD);
		ladField = ComponentFactory.createFormattedTextField(ladModel, NumberFormat
				.getNumberInstance());
		ValidationComponentUtils.setMessageKey(ladField, "picabg.LAD Disease");
		builder.append("LAD Disease [max % stenosis]:", ladField);

		ValueModel lcxModel = patientPresModel.getModel(PICABG.PROPERTYNAME_LCX);
		lcxField = ComponentFactory.createFormattedTextField(lcxModel, NumberFormat
				.getNumberInstance());
		ValidationComponentUtils.setMessageKey(lcxField, "picabg.LCX Disease");
		builder.append("LCX Disease [max % stenosis]:", lcxField);

		ValueModel rcaModel = patientPresModel.getModel(PICABG.PROPERTYNAME_RCA);
		rcaField = ComponentFactory.createFormattedTextField(rcaModel, NumberFormat
				.getNumberInstance());
		ValidationComponentUtils.setMessageKey(rcaField,
				"picabg.Right Coronary System Disease");
		builder.append("Right coronary system Disease [% stenosis]:", rcaField);

		ValueModel itaModel = patientPresModel.getModel(PICABG.PROPERTYNAME_ITA);
		possibleValues = new ArrayList<String>();
		possibleValues.add("None");
		possibleValues.add("Left");
		possibleValues.add("Right");
		possibleValues.add("Both");
		itaList = new JComboBox(new ComboBoxAdapter<String>(possibleValues, itaModel));
		builder.append("Proposed ITA grafts: ", itaList);
		ValueModel colorModel = patientPresModel.getModel(PICABG.PROPERTYNAME_PATIENT_COLOR);
		colorList = new JComboBox(new ComboBoxAdapter<String>(colorValues, colorModel));		

		addGraphProperties(true);

		picabgPanel = builder.getPanel();
		// Synchronize the presentation with the current validation state.
		// If you want to show no validation info before the user has typed
		// anything in the form, use the commented EMPTY result
		// instead of the model's validation result.

		updateComponentTreeMandatoryAndSeverity(patientPresModel
				.getValidationResultModel().getResult());

		// The builder holds the layout container that we now return.
		initEventHandling();
		return new IconFeedbackPanel(patientPresModel.getValidationResultModel(),
				picabgPanel);

	}

	protected void initEventHandling() {
		patientPresModel.getValidationResultModel().addPropertyChangeListener(
				ValidationResultModel.PROPERTYNAME_RESULT,
				new ValidationChangeHandler());
	}

	protected void updateComponentTreeMandatoryAndSeverity(ValidationResult result) {
		log.debug("UpdateCompTree: " + result);
		log.debug("keys contain:" + result.keyMap().keySet());

		ValidationComponentUtils
				.updateComponentTreeMandatoryAndBlankBackground(picabgPanel);
		ValidationComponentUtils.updateComponentTreeSeverityBackground(picabgPanel,
				result);
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
