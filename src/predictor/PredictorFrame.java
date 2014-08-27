/**
 * 
 */
package predictor;

import graph.GraphModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.util.prefs.Preferences;

import javax.print.attribute.standard.OrientationRequested;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import patientModelSet.PatientModelSet;
import patientModelSet.PatientModelSetForm;
import patientModelSet.PatientModelSet.ClearPatientSetAction;
import patientModelSet.PatientModelSet.CopyPatientAction;
import patientModelSet.PatientModelSet.NewDefaultPatientAction;
import patientModelSet.PatientModelSet.RemovePatientAction;
import patientModelSet.PatientModelSet.SavePatientAction;
import patientModelSet.PatientModelSet.SavePatientSetAction;
import support.Property;
import support.WriteSupportAction;
import util.AboutAction;
import util.ExitAction;
import util.HelpAction;
import util.OpenAction;
import util.PrintAction;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

/**
 * @author ehrlinger
 * 
 */
public class PredictorFrame extends JFrame implements Printable {

	private static Logger log = Logger.getLogger(PredictorFrame.class);

	private static final String PATH = "path";

	private static final String programName = "predictor";

	private static final String SCREEN_HEIGHT = "screen_height";

	private static final String SCREEN_WIDTH = "screen_width";

	private static final String DefaultModel = "DefaultModel";

	/**
	 * 
	 */
	private static final long serialVersionUID = 118588600202872732L;

	// Preference keys for this package
	private static final String X_LOCATION = "x_location";

	private static final String Y_LOCATION = "y_location";

	/**
	 * Function to disable double buffering for a component so print quality is
	 * improved.
	 * 
	 * @param c
	 *          Component
	 */
	protected static void disableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	/**
	 * Function to enable double buffering for a component after we're done
	 * printing, so screen paints are fast.
	 * 
	 * @param c
	 *          Component
	 */
	protected static void enableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}

	/**
	 * Preferences hold the sizing preferences of the application.
	 */
	private Preferences _prefs = Preferences.userNodeForPackage(this.getClass());

	private String helpPath = "http://hviresearch.ccf.org/wiki/index.php/JPredictor";

	private String path = System.getProperty("user.dir") + "/Patients";

	private JSplitPane splitPane;

	/**
	 * JToolBar toolbar;
	 */
	private JToolBar toolbar;

	// private AddAction addAction;
	/**
	 * Constructor
	 */
	public PredictorFrame(String classload) {

		log.debug("PredictorFrame: " + classload);
		setDefaultLookAndFeelDecorated(true);
		_prefs.put(DefaultModel, classload);
		// Setting screen size

		// Lets get the frames preferences for screen size.
		// Then we should create a resize/location listener
		// sot store the location/size on change.
		log.debug("Loading preferences");

		// Location/scale preferences.
		int xLoc = _prefs.getInt(X_LOCATION, 20);
		int yLoc = _prefs.getInt(Y_LOCATION, 20);
		int sWidth = _prefs.getInt(SCREEN_WIDTH, Property.getScreenWidth());
		int sHeight = _prefs.getInt(SCREEN_HEIGHT, Property.getScreenHeight());

		// Set the minimum size. This way users can't totally loose the application.
		if (sWidth < 500)
			sWidth = 500;
		if (sHeight < 300)
			sHeight = 300;

		String prefPath = _prefs.get(PATH, path);

		// Test for path existance.
		File dirPath = new File(prefPath);
		if (!dirPath.isDirectory()) {
			log.debug("Path not found: " + prefPath);
			dirPath = new File(path);
			if (!dirPath.isDirectory()) {
				log.debug("Path not found: " + path);
				path = System.getProperty("user.dir");
			}
		} else {
			path = prefPath;
		}
		System.setProperty("user.dir", path);
		storePreferences();

		log.debug("Path: " + path);

		// Setting screen size
		setBounds(xLoc, yLoc, sWidth, sHeight);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ToolTipManager mgr = ToolTipManager.sharedInstance();
		// Setting the tooltip dissmiss delay to maximum
		// value of int
		mgr.setDismissDelay(Integer.MAX_VALUE);
		mgr.setInitialDelay(250);
		// Override the ToolTip.foreground color in Swing's defaults table.
		UIManager.put("ToolTip.foreground", Color.black);

		// Create the background color.
		Color tipColor = new Color(0xFF, 0xFF, 0xCC);
		// Override the ToolTip.background color in Swing's defaults table.
		UIManager.put("ToolTip.background", tipColor);
		// Make a bit of a larger border around the tip.
		UIManager.put("ToolTip.border", BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.black, 2), BorderFactory
						.createLineBorder(tipColor, 10)));

		// ------------------------------------------------------------------
		// Create the application components

		// The Graph panel for the Graphs...
		GraphModel grapher = new GraphModel();

		// A set of patients of type classload, that plot to the grapher
		PatientModelSet patientSet = new PatientModelSet(classload);
		patientSet.setGraphics(grapher);

		// Tell the patientSet how long the default graph should be.
		patientSet.setGraphTime(20.0);
		patientSet.setParentFrame(this);

		// The patientSet has a form that we put our data into.
		PatientModelSetForm patientSetForm = new PatientModelSetForm();
		// ------------------------------------------------------------------
		// Create application Actions.

		// A file menu for manipulating the complete patientSet.
		JMenu fileMenu;

		// The patientModelSet is serializable... storing every patient in the set.
		// We may want a way to export a single patient model
		SavePatientSetAction saveAction = patientSet.getSavePatientSetAction();
		saveAction.setPath(path);

		// We open complete a PatientModelSet also, this will also open a single
		// patient if we select that file. We add single patients to the current
		// set... we open sets by clearing the old set first.
		OpenAction openAction = (OpenAction) patientSet.getOpenPatientSetAction();
		openAction.setPath(path);

		// The newAction should clear the PatientModelSet of all patients.
		ClearPatientSetAction newAction = patientSet.getClearPatientSetAction();

		// Write is the method used to mail support issues.
		WriteSupportAction writeAction = new WriteSupportAction(this);

		// Printing is interesting because we can print the visible window, or just
		// the graph. This action will print the window.
		PrintAction printAction = new PrintAction(this);

		// Landscape... of course.
		printAction.setOrientation(OrientationRequested.LANDSCAPE);

		// ------------------------------------------------------------------------
		// The place a patient menu actions.

		// new patient
		NewDefaultPatientAction newPatientAction = patientSet.getNewPatientAction();
		CopyPatientAction copyPatientAction = patientSet.getCopyPatientAction();
		SavePatientAction savePatientAction = patientSet.getSavePatientAction();
		RemovePatientAction removePatientAction = patientSet
				.getRemovePatientAction();

		// ------------------------------------------------------------------------
		// Making the Menu Bar and Menu Items
		JMenuBar mbar = new JMenuBar();
		mbar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

		// File menu
		setJMenuBar(mbar);
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		mbar.add(fileMenu);

		fileMenu.add(newAction);
		fileMenu.add(openAction);
		fileMenu.add(saveAction);

		fileMenu.addSeparator();
		fileMenu.add(printAction);

		fileMenu.addSeparator();
		ExitAction exitAction = new ExitAction();
		fileMenu.add(exitAction);

		// Patient menu for specific ta actions.
		JMenu actionMenu = new JMenu("Patient");
		actionMenu.setMnemonic(KeyEvent.VK_P);
		mbar.add(actionMenu);
		actionMenu.add(savePatientAction);
		actionMenu.addSeparator();
		actionMenu.add(newPatientAction);
		actionMenu.add(patientSet.newPatientMenu());
		actionMenu.add(copyPatientAction);
		actionMenu.add(removePatientAction);

		// -----------------------------------
		// Help menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		mbar.add(helpMenu);

		helpMenu.add(writeAction);
		helpMenu.addSeparator();

		HelpAction helpItem = new HelpAction(this);

		helpItem.setHelpPath(helpPath);
		helpMenu.add(helpItem);

		AboutAction aboutAction = new AboutAction(this, programName);

		helpMenu.add(aboutAction);

		// ------------------------------------------------------------------------
		// Adding toolbar for displaying shortcuts
		toolbar = patientSet.getPatientSetToolbar();

		toolbar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

		JButton printbutton = toolbar.add(printAction);
		printbutton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton writebutton = toolbar.add(writeAction);
		writebutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		writebutton.setToolTipText("Support");

		// toolbar.setFloatable(false);
		toolbar.setOrientation(JToolBar.HORIZONTAL);

		toolbar.addSeparator();
		toolbar.add(printbutton);
		toolbar.add(writebutton);

		toolbar.addSeparator();

		// ------------------------------------------------------------------------
		// Build the UI by populating the content pane.
		Container contentPane = getContentPane();
		contentPane.add(toolbar, BorderLayout.NORTH);

		// ----------------------------------------------------------------
		// Making the graphics area
		JScrollPane graphPane = new JScrollPane(grapher);
		graphPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// ----------------------------------------------------------------
		// Making Scrollable (if necessary) patient entry area
		JScrollPane patientPane = new JScrollPane(patientSetForm
				.buildPanel(patientSet));

		saveAction.setSavedObject(patientSet);

		patientPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// contentPane.add(patientPane, BorderLayout.WEST);
		// ------------------------------------------------------------------------
		// Create a split pane so we can resize the graph/entry form
		// Create a split pane with the two scroll panes in it.
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, patientPane,
				graphPane);
		splitPane.setOneTouchExpandable(true);
		contentPane.add(splitPane, BorderLayout.CENTER);
		// ----------------------------------------------------------------
		// Add the auto store of the screen size.
		addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent e) {

			}

			public void componentMoved(ComponentEvent e) {

				storePreferences();
			}

			public void componentResized(ComponentEvent e) {
				storePreferences();
			}

			public void componentShown(ComponentEvent e) {

			}
		});
	}

	/**
	 * Implementing print function of interface Printable
	 * 
	 * @param g
	 *          Graphics
	 * @param pageFormat
	 *          PageFormat
	 * @param pageIndex
	 *          int
	 * @return success int
	 */
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		Graphics2D g2d = (Graphics2D) g;
		Component comp = this.getContentPane();
		// Component header;

		log.debug("PredictorFrame : print");
		// If we've printed all the pages... finish up.
		// or this will loop endlessly.
		log.debug("print Frame");
		comp.validate();

		if (pageIndex > 0) {
			return Printable.NO_SUCH_PAGE;
		}

		disableDoubleBuffering(comp);
		// We do this for the ORframe
		Dimension screenSize = comp.getSize();
		double scaleX = pageFormat.getImageableWidth() / screenSize.getWidth();
		double scaleY = pageFormat.getImageableHeight() / screenSize.getHeight();
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2d.scale(scaleX, scaleY);

		// This transform is supposed to avoid the partial printing error.
		// only occurs in landscape printing on windows... to HP printers.
		//
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4924298
		g2d.getTransform().scale(0.999999, 1.0);

		comp.paint(g2d);
		// Attempt to let the print catch up with the painting.
		comp.repaint();
		enableDoubleBuffering(comp);

		return Printable.PAGE_EXISTS;

	}

	/**
	 * storePreferences saves the current size of the window into the preferences,
	 * and the current working directory.
	 * 
	 */
	public void storePreferences() {
		_prefs.put(PATH, System.getProperty("user.dir"));

		Rectangle tBounds = this.getBounds();
		_prefs.putInt(X_LOCATION, tBounds.x);
		_prefs.putInt(Y_LOCATION, tBounds.y);
		_prefs.putInt(SCREEN_WIDTH, tBounds.width);
		_prefs.putInt(SCREEN_HEIGHT, tBounds.height);

	}

	/**
	 * update does a quick resize when we open a new patient.... if necessary.
	 * 
	 */
	public void update() {
		splitPane.resetToPreferredSizes();
	}
}
