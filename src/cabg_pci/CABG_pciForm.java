package cabg_pci;

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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class CABG_pciForm extends PatientModelForm {
	private static Logger log = Logger.getLogger(CABG_pciForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319950740880016103L;

	private JFormattedTextField birthDateField;

	private JFormattedTextField heightField;

	private JFormattedTextField weightField;

	private JFormattedTextField ageField;
	private JCheckBox emgsrgBox;
	private JCheckBox afib_prBox;

	private JFormattedTextField lvefField;

	private JComboBox mvrgsevGroupList;

	private JComboBox cad_sysGroupList;
	private JFormattedTextField lmtField;

	private JCheckBox hx_htnBox;
	private JFormattedTextField bpsystField;
	private JFormattedTextField bpdiasField;
	private JCheckBox hx_maligBox;
	private JCheckBox hx_cvaBox;
	private JCheckBox hx_chfBox;
	private JCheckBox hx_copdBox;

	private JCheckBox hx_diabBox;
	private JCheckBox hx_smokeBox;

	private JCheckBox hx_rnldzBox;
	private JCheckBox hx_fcadBox;
	private JFormattedTextField hct_prField;

	private JFormattedTextField bun_prField;
	private JFormattedTextField creat_prField;
	private JCheckBox executeCABGBox;
	protected JComboBox colorCABGList;

	private JCheckBox executeBMSBox;
	protected JComboBox colorBMSList;

	private JCheckBox executeDESBox;
	protected JComboBox colorDESList;

	/*
	 * The input variables...
	 */

	private JPanel cabg_pciSupportPanel;

	private CABG_pciPresentationModel cabg_pciPresModel;

	/**
	 * EntryForm constructor
	 * 
	 * Initialize the class and creates actions for use in menus.
	 */
	public CABG_pciForm() {
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
		log.debug("Building cabg_pci panel");
		if (tsData == null)
			return (null);
		log.debug("patientModel");

		try {
			cabg_pciPresModel = new CABG_pciPresentationModel(
					(CABG_pci) patientModel);

			// Fill the grid with components; the builder offers to create
			// frequently used components, e.g. separators and labels.

			// -----------------------------------------------------------
			// Start with patient Demographics. Naming/labeling is not required,
			// though
			// suggested for legends.
			//
			// Build the demographics with 2 columns.
			buildPatientDemographicsSection((PresentationModel<PatientModel>) cabg_pciPresModel);

			ValueModel birthDateModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_BIRTHDATE);
			birthDateField = ComponentFactory.createDateField(birthDateModel);
			birthDateField.setToolTipText("Birth Date: [mm/dd/yyyy]");
			ValidationComponentUtils.setMessageKey(birthDateField,
					"cabg_pci.Birth Date");
			ValidationComponentUtils.setMandatory(birthDateField, true);
			builder.append("Birth Date:", birthDateField);

			ValueModel ageModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_AGE);
			ageField = ComponentFactory.createFormattedTextField(ageModel,
					NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(ageField, "cabg_pci.age");
			ageField.setToolTipText("Age in years");
			builder.append("Age: [yrs]", ageField);
			// female is a boolean, true/false.
			ValueModel femaleModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_FEMALE);
			JRadioButton femaleBox = ComponentFactory.createRadioButton(
					femaleModel, true, "Female");
			JRadioButton maleBox = ComponentFactory.createRadioButton(
					femaleModel, false, "Male");
			builder.append(maleBox);
			builder.append(femaleBox);
			// builder.nextRow();
			ValueModel heightModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HT);
			heightField = ComponentFactory.createFormattedTextField(
					heightModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(heightField, "CABG_pci.ht");
			heightField.setToolTipText("Height in centimeters between 100-225");
			builder.append("Height: [cm]", heightField);

			ValueModel weightModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_WT);
			weightField = ComponentFactory.createFormattedTextField(
					weightModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(weightField, "CABG_pci.wt");
			weightField.setToolTipText("Weight in kilograms between ");
			builder.append("Weight: [KG]", weightField);

			builder.appendSeparator("Cardiac Morbidity");
			ValueModel emgsrgModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_EMGSRG);
			emgsrgBox = ComponentFactory.createCheckBox(emgsrgModel,
					"Emergent status");
			emgsrgBox.setToolTipText("Emergent status");
			builder.append(emgsrgBox);

			ValueModel afib_prModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_AFIB_PR);
			afib_prBox = ComponentFactory.createCheckBox(afib_prModel,
					"Atrial Fibrillation");
			afib_prBox.setToolTipText("");
			builder.append(afib_prBox);

			ValueModel lvefModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_LVEF);
			lvefField = ComponentFactory.createFormattedTextField(lvefModel,
					NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(lvefField,
					"cabg_pci.LV ejection fraction");
			lvefField.setToolTipText("Left ventricular ejection fraction (%)");
			builder.append("LV ejection fraction:", lvefField);

			ValueModel mvrgsevModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_MVRGSEV);
			List<String> possibleValues = new ArrayList<String>();
			possibleValues = new ArrayList<String>();
			possibleValues.add("0");
			possibleValues.add("1");
			possibleValues.add("2");
			possibleValues.add("3");
			possibleValues.add("4");

			final ComboBoxAdapter<String> comboBoxAdapter = new ComboBoxAdapter<String>(
					possibleValues, mvrgsevModel);
			mvrgsevGroupList = new JComboBox();
			mvrgsevGroupList.setModel(comboBoxAdapter);

			builder.append("MV regurgitation grade:", mvrgsevGroupList);

			ValueModel cad_sysModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_CAD_SYS);
			possibleValues = new ArrayList<String>();
			possibleValues.add("0");
			possibleValues.add("1");
			possibleValues.add("2");
			possibleValues.add("3");
			cad_sysGroupList = new JComboBox(new ComboBoxAdapter<String>(
					possibleValues, cad_sysModel));
			builder.append("Number of coronary systems>=50% :",
					cad_sysGroupList);

			ValueModel lmtModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_LMT);
			lmtField = ComponentFactory.createFormattedTextField(lmtModel,
					NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(lmtField, "CABG_pci.lmt");
			lmtField.setToolTipText("LMT disease: stenosis  (0.00-1.00)");
			builder.append("LMT disease: ", lmtField);

			ValueModel hx_htnModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_HTN);
			hx_htnBox = ComponentFactory.createCheckBox(hx_htnModel,
					"History of Hypertension");
			hx_htnBox.setToolTipText("History of Hypertension");
			builder.append(hx_htnBox);
			builder.nextRow();
			
			ValueModel bpsystModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_BPSYST);
			bpsystField = ComponentFactory.createFormattedTextField(
					bpsystModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(bpsystField,
					"CABG_pci.bpsyst");
			bpsystField.setToolTipText("Systolic Blood Pressure");
			builder.append("Systolic Blood Pressure: ", bpsystField);

			ValueModel bpdiasModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_BPDIAS);
			bpdiasField = ComponentFactory.createFormattedTextField(
					bpdiasModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(bpdiasField,
					"CABG_pci.bpdias");
			bpdiasField.setToolTipText("Diastolic Blood Pressure");
			builder.append("Diastolic Blood Pressure: ", bpdiasField);

			// ------------------------------------------------------------------

			builder.appendSeparator("Non-Cardiac Comorbidity");
			ValueModel hx_maligModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_MALIG);
			hx_maligBox = ComponentFactory.createCheckBox(hx_maligModel,
					"History of malignancy");
			hx_maligBox.setToolTipText("History of malignancy");
			builder.append(hx_maligBox);

			ValueModel hx_cvaModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_CVA);
			hx_cvaBox = ComponentFactory.createCheckBox(hx_cvaModel,
					"History of stroke");
			hx_cvaBox.setToolTipText("History of stroke");
			builder.append(hx_cvaBox);

			ValueModel hx_chfModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_CHF);
			hx_chfBox = ComponentFactory.createCheckBox(hx_chfModel,
					"History of CHF");
			hx_chfBox.setToolTipText("History of CHF");
			builder.append(hx_chfBox);

			ValueModel hx_copdModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_COPD);
			hx_copdBox = ComponentFactory.createCheckBox(hx_copdModel,
					"History of COPD");
			hx_copdBox.setToolTipText("History of COPD");
			builder.append(hx_copdBox);

			ValueModel hx_diabModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_DIAB);
			hx_diabBox = ComponentFactory.createCheckBox(hx_diabModel,
					"History of diabetes");
			hx_diabBox.setToolTipText("History of diabetes");
			builder.append(hx_diabBox);
			ValueModel hx_smokeModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_SMOKE);
			hx_smokeBox = ComponentFactory.createCheckBox(hx_smokeModel,
					"History of smoke");
			hx_smokeBox.setToolTipText("History of smoke");
			builder.append(hx_smokeBox);

			ValueModel hx_rnldzModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_RNLDZ);
			hx_rnldzBox = ComponentFactory.createCheckBox(hx_rnldzModel,
					"History of renal disease");
			hx_rnldzBox.setToolTipText("History of renal disease");
			builder.append(hx_rnldzBox);

			ValueModel hx_fcadModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HX_FCAD);
			hx_fcadBox = ComponentFactory.createCheckBox(hx_fcadModel,
					"Family history of CAD");
			hx_fcadBox.setToolTipText("Family history of CAD");
			builder.append(hx_fcadBox);

			// builder.nextRow();
			ValueModel hct_prModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_HCT_PR);
			hct_prField = ComponentFactory.createFormattedTextField(
					hct_prModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(hct_prField,
					"cabg_pci.hct_pr");
			hct_prField.setToolTipText("Hematocrit (%)");
			builder.append("Hematocrit (%):", hct_prField);

			ValueModel creat_prModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_CREAT_PR);
			creat_prField = ComponentFactory.createFormattedTextField(
					creat_prModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(creat_prField,
					"cabg_pci.creat_pr");
			creat_prField.setToolTipText("Creatinine ");
			builder.append("Creatinine:", creat_prField);

			ValueModel bun_prModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_BUN_PR);
			bun_prField = ComponentFactory.createFormattedTextField(
					bun_prModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(bun_prField,
					"cabg_pci.Blood Urea Nitrogen");
			bun_prField.setToolTipText("Blood urea nitrogen (mg/dL)");
			builder.append("Blood urea nitrogen (mg/dL):", bun_prField);

			builder.appendSeparator("Graph Properties");

			ValueModel colorCABGModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_COLOR_CABG);
			colorCABGList = new JComboBox(new ComboBoxAdapter<String>(
					colorValues, colorCABGModel));
			ValueModel executeCABGModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_EXECUTE_CABG);
			executeCABGBox = ComponentFactory.createCheckBox(executeCABGModel,
					"CABG");
			hx_cvaBox.setToolTipText("Include CABG Model");
			builder.append(executeCABGBox);

			ValueModel executeBMSModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_EXECUTE_BMS);

			ValueModel colorBMSModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_COLOR_BMS);
			colorBMSList = new JComboBox(new ComboBoxAdapter<String>(
					colorValues, colorBMSModel));

			executeBMSBox = ComponentFactory.createCheckBox(executeBMSModel,
					"BMS");
			hx_cvaBox.setToolTipText("Include BMS Model");
			builder.append(executeBMSBox);

			ValueModel executeDESModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_EXECUTE_DES);
			executeDESBox = ComponentFactory.createCheckBox(executeDESModel,
					"DES");
			hx_cvaBox.setToolTipText("Include DES Model");
			builder.append(executeDESBox);
			ValueModel colorDESModel = cabg_pciPresModel
					.getModel(CABG_pci.PROPERTYNAME_COLOR_DES);
			colorDESList = new JComboBox(new ComboBoxAdapter<String>(
					colorValues, colorDESModel));
			builder.nextRow();
			builder.append("CABG Color: ", colorCABGList);

			builder.append("BMS Color: ", colorBMSList);
			builder.append("DES Color: ", colorDESList);

			addGraphProperties(false);
			// These come from the patientModel.

			builder.append(confidenceLimitBox);
			// The builder holds the layout container that we now return.
			cabg_pciSupportPanel = builder.getPanel();

			//updateComponentTreeMandatoryAndSeverity(cabg_pciPresModel
				//	.getValidationResultModel().getResult());

			// The builder holds the layout container that we now return.
			initEventHandling();
		} catch (NullPointerException e1) {
			log.debug("");
			log.debug("NullPointer in build");
			ExceptionHandler.logger(e1, log);
		}
		return new IconFeedbackPanel(cabg_pciPresModel
				.getValidationResultModel(), cabg_pciSupportPanel);

	}

	@Override
	protected void initEventHandling() {
		cabg_pciPresModel.getValidationResultModel().addPropertyChangeListener(
				ValidationResultModel.PROPERTYNAME_RESULT,
				new ValidationChangeHandler());
	}

	@Override
	protected void updateComponentTreeMandatoryAndSeverity(
			ValidationResult result) {
		log.debug("UpdateCompTree: " + result);
		log.debug("keys contain:" + result.keyMap().keySet());
		ValidationComponentUtils
				.updateComponentTreeMandatoryAndBlankBackground(cabg_pciSupportPanel);
		ValidationComponentUtils.updateComponentTreeSeverityBackground(
				cabg_pciSupportPanel, result);
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

	}

}
