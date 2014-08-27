package lvadSupport;

import java.text.NumberFormat;

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

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class LVADSupportForm extends PatientModelForm {
	private static Logger log = Logger.getLogger(LVADSupportForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319950740880016103L;

	private JFormattedTextField birthDateField;

	private JFormattedTextField heightField;
	private JFormattedTextField weightField;

	private JFormattedTextField bun_prField;

	private JCheckBox hx_dialBox;
	private JCheckBox hx_htnBox;
	private JCheckBox vent_prBox;
	private JFormattedTextField mcsDateField;

	private JCheckBox lvadBox;
	private JCheckBox mb_nuero2Box;
	private JFormattedTextField mcsDate2Field;

	private JCheckBox lvad2Box;
	private JCheckBox mb_nuero3Box;
	private JFormattedTextField mcsDate3Field;

	private JCheckBox lvad3Box;
	private JPanel lvadSupportPanel;

	private LVADSupportPresentationModel lvadSupportPresModel;

	private JComboBox colorList;

	/*
	 * The input variables...
	 */

	/**
	 * EntryForm constructor
	 * 
	 * Initialize the class and creates actions for use in menus.
	 */
	public LVADSupportForm() {
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
		log.debug("Building LVADSupport panel");
		if (tsData == null)
			return (null);
		log.debug("patientModel");
		lvadSupportPresModel = new LVADSupportPresentationModel(
				(LVADSupport) patientModel);
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
		buildPatientDemographicsSection(lvadSupportPresModel, 2);

		ValueModel birthDateModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_BIRTHDATE);
		birthDateField = ComponentFactory.createDateField(birthDateModel);
		birthDateField.setToolTipText("Birth Date: [mm/dd/yyyy]");
		ValidationComponentUtils.setMessageKey(birthDateField,
				"LVADSupport.Birth Date");
		ValidationComponentUtils.setMandatory(birthDateField, false);
		builder.append("Birth Date:", birthDateField);

		builder.nextRow();

		builder.appendSeparator("Demographics");

		ValueModel heightModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_HEIGHT);
		heightField = ComponentFactory.createFormattedTextField(heightModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils.setMessageKey(heightField,
				"LVADSupport.Height");
		heightField.setToolTipText("Height in centimeters between 128-205");
		builder.append("Height: [cm]", heightField);

		ValueModel weightModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_WEIGHT);
		weightField = ComponentFactory.createFormattedTextField(weightModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils.setMessageKey(weightField,
				"LVADSupport.Weight");
		weightField.setToolTipText("Weight in KG between 24 and 172");
		builder.append("Weight: [kg]", weightField);

		builder.appendSeparator();

		ValueModel bun_prModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_BUN_PR);
		bun_prField = ComponentFactory.createFormattedTextField(bun_prModel,
				NumberFormat.getNumberInstance());
		ValidationComponentUtils.setMessageKey(bun_prField,
				"LVADSupport.Bun_pr");
		bun_prField.setToolTipText("BUN [mg/dL] between 9 and 115");
		builder.append("BUN: [mg/dL]", bun_prField);
		builder.nextRow();

		ValueModel hx_dialModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_HX_DIAL);
		hx_dialBox = ComponentFactory.createCheckBox(hx_dialModel,
				"History of dialysis");
		hx_dialBox.setToolTipText("History of dialysis");
		builder.append(hx_dialBox);

		ValueModel hx_htnModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_HX_HTN);
		hx_htnBox = ComponentFactory.createCheckBox(hx_htnModel,
				"History of hypertension");
		hx_htnBox.setToolTipText("History of hypertension");
		builder.append(hx_htnBox);

		ValueModel vent_prModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_VENT_PR);
		vent_prBox = ComponentFactory.createCheckBox(vent_prModel, "Ventilator Pre-MCS");
		vent_prBox.setToolTipText("Pre-MCS ventilator");
		builder.append(vent_prBox);
		builder.nextRow();


		builder.appendSeparator("Index Device");

		ValueModel mcsDateModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_MCS_DATE);
		mcsDateField = ComponentFactory.createDateField(mcsDateModel);
		mcsDateField.setToolTipText("MCS Date [mm/dd/yyyy]");
		ValidationComponentUtils.setMandatory(mcsDateField, true);
		ValidationComponentUtils.setMessageKey(mcsDateField,
				"LVADSupport.MCS_Date");

		builder.append("MCS Date:", mcsDateField);

		ValueModel lvadModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_LVAD);
		lvadBox = ComponentFactory.createCheckBox(lvadModel, "LVAD device");
		lvadBox.setToolTipText("LVAD device");
		builder.append(lvadBox);

		builder.nextRow();

		builder.appendSeparator("2nd Device");

		ValueModel mcsDateModel2 = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_MCS_2_DATE);
		mcsDate2Field = ComponentFactory.createDateField(mcsDateModel2);
		mcsDate2Field.setToolTipText("MCS Date, renewal 2 [mm/dd/yyyy]");
		ValidationComponentUtils.setMandatory(mcsDate2Field, false);
		ValidationComponentUtils.setMessageKey(mcsDate2Field,
				"LVADSupport.MCS_Date_2");

		builder.append("MCS Date:", mcsDate2Field);

		ValueModel lvad2Model = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_LVAD_2);
		lvad2Box = ComponentFactory.createCheckBox(lvad2Model, "LVAD device");
		lvad2Box.setToolTipText("LVAD device");
		builder.append(lvad2Box);

		ValueModel mb_nuero2Model = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_MB_NUERO_2);
		mb_nuero2Box = ComponentFactory
				.createCheckBox(mb_nuero2Model, "Neuro comp");
		mb_nuero2Box.setToolTipText("Neurological complication");
		builder.append(mb_nuero2Box);
		//builder.nextRow();

		builder.appendSeparator("3rd Device");

		ValueModel mcsDate3Model = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_MCS_3_DATE);
		mcsDate3Field = ComponentFactory.createDateField(mcsDate3Model);
		mcsDate3Field.setToolTipText("MCS Date, renewal 3 [mm/dd/yyyy]");
		ValidationComponentUtils.setMandatory(mcsDate3Field, false);
		ValidationComponentUtils.setMessageKey(mcsDate3Field,
				"LVADSupport.MCS_Date_3");

		builder.append("MCS Date:", mcsDate3Field);

		ValueModel lvad3Model = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_LVAD_3);
		lvad3Box = ComponentFactory.createCheckBox(lvad3Model, "LVAD device");
		lvad3Box.setToolTipText("LVAD device");
		builder.append(lvad3Box);

		ValueModel mb_nuero3Model = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_MB_NUERO_3);
		mb_nuero3Box = ComponentFactory
				.createCheckBox(mb_nuero3Model, "Neuro comp");
		mb_nuero3Box.setToolTipText("Neurological complication");
		builder.append(mb_nuero3Box);
		//builder.nextRow();

		ValueModel colorModel = lvadSupportPresModel
				.getModel(LVADSupport.PROPERTYNAME_PATIENT_COLOR);
		colorList = new JComboBox(new ComboBoxAdapter<String>(colorValues,
				colorModel));

		// builder.nextRow();
		addGraphProperties(true);

		// The builder holds the layout container that we now return.
		lvadSupportPanel = builder.getPanel();

		updateComponentTreeMandatoryAndSeverity(lvadSupportPresModel
				.getValidationResultModel().getResult());

		// The builder holds the layout container that we now return.
		initEventHandling();
		return new IconFeedbackPanel(lvadSupportPresModel
				.getValidationResultModel(), lvadSupportPanel);

	}

	@Override
	protected void initEventHandling() {
		lvadSupportPresModel.getValidationResultModel()
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
				.updateComponentTreeMandatoryAndBlankBackground(lvadSupportPanel);
		ValidationComponentUtils.updateComponentTreeSeverityBackground(
				lvadSupportPanel, result);
		if (!result.hasErrors())
			try {
				patientModel.execute();
			} catch (ModelExecuteException e) {
				ExceptionHandler.logger(e, log);
			}
	}

	@Override
	protected void addGraphProperties(boolean color) {
		builder.appendSeparator("Graph Properties");

		
		
		// These come from the patientModel.
		builder.append("Patient Graph Color: ", colorList);
		builder.append(confidenceLimitBox);

	}

}