package postInfarctVSD;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class PostInfarctVSDForm extends PatientModelForm {
	private static Logger log = Logger.getLogger(PostInfarctVSDForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319950740880016103L;

	private JFormattedTextField bunField;

	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

	private JFormattedTextField iv_misgField;

	private JFormattedTextField opDateField;

	private JCheckBox pereffBox;

	private PostInfarctVSDPresentationModel postInfarctPresModel;

	private JPanel postInVSDPanel;

	private JCheckBox pvdBox;

	private JCheckBox rv_abnorBox;

	private JComboBox urineList;
	
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
	public PostInfarctVSDForm() {
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
		log.debug("Building PostInfarctVSD panel");
		postInfarctPresModel = new PostInfarctVSDPresentationModel(
				(PostInfarctVSD) patientModel);

		buildPatientDemographicsSection(postInfarctPresModel);

		builder.appendSeparator("");
		ValueModel iv_misgModel = postInfarctPresModel
				.getModel(PostInfarctVSD.PROPERTYNAME_IV_MISG);
		iv_misgField = ComponentFactory.createIntegerField(iv_misgModel);
		ValidationComponentUtils.setMessageKey(iv_misgField,
				"postInfarctVSD.Time between MI and operation");
		builder.append("Interval from MI to operation (days):", iv_misgField);

		ValueModel rv_abnorModel = postInfarctPresModel
				.getModel(PostInfarctVSD.PROPERTYNAME_RV_ABNOR);
		rv_abnorBox = ComponentFactory.createCheckBox(rv_abnorModel,
				"Right Ventricular Dysfunction");
		builder.append(rv_abnorBox);

		ValueModel pereffModel = postInfarctPresModel
				.getModel(PostInfarctVSD.PROPERTYNAME_PEREFF);
		pereffBox = ComponentFactory.createCheckBox(pereffModel,
				"Pericardial Effusion");
		builder.append(pereffBox);

		List<String> possibleValues = new ArrayList<String>();
		ValueModel urineModel = postInfarctPresModel
				.getModel(PostInfarctVSD.PROPERTYNAME_URINEOUTPUT);
		possibleValues = new ArrayList<String>();
		possibleValues.add(PostInfarctVSD.URINE_LABEL_0);
		possibleValues.add(PostInfarctVSD.URINE_LABEL_1);
		possibleValues.add(PostInfarctVSD.URINE_LABEL_2);
		urineList = new JComboBox(new ComboBoxAdapter<String>(possibleValues, urineModel));
		builder.append("Urine Output:", urineList);

		ValueModel bunModel = postInfarctPresModel
				.getModel(PostInfarctVSD.PROPERTYNAME_BUN);
		bunField = ComponentFactory.createFormattedTextField(bunModel, NumberFormat
				.getNumberInstance());
		ValidationComponentUtils.setMessageKey(bunField,
				"postInfarctVSD.Blood Urea Nitrogen");

		builder.append("Blood urea nitrogen:", bunField);

		ValueModel pvdModel = postInfarctPresModel
				.getModel(PostInfarctVSD.PROPERTYNAME_PVD);
		pvdBox = ComponentFactory.createCheckBox(pvdModel, "HX PVD");
		builder.append(pvdBox);
		builder.nextRow();

		ValueModel opDateModel = postInfarctPresModel
				.getModel(PostInfarctVSD.PROPERTYNAME_OPDATE);
		opDateField = ComponentFactory.createDateField(opDateModel);
		ValidationComponentUtils.setMessageKey(opDateField,
				"postInfarctVSD.Operation Date");
		ValidationComponentUtils.setMandatory(opDateField, true);
		builder.append("Operation Date: [mm/dd/yyyy]", opDateField);
		ValueModel colorModel = postInfarctPresModel.getModel(PostInfarctVSD.PROPERTYNAME_PATIENT_COLOR);
		colorList = new JComboBox(new ComboBoxAdapter<String>(colorValues, colorModel));		

		addGraphProperties(true);

		// The builder holds the layout container that we now return.
		postInVSDPanel = builder.getPanel();

		updateComponentTreeMandatoryAndSeverity(postInfarctPresModel
				.getValidationResultModel().getResult());
		// The builder holds the layout container that we now return.
		initEventHandling();
		return new IconFeedbackPanel(postInfarctPresModel
				.getValidationResultModel(), postInVSDPanel);

	}

	@Override
	protected void initEventHandling() {
		postInfarctPresModel.getValidationResultModel().addPropertyChangeListener(
				ValidationResultModel.PROPERTYNAME_RESULT,
				new ValidationChangeHandler());
	}

	@Override
	protected void updateComponentTreeMandatoryAndSeverity(ValidationResult result) {
		log.debug("UpdateCompTree: " + result);
		log.debug("keys contain:" + result.keyMap().keySet());
		ValidationComponentUtils
				.updateComponentTreeMandatoryAndBlankBackground(postInVSDPanel);
		ValidationComponentUtils.updateComponentTreeSeverityBackground(
				postInVSDPanel, result);
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
