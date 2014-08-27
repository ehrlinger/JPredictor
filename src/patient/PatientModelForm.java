package patient;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import util.ComponentFactory;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.view.ValidationComponentUtils;

public abstract class PatientModelForm {
	/**
	 * Updates the component background in the mandatory panel and the
	 * validation background in the severity panel. Invoked whenever the
	 * observed validation result changes.
	 */
	public final class ValidationChangeHandler implements
			PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			log.debug("propertyChanged");
			updateComponentTreeMandatoryAndSeverity((ValidationResult) evt
					.getNewValue());
		}
	}

	// -------- Graphform ------------------

	/**
	 * Logger log;
	 */
	private static Logger log = Logger.getLogger(PatientModelForm.class);

	protected static JFrame parent;

	protected PresentationModel<PatientModel> adapter;

	protected DefaultFormBuilder builder;

	protected JCheckBox confidenceLimitBox;

	private int columnCount;

	protected Container contentPane = null;

	protected FormLayout layout;

	protected JTextField patientIDField;

	protected PatientModel patientModel;

	protected JTextField patientNameField;

	private JTextField patientNoteField;
	protected ArrayList<String> colorValues;

	/**
	 * EntryForm constructor
	 * 
	 * Initialize the class and creates actions for use in menus.
	 */
	public PatientModelForm() {
		colorValues = new ArrayList<String>();
		colorValues.add("BLACK");
		colorValues.add("VERY_DARK_BLUE");
		colorValues.add("DARK_BLUE");
		colorValues.add("BLUE");
		colorValues.add("LIGHT_BLUE");
		colorValues.add("VERY_LIGHT_BLUE");
		colorValues.add("VERY_DARK_CYAN");
		colorValues.add("DARK_CYAN");
		colorValues.add("LIGHT_CYAN");
		colorValues.add("CYAN");
		colorValues.add("LIGHT_CYAN");
		colorValues.add("VERY_LIGHT_CYAN");
		colorValues.add("DARK_GRAY");
		colorValues.add("GRAY");
		colorValues.add("LIGHT_GRAY");
		colorValues.add("VERY_DARK_GREEN");
		colorValues.add("DARK_GREEN");
		colorValues.add("GREEN");
		colorValues.add("LIGHT_GREEN");
		colorValues.add("VERY_LIGHT_GREEN");
		colorValues.add("VERY_DARK_MAGENTA");
		colorValues.add("DARK_MAGENTA");
		colorValues.add("MAGENTA");
		colorValues.add("LIGHT_MAGENTA");
		colorValues.add("VERY_DARK_RED");
		colorValues.add("DARK_RED");
		colorValues.add("RED");
		colorValues.add("LIGHT_RED");
		colorValues.add("PINK");
		colorValues.add("ORANGE");
		colorValues.add("VERY_LIGHT_RED");
		colorValues.add("VERY_DARK_YELLOW");
		colorValues.add("DARK_YELLOW");
		colorValues.add("YELLOW");
		colorValues.add("LIGHT_YELLOW");
		colorValues.add("VERY_LIGHT_YELLOW");
		colorValues.add("WHITE");

	}

	/**
	 * Builds the panel. Initializes and configures components first, then
	 * creates a FormLayout, configures the layout, creates a builder, sets a
	 * border, and finally adds the components.
	 * 
	 * @return the built panel
	 */
	public JComponent buildPanel(PatientModel dataModel) {

		log.debug("Building PatientModelForm panel");
		try {
			patientModel = dataModel;
			adapter = new PresentationModel<PatientModel>(dataModel);
		} catch (NullPointerException ex) {
			log.debug("NULL POinter in presentation model....");
		}
		// Create a FormLayout instance on the given column and row specs.
		// For almost all forms you specify the columns; sometimes rows are
		// created dynamically. In this case the labels are right aligned.
		layout = new FormLayout("max(40dlu;pref), 1dlu, max(40dlu;pref)" // 1st
				// major
				// colum
				// + ",2dlu , max(40dlu;pref), 1dlu, max(40dlu;pref)" // 2nd
				// major
				// column
				, ""); // add rows dynamically

		// Specify that columns 1 & 5 as well as 3 & 7 have equal widths.
		// layout.setColumnGroups(new int[][]{{1, 5}, {3, 7}});

		// Create a builder that assists in adding components to the container.
		// Wrap the panel with a standardized border.
		builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		return null;
	}

	public void buildPatientDemographicsSection(
			PresentationModel<PatientModel> patientPresModel) {
		// Add a titled separator.
		builder.appendSeparator("Patient Demographics");

		// Patient Name.
		ValueModel patientNameModel = patientPresModel
				.getModel(PatientModel.PROPERTYNAME_PATIENT_NAME);
		patientNameField = ComponentFactory.createTextField(patientNameModel,
				false);
		patientNameField
				.setToolTipText("Name identifier placed in graph header.");
		ValidationComponentUtils.setMessageKey(patientNameField,
				"picabg.Patient Name");
		builder.append("Patient Name:", patientNameField);

		// Patient ID Number.
		ValueModel patientIDModel = patientPresModel
				.getModel(PatientModel.PROPERTYNAME_PATIENT_ID);
		patientIDField = ComponentFactory
				.createTextField(patientIDModel, false);
		ValidationComponentUtils.setMessageKey(patientIDField,
				"picabg.Patient ID");
		patientIDField
				.setToolTipText("Identifier number placed in graph header");
		builder.append("Patient Record Number:", patientIDField);

		// Patient Note.
		ValueModel patientNoteModel = patientPresModel
				.getModel(PatientModel.PROPERTYNAME_PATIENT_NOTE);
		patientNoteField = ComponentFactory.createTextField(patientNoteModel,
				false);
		patientNoteField
				.setToolTipText("Patient notes used for user reference only.");
		ValidationComponentUtils.setMessageKey(patientNoteField,
				"picabg.Patient Notes");
		if (columnCount > 1)
			builder.append("Patient Notes:", patientNoteField, 5);
		else
			builder.append("Patient Notes:", patientNoteField);
		// Race is a boolean, true/false.
		ValueModel confidenceModel = patientPresModel
				.getModel(PatientModel.PROPERTYNAME_CONFIDENCE);
		confidenceLimitBox = ComponentFactory.createCheckBox(confidenceModel,
				"Confidence Limits");
	}

	public void buildPatientDemographicsSection(
			PresentationModel<PatientModel> patientPresModel, int width) {
		columnCount = width;
		buildPatientDemographicsSection(patientPresModel);
	}

	protected abstract void initEventHandling();

	abstract protected void addGraphProperties(boolean color);

	public void setParent(JFrame parent) {
		log.debug("Set parent ");
		PatientModelForm.parent = parent;
		columnCount = 1;
	}

	protected abstract void updateComponentTreeMandatoryAndSeverity(
			ValidationResult result);
}
