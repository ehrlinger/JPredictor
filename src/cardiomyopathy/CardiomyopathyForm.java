package cardiomyopathy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

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

public class CardiomyopathyForm extends PatientModelForm {
	private static Logger log = Logger.getLogger(CardiomyopathyForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319950740880016103L;

	private JFormattedTextField birthDateField;

	private JFormattedTextField surgeryDateField;

	private JFormattedTextField miDateField;

	private JFormattedTextField ageField;

	private JFormattedTextField iv_pmiField;

	private JComboBox nyhaGroupList;

	private JComboBox cacGroupList;

	private JComboBox diabetesGroupList;

	private JFormattedTextField ef_comField;

	private JCheckBox afib_prBox;

	private JCheckBox chb_prBox;

	private JCheckBox avrgrgBox;

	private JCheckBox lmtanyBox;

	private JCheckBox rca50Box;

	private JCheckBox lcx70Box;
	private JCheckBox vd3Box;
	private JCheckBox varr_prBox;

	private JCheckBox hx_cvaBox;

	private JCheckBox hx_copdBox;

	private JCheckBox hx_popdzBox;
	private JCheckBox hx_pvdBox;

	private JFormattedTextField chol_prField;

	private JFormattedTextField hct_prField;

	private JCheckBox hx_rnldzBox;

	private JFormattedTextField bun_prField;
	private JFormattedTextField blrbn_prField;
	private JCheckBox executeCABGBox;
	protected JComboBox colorCABGList;

	private JCheckBox executeCBGVBox;
	protected JComboBox colorCBGVList;

	private JCheckBox executeSVRBox;
	protected JComboBox colorSVRList;

	private JCheckBox executeTXPLBox;
	protected JComboBox colorTXPLList;

	/*
	 * The input variables...
	 */

	private JPanel cardiomyopathySupportPanel;

	private CardiomyopathyPresentationModel cardiomyopathyPresModel;

	/**
	 * EntryForm constructor
	 * 
	 * Initialize the class and creates actions for use in menus.
	 */
	public CardiomyopathyForm() {
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
		log.debug("Building Cardiomyopathy panel");
		if (tsData == null)
			return (null);
		log.debug("patientModel");

		try {
			cardiomyopathyPresModel = new CardiomyopathyPresentationModel(
					(Cardiomyopathy) patientModel);

			// Fill the grid with components; the builder offers to create
			// frequently used components, e.g. separators and labels.

			// -----------------------------------------------------------
			// Start with patient Demographics. Naming/labeling is not required,
			// though
			// suggested for legends.
			//
			// Build the demographics with 2 columns.
			buildPatientDemographicsSection((PresentationModel<PatientModel>) cardiomyopathyPresModel);

			ValueModel birthDateModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_BIRTHDATE);
			birthDateField = ComponentFactory.createDateField(birthDateModel);
			birthDateField.setToolTipText("Birth Date: [mm/dd/yyyy]");
			ValidationComponentUtils.setMessageKey(birthDateField,
					"Cardiomyopathy.Birth Date");
			ValidationComponentUtils.setMandatory(birthDateField, true);
			builder.append("Birth Date:", birthDateField);

			ValueModel surgeryDateModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_SURGERYDATE);
			surgeryDateField = ComponentFactory
					.createDateField(surgeryDateModel);
			surgeryDateField.setToolTipText("Surgery Date: [mm/dd/yyyy]");
			ValidationComponentUtils.setMessageKey(surgeryDateField,
					"Cardiomyopathy.Surgery Date");
			ValidationComponentUtils.setMandatory(surgeryDateField, true);
			builder.append("Surgery Date:", surgeryDateField);

			ValueModel ageModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_AGE);
			ageField = ComponentFactory.createFormattedTextField(ageModel,
					NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(ageField,
					"Cardiomyopathy.age");
			ageField.setToolTipText("Age at surgery date in years");
			builder.append("Age: [yrs]", ageField);

			ValueModel miDateModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_MIDATE);
			miDateField = ComponentFactory.createDateField(miDateModel);
			miDateField.setToolTipText("Date of MI: [mm/dd/yyyy]");
			ValidationComponentUtils.setMessageKey(miDateField,
					"Cardiomyopathy.MI Date");
			ValidationComponentUtils.setMandatory(miDateField, true);
			builder.append("Date of MI:", miDateField);

			ValueModel iv_pmiModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_IV_PMI);
			iv_pmiField = ComponentFactory.createFormattedTextField(
					iv_pmiModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(iv_pmiField,
					"Cardiomyopathy.iv_pmi");
			iv_pmiField
					.setToolTipText("Interval between MI date and surgery date in years");
			builder.append("MI-Surgery interval: [yrs]", iv_pmiField);

			builder.appendSeparator("Cardiac Morbidity");

			ValueModel ef_comModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_EF_COM);
			ef_comField = ComponentFactory.createFormattedTextField(
					ef_comModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(ef_comField,
					"Cardiomyopathy.LV ejection fraction");
			ef_comField
					.setToolTipText("Left ventricular ejection fraction (%)");
			builder.append("LV ejection fraction:", ef_comField);

			ValueModel nyhaModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_NYHA_GROUP);
			List<String> possibleValues = new ArrayList<String>();
			possibleValues = new ArrayList<String>();
			possibleValues.add("");
			possibleValues.add("I");
			possibleValues.add("II");
			possibleValues.add("III");
			possibleValues.add("IV");

			final ComboBoxAdapter<String> comboBoxAdapter = new ComboBoxAdapter<String>(
					possibleValues, nyhaModel);
			nyhaGroupList = new JComboBox();
			 nyhaGroupList.setModel(comboBoxAdapter);

			builder.append("NYHA Function Class:", nyhaGroupList);

			ValueModel cac_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_CAC_PR);
			possibleValues = new ArrayList<String>();
			possibleValues.add("0");
			possibleValues.add("1");
			possibleValues.add("2");
			possibleValues.add("3");
			possibleValues.add("4");
			cacGroupList = new JComboBox(new ComboBoxAdapter<String>(
					possibleValues, cac_prModel));
			builder.append("Canadian Angina Class:", cacGroupList);

			ValueModel afib_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_AFIB_PR);
			afib_prBox = ComponentFactory.createCheckBox(afib_prModel,
					"Atrial Fibrillation");
			afib_prBox.setToolTipText("");
			builder.append(afib_prBox);

			ValueModel chb_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_CHB_PR);
			chb_prBox = ComponentFactory.createCheckBox(chb_prModel,
					"Complete heart block/pacer");
			chb_prBox.setToolTipText("Complete heart block/pacer");
			builder.append(chb_prBox);
			ValueModel varr_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_VARR_PR);
			varr_prBox = ComponentFactory.createCheckBox(varr_prModel,
					"Ventricular Arrhythmia");
			varr_prBox.setToolTipText("Ventricular Arrhythmia");
			builder.append(varr_prBox);
			ValueModel avrgrgModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_AVRGRG);
			avrgrgBox = ComponentFactory.createCheckBox(avrgrgModel,
					"AV Regurgitation");
			avrgrgBox.setToolTipText("AV Regurgitation");
			builder.append(avrgrgBox);

			ValueModel lmtanyModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_LMTANY);
			lmtanyBox = ComponentFactory.createCheckBox(lmtanyModel,
					"LMT disease");
			lmtanyBox.setToolTipText("LMT disease: stenosis>0%");
			builder.append(lmtanyBox);

			ValueModel rca50Model = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_RCA50);
			rca50Box = ComponentFactory.createCheckBox(rca50Model,
					"RCA disease");
			rca50Box.setToolTipText("RCA disease: stenosis>=50%");
			builder.append(rca50Box);

			ValueModel lcx70Model = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_LCX70);
			lcx70Box = ComponentFactory.createCheckBox(lcx70Model,
					"LCx disease");
			lcx70Box.setToolTipText("LCx disease: stenosis>=70%");
			builder.append(lcx70Box);

			ValueModel vd3Model = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_VD3);
			vd3Box = ComponentFactory.createCheckBox(vd3Model,
					"3-System Disease");
			vd3Box
					.setToolTipText("3-System Disease: All LCx, LAD, RCA stenosis >= 50%");
			builder.append(vd3Box);

			// ------------------------------------------------------------------

			builder.appendSeparator("Non-Cardiac Comorbidity");

			ValueModel hx_cvaModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_HX_CVA);
			hx_cvaBox = ComponentFactory.createCheckBox(hx_cvaModel,
					"History of stroke");
			hx_cvaBox.setToolTipText("History of stroke");
			builder.append(hx_cvaBox);

			ValueModel hx_copdModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_HX_COPD);
			hx_copdBox = ComponentFactory.createCheckBox(hx_copdModel,
					"History of COPD");
			hx_copdBox.setToolTipText("History of COPD");
			builder.append(hx_copdBox);

			ValueModel hx_popdzModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_HX_POPDZ);
			hx_popdzBox = ComponentFactory.createCheckBox(hx_popdzModel,
					"History of popliteal disease");
			hx_popdzBox.setToolTipText("History of popliteal disease");
			builder.append(hx_popdzBox);

			ValueModel hx_pvdModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_HX_PVD);
			hx_pvdBox = ComponentFactory.createCheckBox(hx_pvdModel,
					"History of peripheral vascular disease");
			hx_pvdBox.setToolTipText("History of peripheral vascular disease");
			builder.append(hx_pvdBox);

			ValueModel hx_rnldzModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_HX_RNLDZ);
			hx_rnldzBox = ComponentFactory.createCheckBox(hx_rnldzModel,
					"History of renal disease");
			hx_rnldzBox.setToolTipText("History of renal disease");
			builder.append(hx_rnldzBox);

			builder.nextRow();
			ValueModel diabetesModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_DIABETES);
			possibleValues = new ArrayList<String>();
			possibleValues.add("");
			possibleValues.add("Treated");
			possibleValues.add("Insulin");
			diabetesGroupList = new JComboBox(new ComboBoxAdapter<String>(
					possibleValues, diabetesModel));
			builder.append("Diabetes:", diabetesGroupList);

			ValueModel chol_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_CHOL_PR);
			chol_prField = ComponentFactory.createFormattedTextField(
					chol_prModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(chol_prField,
					"Cardiomyopathy.Cholesterol");
			chol_prField.setToolTipText("Cholesterol (mg/dL)");
			builder.append("Cholesterol (mg/dL):", chol_prField);

			ValueModel hct_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_HCT_PR);
			hct_prField = ComponentFactory.createFormattedTextField(
					hct_prModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(hct_prField,
					"Cardiomyopathy.Hematocrit");
			hct_prField.setToolTipText("Hematocrit (%)");
			builder.append("Hematocrit (%):", hct_prField);

			ValueModel bun_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_BUN_PR);
			bun_prField = ComponentFactory.createFormattedTextField(
					bun_prModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(bun_prField,
					"Cardiomyopathy.Blood Urea Nitrogen");
			bun_prField.setToolTipText("Blood urea nitrogen (mg/dL)");
			builder.append("Blood urea nitrogen (mg/dL):", bun_prField);

			ValueModel blrbn_prModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_BLRBN_PR);
			blrbn_prField = ComponentFactory.createFormattedTextField(
					blrbn_prModel, NumberFormat.getNumberInstance());
			ValidationComponentUtils.setMessageKey(blrbn_prField,
					"Cardiomyopathy.Bilirubin");
			bun_prField.setToolTipText("Bilirubin (mg/dL)");
			builder.append("Bilirubin (mg/dL):", blrbn_prField);

			builder.appendSeparator("Graph Properties");

			ValueModel colorCABGModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_COLOR_CABG);
			colorCABGList = new JComboBox(new ComboBoxAdapter<String> (colorValues,
					colorCABGModel));
			ValueModel executeCABGModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_EXECUTE_CABG);
			executeCABGBox = ComponentFactory.createCheckBox(executeCABGModel,
					"CABG");
			hx_cvaBox.setToolTipText("Include CABG Model");
			builder.append(executeCABGBox);

			ValueModel executeCBGVModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_EXECUTE_CBGV);

			ValueModel colorCBGVModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_COLOR_CBGV);
			colorCBGVList = new JComboBox(new ComboBoxAdapter<String> (colorValues,
					colorCBGVModel));

			executeCBGVBox = ComponentFactory.createCheckBox(executeCBGVModel,
					"CABG+MVA");
			hx_cvaBox.setToolTipText("Include CABG+MVA Model");
			builder.append(executeCBGVBox);

			ValueModel executeSVRModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_EXECUTE_SVR);
			executeSVRBox = ComponentFactory.createCheckBox(executeSVRModel,
					"CABG+SVR");
			hx_cvaBox.setToolTipText("Include CABG+SVR Model");
			builder.append(executeSVRBox);
			ValueModel colorSVRModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_COLOR_SVR);
			colorSVRList = new JComboBox(new ComboBoxAdapter<String>(colorValues,
					colorSVRModel));

			ValueModel executeTXPLModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_EXECUTE_TXPL);
			executeTXPLBox = ComponentFactory.createCheckBox(executeTXPLModel,
					"LCTx");
			hx_cvaBox.setToolTipText("Include LCTx Model");
			builder.append(executeTXPLBox);
			ValueModel colorTXPLModel = cardiomyopathyPresModel
					.getModel(Cardiomyopathy.PROPERTYNAME_COLOR_TXPL);

			colorTXPLList = new JComboBox(new ComboBoxAdapter<String>(colorValues,
					colorTXPLModel));

			builder.append("CABG Color: ", colorCABGList);

			builder.append("CABG+MVA Color: ", colorCBGVList);
			builder.append("CABG+SVR Color: ", colorSVRList);
			builder.append("LCTx Color: ", colorTXPLList);

			addGraphProperties(false);
			// These come from the patientModel.

			builder.append(confidenceLimitBox);
			// The builder holds the layout container that we now return.
			cardiomyopathySupportPanel = builder.getPanel();

			updateComponentTreeMandatoryAndSeverity(cardiomyopathyPresModel
					.getValidationResultModel().getResult());

			// The builder holds the layout container that we now return.
			initEventHandling();
		} catch (NullPointerException e1) {
			log.debug("");
			log.debug("NullPointer");
			ExceptionHandler.logger(e1, log);
		}
		return new IconFeedbackPanel(cardiomyopathyPresModel
				.getValidationResultModel(), cardiomyopathySupportPanel);

	}

	@Override
	protected void initEventHandling() {
		cardiomyopathyPresModel.getValidationResultModel()
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
				.updateComponentTreeMandatoryAndBlankBackground(cardiomyopathySupportPanel);
		ValidationComponentUtils.updateComponentTreeSeverityBackground(
				cardiomyopathySupportPanel, result);
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
