package patientModelSet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import patient.PatientPresentationModel;
import util.ComponentFactory;
import util.IconFeedbackPanel;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class PatientModelSetForm {
	private static Logger log = Logger.getLogger(PatientModelSetForm.class);

	// -------- Graphform ------------------

	// We need to know who owns us, so we can populate the
	// output window once it's been created. Set the content pane to
	// null, so we only assign the graph to it once.
	protected static JFrame parent;

	protected PatientPresentationModel adapter;

	protected Container contentPane = null;

	protected PatientModelSet dModel;

	private JTextField graphTitleField;

	private PatientModelSetPresentationModel patientSetPresenter;

	private ArrayList<String> possibleValues;

	private JComboBox curveModelList;

	private JPanel patientSetPanel;

	// private JComboBox legendLocationList;

	/**
	 * EntryForm constructor
	 * 
	 * Initialize the class and creates actions for use in menus.
	 */
	public PatientModelSetForm() {

	}

	/**
	 * Builds the panel. Initializes and configures components first, then creates
	 * a FormLayout, configures the layout, creates a builder, sets a border, and
	 * finally adds the components.
	 * 
	 * @return the built panel
	 */
	
	public JComponent buildPanel(PatientModelSet dataModel) {
		dModel = dataModel;

		patientSetPresenter = new PatientModelSetPresentationModel(dataModel);

		// Create a FormLayout instance on the given column and row specs.
		// For almost all forms you specify the columns; sometimes rows are
		// created dynamically. In this case the labels are right aligned.
		FormLayout layout = new FormLayout("max(40dlu;pref), 1dlu, max(40dlu;pref)"
		// 1st major column
				+ ",2dlu , max(40dlu;pref), 1dlu, max(40dlu;pref)"
		// 2nd major column
				, ""); // add rows dynamically

		// Specify that columns 1 & 5 as well as 3 & 7 have equal widths.
		// layout.setColumnGroups(new int[][]{{1, 5}, {3, 7}});

		// Create a builder that assists in adding components to the container.
		// Wrap the panel with a standardized border.
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		// Fill the grid with components; the builder offers to create
		// frequently used components, e.g. separators and labels.

		// Add a titled separator.
		// The builder holds the layout container that we now return.
		// Add a titled separator.
		builder.appendSeparator("Graph Options");

		// Then add fields.
		ValueModel graphTitleModel = patientSetPresenter
				.getModel("graphTitleString");
		graphTitleField = ComponentFactory.createTextField(graphTitleModel);
		ValidationComponentUtils.setMessageKey(graphTitleField,
				"PatientModelSet.Graph Title");
		builder.append("Graph Title:", graphTitleField, 5);

		// Choose a curve set.
		ValueModel curveModelModel = patientSetPresenter
				.getModel(PatientModelSet.PROPERTYNAME_CURVEMODEL);
		possibleValues = new ArrayList<String>();
		possibleValues.add("Survival");
		possibleValues.add("Hazard");
		curveModelList = new JComboBox(new ComboBoxAdapter<String>(possibleValues,
				curveModelModel));
		builder.append("Graph Model: ", curveModelList);
//		// post a legend.
//		ValueModel legendLocationModel = patientSetPresenter
//				.getModel(PatientModelSet.PROPERTYNAME_LEGENDLOCATION);
//		possibleValues = new ArrayList<String>();
//		possibleValues.add("None");
//		possibleValues.add("Lower Left");
//		possibleValues.add("Upper Right");
//		possibleValues.add("Lower Right");
//
//		// TODO Fix legending.
//		legendLocationList = new JComboBox(new ComboBoxAdapter(possibleValues,
//				legendLocationModel));
//
//		builder.append("Legend Location: ", legendLocationList);

		ValueModel graphTimeModel = patientSetPresenter.getModel("graphTime");
		JTextField graphTimeField = ComponentFactory.createFormattedTextField(
				graphTimeModel, NumberFormat.getNumberInstance());

		graphTimeField.setToolTipText("How long do you want the graph?");
		ValidationComponentUtils.setMessageKey(graphTimeField,
				"PatientModelSet.Graph Time");
		builder.append("Graph Time [years]:", graphTimeField);

		builder.nextLine();

		JButton refreshButton = new JButton("Refresh Graph");
		refreshButton.addActionListener(dModel.getRunAction());

		builder.append(refreshButton, 4);

		patientSetPanel = builder.getPanel();
		JPanel graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		// The entry pane is the view of the current patient, the patients control
		// panel Sand the menu.
		graphPanel.add(dModel.getUIPanel(), BorderLayout.CENTER);
		graphPanel.add(new IconFeedbackPanel(patientSetPresenter
				.getValidationResultModel(), patientSetPanel), BorderLayout.SOUTH);

		initEventHandling();

		return (graphPanel);

	}

	/**
	 * Updates the component background in the mandatory panel and the validation
	 * background in the severity panel. Invoked whenever the observed validation
	 * result changes.
	 */
	public final class ValidationChangeHandler implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			log.debug("propertyChanged");
			updateComponentTreeMandatoryAndSeverity((ValidationResult) evt
					.getNewValue());
		}
	}

	protected void initEventHandling() {
		patientSetPresenter.getValidationResultModel().addPropertyChangeListener(
				ValidationResultModel.PROPERTYNAME_RESULT,
				new ValidationChangeHandler());
	}

	protected void updateComponentTreeMandatoryAndSeverity(ValidationResult result) {
		log.debug("UpdateCompTree: " + result);
		log.debug("keys contain:" + result.keyMap().keySet());

		ValidationComponentUtils
				.updateComponentTreeMandatoryAndBlankBackground(patientSetPanel);
		ValidationComponentUtils.updateComponentTreeSeverityBackground(
				patientSetPanel, result);
	}

	public void setParent(JFrame parent) {
		log.debug("Set parent ");
		PatientModelSetForm.parent = parent;
	}

}