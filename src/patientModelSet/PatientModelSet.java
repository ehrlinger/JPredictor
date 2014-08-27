package patientModelSet;

import graph.GraphModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import org.apache.log4j.Logger;

import patient.ModelExecuteException;
import patient.PatientModel;
import patient.PatientModelForm;
import predictor.PredictorFrame;
import support.ExceptionHandler;
import util.BaseAction;
import util.NewAction;
import util.OpenAction;
import util.RunAction;
import util.SaveAction;
import util.Updatable;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.validation.ValidationResultModel;

/**
 * ModelSet is a collection of data models. They are displayed in a tabbed form.
 * patient
 * 
 * @author ehrlinger
 * 
 */
public class PatientModelSet extends Model implements Updatable, Serializable {

	public class ClearPatientSetAction extends BaseAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2160334205129863430L;

		PatientModelSet parent;

		public ClearPatientSetAction(PatientModelSet updateObject) {
			super("Clear Patient Set", new ImageIcon(
					"images/actions/document-new.png"), "Clear Patient Set", new Integer(
					KeyEvent.VK_C), KeyStroke.getKeyStroke(KeyEvent.VK_C,
					ActionEvent.CTRL_MASK));
			log = Logger.getLogger(ClearPatientSetAction.class);
			log.debug("ClearPatientSetAction instance");
			// We want to change the tooltip text to "Import Patient"
			this.putValue(Action.SHORT_DESCRIPTION,
					"Clear all patients from this set.");

			parent = updateObject;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see patientModelSet.ActionClearPatientSet#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			parent.removeAllTabs();

			graph.resetLegend();
		}

	}

	/**
	 * Tells the application how to export a single patient.
	 */
	public class CopyPatientAction extends BaseAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 9118174355367310187L;

		public CopyPatientAction() {
			super("Duplicate This Patient", new ImageIcon(
					"images/actions/contact-copy.png"), "Duplicate This Patient",
					new Integer(KeyEvent.VK_D), KeyStroke.getKeyStroke(KeyEvent.VK_D,
							ActionEvent.CTRL_MASK));
			log = Logger.getLogger(CopyPatientAction.class);
			this.putValue(Action.SHORT_DESCRIPTION,
					"Create a duplicate of this patient");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see patientModelSet.ActionCopyPatient#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			log.debug("Duplicate Patient Tab DATA");
			if (tabbedPane.getModel().isSelected()) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				patient = patientSet.elementAt(selectedIndex).clone();
				addTab(patient);
			}
		}

	}

	/**
	 * Tells the application how to export a single patient.
	 */
	public class NewDefaultPatientAction extends NewAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8737492093575268717L;

		public NewDefaultPatientAction(PatientModelSet pat) {
			super(new ImageIcon("images/actions/contact-new.png"), pat);
			log.debug("PatientSetNewAction instance");
			this.putValue(Action.NAME, "New default patient");
			this.putValue(Action.SHORT_DESCRIPTION,
					"Add a new, empty default patient type");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see patientModelSet.ActionNewPatient#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event) {
			addTab();

		}

	}

	/**
	 * Tells the application how to export a single patient.
	 */
	protected class NewPatientAction extends NewAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8737492093575268717L;

		private String patientModelName;

		public NewPatientAction(PatientModelSet patSet, String patient) {
			super(new ImageIcon("images/actions/contact-new.png"), patSet);
			log.debug("PatientSetNewAction instance:" + patient);
			
			this.putValue(Action.SHORT_DESCRIPTION, "Add a new empty patient");
			String 
			patientType = patient.substring(patient.lastIndexOf(".") + 1,
					patient.length());
			this.putValue(Action.NAME, "New " + patientType + " patient");
			this.putValue(Action.SHORT_DESCRIPTION, "Add an empty " + patientType
					+ " patient");
			this.putValue(Action.MNEMONIC_KEY, null);
			this.putValue(Action.ACCELERATOR_KEY, null);

			this.patientModelName = patient;
		}

		
		public void actionPerformed(ActionEvent event) {
			try {
				final PatientModel patient = (PatientModel) Class.forName(
						patientModelName).newInstance();
				addTab(patient);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Tells the application how to import a single patient or a patient set. We
	 * can actually use the OpenAction update mechanism. We only subclass so we
	 * can change the tooltip text to reflect the import into a set of patients.
	 */
	protected class OpenPatientSetAction extends OpenAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2160334205129863430L;

		public OpenPatientSetAction(JFrame parent, Updatable updateObject) {
			super(parent, updateObject);
			log.debug("PatientSetOpenAction instance");
			// We want to change the tooltip text to "Import Patient"
			this.putValue(Action.SHORT_DESCRIPTION,
					"Open patient or patient set from saved file");
			this.putValue(Action.NAME, "Open a patient or patient set");

		}

	}

	/**
	 * Tells the application how to remove a single patient.
	 */
	public class RemovePatientAction extends BaseAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4728094700329665114L;

		public RemovePatientAction() {
			super("Remove This Patient", new ImageIcon(
					"images/actions/contact-delete.png"), "Remove this patient tab",
					new Integer(KeyEvent.VK_X), KeyStroke.getKeyStroke(KeyEvent.VK_X,
							ActionEvent.CTRL_MASK));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see patientModelSet.ActionRemovePatient#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {

			removeSelectedTab();

			// We don't want to be able to remove the last pane.... so disable
			// this.
			log.debug("Component Count" + tabbedPane.getComponentCount());
			if (tabbedPane.getComponentCount() == 0) {
				this.setEnabled(false);
			}
		}
	}

	// ---------------------------------------------------------------------------------
	// Action methods - Tell the application how to modify the patient set
	// ---------------------------------------------------------------------------------
	/**
	 * Tells the application how to execute a set of patients.
	 */
	protected class RunPatientSetAction extends RunAction {

		private static final long serialVersionUID = 5962025145283633868L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// What does the patient set do when we run for the first time?
			// Loop through all Patients and have them run (if necessary maybe?)
			log.debug("PatientSetRunAction performed.");
			refreshPatientSet();
		}

	}

	/**
	 * Tells the application how to export a single patient.
	 */
	public class SavePatientAction extends SaveAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 9118174355367310187L;

		public SavePatientAction(JFrame parent, Updatable updateObject) {
			super(parent, updateObject);
			log.debug("PatientSetSaveAction instance");
			this.putValue(Action.SHORT_DESCRIPTION, "Export patient to file (save)");
			this.putValue(Action.SMALL_ICON, new ImageIcon(
					"images/actions/contact-save.png"));
		}

		public void actionPerformed(ActionEvent e) {
			log.debug("Exporting Patient Tab DATA");
			if (tabbedPane.getModel().isSelected()) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				super.setSavedObject(patientSet.elementAt(selectedIndex));
			}
			super.actionPerformed(e);
		}

	}

	/**
	 * Tells the application how to Save the complete set..
	 */
	public class SavePatientSetAction extends SaveAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 9118174355367310187L;

		private Logger log = Logger.getLogger(SavePatientSetAction.class);

		public SavePatientSetAction(JFrame parent, PatientModelSet updateObject) {
			super(parent, updateObject);
			this.putValue(Action.NAME, "Save this patient set.");
			log.debug("PatientSetSaveAction instance");
			this.putValue(Action.SHORT_DESCRIPTION, "Save this patient set");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see patientModelSet.ActionSavePatientSet#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			log.debug("Save this patient set.");
			super.actionPerformed(e);
		}
	}

	private static final String PATH = "path";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3114447386923365951L;

	public static final String PROPERTYNAME_CURVEMODEL = "curveModel";

	public static final String PROPERTYNAME_GRAPHTIME = "graphTime";

	public static final String PROPERTYNAME_LEGENDLOCATION = "legendLocation";

	public static final String PROPERTYNAME_GRAPH_TITLE_STRING = "graphTitleString";

	/**
	 * Preferences hold the sizing preferences of the application.
	 */
	private Preferences _prefs = Preferences.userNodeForPackage(this.getClass());

	private int addedTabCount = 0;

	private String classload;

	private PatientModelForm entry;

	private Class<PatientModelForm> form;

	private GraphModel graph = null;

	private String curveModel;

	private double graphTime;

	protected Logger log = Logger.getLogger(PatientModelSet.class);

	private PatientModel model;

	private JMenu newMenu = null;

	private JScrollPane pane;

	private JComponent panelForm;

	private PredictorFrame parentFrame;

	private String path = System.getProperty("user.dir") + "/Patients";

	// First turn the classload string into a dmodel class.
	private PatientModel patient;

	private Vector<PatientModel> patientSet = new Vector<PatientModel>();

	protected RemovePatientAction rmAction = null;

	protected JTabbedPane tabbedPane = new JTabbedPane();

	private String graphTitleString;

	private JToolBar toolbar = null;

	private String legendLocation;

	private PatientModelSetPresentationModel presentModel;

	public PatientModelSet() {
		this("transplantSupport.TransplantSupport");
	}

	public PatientModelSet(String classload) {
		rmAction = new RemovePatientAction();
		curveModel = "Survival";
		legendLocation = "None";
		if (classload == null)
			this.classload = "transplantSupport.TransplantSupport";
		else
			this.classload = classload;
		setupPropertyChangeListener(new PatientModelSetPresentationModel(this));
	}

	protected void setupPropertyChangeListener(
			PatientModelSetPresentationModel presenter) {
		presentModel = presenter;
		// Add a propertyChangeListener to run an
		addPropertyChangeListener(new PropertyChangeListener() {

			private ValidationResultModel valid;

			public void propertyChange(PropertyChangeEvent event) {
				log.debug("PropertyChanged:" + event.getPropertyName());
				valid = presentModel.getValidationResultModel();

				if (valid.hasErrors())
					return;

				// We have valid data..
				// update the graph...
				if (event.getPropertyName().equalsIgnoreCase(PROPERTYNAME_GRAPHTIME)) {
					log.debug("time: " + graphTime);

					// if we have patients, and we've changed the time scale, we want to
					// loop through them to make sure they've been executed.
					for (PatientModel pat : patientSet) {
						try {
							pat.execute();
						} catch (ModelExecuteException ex) {
							ExceptionHandler.logger(ex, log);
						}
					}
				}
			}
		});
	}

	public PatientModelSet(String classload, GraphModel graph) {
		this(classload);
		log.debug("PatientModelSet: " + classload);
		this.graph = graph;
		this.classload = classload;

	}

	/**
	 * Create a JLabel and add it to the tabbed pane. The text of the label
	 * matches the tab's text, and is simply the number of tabs created during
	 * this run of the application.
	 * <p>
	 * 
	 * @return The index of the newly added tab.
	 */
	private void addTab() {
		log.debug("Adding tab default tab");
		try {
			patient = (PatientModel) Class.forName(model.getClass().getName())
					.newInstance();
		} catch (InstantiationException e) {
			ExceptionHandler.logger(e, log);

		} catch (IllegalAccessException e) {
			ExceptionHandler.logger(e, log);
		} catch (ClassNotFoundException e) {
			ExceptionHandler.logger(e, log);
		}

		addTab(patient);
	}


	private int addTab(PatientModel patient) {
		// TODO If the default patient that was opened on App initialization is
		// NOT modified, we want to remove it. How do we detect that?
		String name = patient.getPatientName();
		log.debug("Adding tab " + addedTabCount + ": " + name + "Building Panel");

		String classload = patient.getClass().getName();
		try {
			form = (Class<PatientModelForm>) Class.forName(classload + "Form");
			entry = form.newInstance();
			patientSet.add(patient);
			graph.addDataset(patient);
			patient.setGraph(graph);
			patient.setModelSet(this);

		} catch (ClassNotFoundException ex) {
			ExceptionHandler.logger(ex, log);
		} catch (InstantiationException e) {

			ExceptionHandler.logger(e, log);
		} catch (IllegalAccessException e) {
			ExceptionHandler.logger(e, log);
		}

		panelForm = entry.buildPanel(patient);
		pane = new JScrollPane(panelForm);
		pane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		tabbedPane.addTab(classload.substring(classload.lastIndexOf(".") + 1,
				classload.length())
				+ "("
				+ patient.getPatientColor()
				+ "):"
				+ name
				+ " "
				+ patient.getPatientNote(), pane);
		log.debug("AddTabe: " + tabbedPane.getComponentCount());
		tabbedPane.setSelectedIndex(tabbedPane.getComponentCount() - 1);
		tabbedPane.revalidate();
		//
		// Bump the number of tabs added.
		//
		addedTabCount += 1;

		rmAction.setEnabled(tabbedPane.getComponentCount() > 0);

		if (parentFrame != null)
			parentFrame.update();

		return (tabbedPane.getTabCount() - 1);
	}

	public void changeTabTitle(PatientModel comp, String tabTitle) {
		int index = 0;

		for (int i = 0; i < patientSet.size(); i++) {
			if (patientSet.elementAt(i).equals(comp)) {
				index = i;
				break;
			}
		}

		tabbedPane.setTitleAt(index, tabTitle);

	}

	public String getClassload() {
		return classload;
	}

	public ClearPatientSetAction getClearPatientSetAction() {
		return new ClearPatientSetAction(this);
	}

	public CopyPatientAction getCopyPatientAction() {
		return new CopyPatientAction();
	}

	public double getGraphTime() {
		return graphTime;
	}

	public NewDefaultPatientAction getNewPatientAction() {
		return new NewDefaultPatientAction(this);
	}

	public OpenAction getOpenPatientSetAction() {

		return new OpenPatientSetAction(parentFrame, this);
	}

	// ---------------------------------------------------------------------------------
	// Action Get methods
	// ---------------------------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public Vector<PatientModel> getPatientSet() {
		return patientSet;
	}

	/**
	 * The toolbar is for patient specific controls.
	 * 
	 * @return
	 */
	public JToolBar getPatientSetToolbar() {
		if (toolbar == null) {

			// Adding toolbar for displaying shortcuts
			toolbar = new JToolBar();

			// Start with the Patient Set Actions... clear/open/save
			JButton clearButton = toolbar.add(new ClearPatientSetAction(this));
			clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);

			JButton openButton = toolbar.add(new OpenPatientSetAction(parentFrame,
					this));
			openButton.setAlignmentX(Component.CENTER_ALIGNMENT);

			JButton saveButton = toolbar.add(new SavePatientSetAction(parentFrame,
					this));
			saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

			toolbar.addSeparator();

			// The the patient actions new/remove/clone/export/import
			JButton newButton = toolbar.add(new NewDefaultPatientAction(this));
			newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			// Component newPatientTypeMenu = toolbar.add(newMenu);
			// newPatientTypeMenu.setAlignmentX(Component.CENTER_ALIGNMENT);

			JButton copyButton = toolbar.add(new CopyPatientAction());
			copyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

			JButton exportButton = toolbar.add(new SavePatientAction(parentFrame,
					this));
			exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);

			rmAction = new RemovePatientAction();

			// We want the enabled action to be false unless there is more than
			// one
			// object
			rmAction.setEnabled(false);
			JButton removeButton = toolbar.add(rmAction);
			removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return (toolbar);
	}

	public RemovePatientAction getRemovePatientAction() {
		return rmAction;
	}

	/**
	 * The applications runAction will run through all the patients in the
	 * ModelSet.
	 * 
	 * @return PatientSetRunAction
	 */
	public RunAction getRunAction() {
		return new RunPatientSetAction();
	}

	public SavePatientAction getSavePatientAction() {
		return new SavePatientAction(parentFrame, this);
	}

	public SavePatientSetAction getSavePatientSetAction() {
		return new SavePatientSetAction(parentFrame, this);
	}

	public String getGraphTitleString() {
		return graphTitleString;
	}

	/**
	 * 
	 * @return
	 */
	
	public JPanel getUIPanel() {
		// ------------------------------------------------------------------
		// Create the application components
		//
		// We start with a blank form corresponding to either the last type, or
		// default type.
		try {
			log.debug("Loading class:" + classload);

			model = (PatientModel) Class.forName(classload).newInstance();

			patient = model;

			form = (Class<PatientModelForm>) Class.forName(classload + "Form");
			entry = form.newInstance();

		} catch (ClassNotFoundException e1) {

			log.error("Tried to load an unknown model class");
			ExceptionHandler.logger(e1, log);
		} catch (InstantiationException e) {
			ExceptionHandler.logger(e, log);

		} catch (IllegalAccessException e) {
			ExceptionHandler.logger(e, log);
		}

		log.debug("Path: " + path);

		// create the patient panel
		panelForm = entry.buildPanel(patient);
		pane = new JScrollPane(panelForm);
		pane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// If we want a default empty patient...
		// addTab();

		JPanel entryPane = new JPanel();

		entryPane.setLayout(new BorderLayout());
		// The entry pane is the view of the current patient, the patients
		// control
		// panel Sand the menu.
		entryPane.add(tabbedPane, BorderLayout.CENTER);

		return (entryPane);
	}

	public JMenu newPatientMenu() {
		if (newMenu == null) {
			newMenu = new JMenu("New Patients");
			newMenu.add(new NewPatientAction(this, "lvadSupport.LVADSupport"));
			newMenu.add(new NewPatientAction(this, "cabg_pci.CABG_pci"));
			newMenu.add(new NewPatientAction(this, "cardiomyopathy.Cardiomyopathy"));
			newMenu.add(new NewPatientAction(this,
					"transplantSupport.TransplantSupport"));
			newMenu.add(new NewPatientAction(this, "picabg.PICABG"));
			newMenu.add(new NewPatientAction(this, "postInfarctVSD.PostInfarctVSD"));
		}
		return (newMenu);

	}

	/**
	 * Remove all tabs from the tabbed pane.
	 */
	protected void removeAllTabs() {
		for (PatientModel pats : patientSet) {
			pats.removeFromGraph();
		}
		patientSet.clear();
		tabbedPane.removeAll();
		addedTabCount = 1;
		rmAction.setEnabled(tabbedPane.getComponentCount() > 0);

	}

	/**
	 * Remove the currently selected tab from the tabbed pane.
	 */
	private void removeSelectedTab() {

		// Do we want to post an "Are you sure?" dialog?
		if (tabbedPane.getModel().isSelected()) {
			int selectedIndex = tabbedPane.getSelectedIndex();
			// custom title, warning icon
			int returnValue = JOptionPane
					.showConfirmDialog(
							parentFrame, // Option type
							"Are you sure you want to remove this patient and loose any changes?",
							"Remove Patient Warning", JOptionPane.YES_NO_OPTION);
			if (returnValue == JOptionPane.YES_OPTION) {
				patientSet.elementAt(selectedIndex).removeFromGraph();
				patientSet.removeElementAt(selectedIndex);
				tabbedPane.removeTabAt(selectedIndex);
				tabbedPane.revalidate();
				parentFrame.update();
			}
		}
		rmAction.setEnabled(tabbedPane.getComponentCount() > 0);

	}

	public void setClassload(String classload) {
		this.classload = classload;
	}

	public void setGraphics(GraphModel grapher) {
		addPropertyChangeListener(grapher);
		graph = grapher;
	}

	public void setGraphTime(double graphTime) {
		log.debug("setGraphTime: " + graphTime + " : " + this.graphTime);

		double old = this.graphTime;
		this.graphTime = graphTime;
		firePropertyChange(PROPERTYNAME_GRAPHTIME, old, graphTime);

	}

	/**
	 * setParentFrame We need to know the parent frame so we can open dialog boxes
	 * for file manipulations
	 * 
	 * @param par
	 */
	public void setParentFrame(PredictorFrame par) {
		this.parentFrame = par;
	}

	public void setPatientSet(Vector<PatientModel> patientSet) {
		this.patientSet = patientSet;
	}

	public void setGraphTitleString(String titleString) {
		String old = this.graphTitleString;
		this.graphTitleString = titleString;

		firePropertyChange(PROPERTYNAME_GRAPH_TITLE_STRING, old, titleString);
	}

	/**
	 * storePreferences saves the current size of the window into the preferences,
	 * and the current working directory.
	 * 
	 */
	public void storePreferences() {
		_prefs.put(PATH, System.getProperty("user.dir"));
	}

	public void refreshPatientSet() {
		String tempName;
		for (PatientModel pats : patientSet) {
			tempName = pats.getPatientName();
			pats.setPatientName(tempName + "TEMP");
			pats.setPatientName(tempName);
		}
	}

	/**
	 * the update method is used by the openAction. OpenAction sends an update
	 * with the object selected to e opened. This method says what to do with that
	 * object.
	 * 
	 * @see util.Updatable#update(java.lang.Object)
	 */
	
	public void update(Object obs) {
		// Got an update. What do we do now?
		log.debug("Update Request with type " + obs.getClass().getName());

		// If we tried to import a patient model, obs == PatientModel, add it to
		// the
		// interface.
		if (obs instanceof PatientModelSet) {
			// If obs is a PatientModelSet then we need to remove all tabs, and
			// recreate the save set for ruther editing.
			PatientModelSet pSet = (PatientModelSet) obs;
			Vector<PatientModel> pModelSet = pSet.getPatientSet();
			tabbedPane.removeAll();

			// now update the PatientModelSet attributes.
			setGraphTime(pSet.getGraphTime());
			setGraphTitleString(pSet.getGraphTitleString());
			setLegendLocation(pSet.getLegendLocation());
			setCurveModel(pSet.getCurveModel());

			try {
				for (PatientModel pats : pModelSet) {
					addTab(pats);
					pats.execute();
				}
			} catch (ModelExecuteException e) {
				ExceptionHandler.logger(e, log);
			}
		}
		if (obs instanceof PatientModel) {
			log.debug("We're opening a PatientModel");
			PatientModel pModel = (PatientModel) obs;
			addTab(pModel);
			try {
				pModel.execute();
			} catch (ModelExecuteException e) {
				ExceptionHandler.logger(e, log);
			}
		}
	}

	public String getCurveModel() {
		return curveModel;
	}

	public void setCurveModel(String curveModel) {
		String oldModel = this.curveModel;
		this.curveModel = curveModel;
		firePropertyChange(PROPERTYNAME_CURVEMODEL, oldModel, curveModel);
	}

	public String getLegendLocation() {
		return legendLocation;
	}

	public void setLegendLocation(String legendLocation) {
		String oldModel = this.legendLocation;
		this.legendLocation = legendLocation;
		firePropertyChange(PROPERTYNAME_LEGENDLOCATION, oldModel, legendLocation);
	}

}
