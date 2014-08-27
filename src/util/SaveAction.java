package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import support.ExceptionHandler;

public class SaveAction extends BaseAction {
	private static final String PATH = "path";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1189910365441825289L;

	/**
	 * Preferences hold the sizing/directory preferences of the application.
	 */
	private final Preferences _prefs = Preferences.userNodeForPackage(this.getClass());

	private JFileChooser fileSelBox;

	private final Logger log = Logger.getLogger(SaveAction.class);

	private JFrame parent;

	protected Object savedObject;

	public SaveAction(final JFrame parent, final Object savedObject) {
		super("Save Patient As", new ImageIcon("images/actions/document-save.png"),
				"Save Patient As", new Integer(KeyEvent.VK_S), KeyStroke.getKeyStroke(
						KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		this.parent = parent;
		setSavedObject(savedObject);
	}

	public void actionPerformed(final ActionEvent e) {
		log.debug("Saving DATA");

		// Get the text from the responseText field
		File file = null;
		FileOutputStream outFile = null;

		// Test the preferences path to see if it exists.
		final String prefPath = _prefs.get(PATH, path);
		file = new File(prefPath);
		if (file.isDirectory()) {
			path = prefPath;
		}

		fileSelBox = new JFileChooser(path);

		// Change the title and button on the file selection box.
		fileSelBox.setDialogType(JFileChooser.SAVE_DIALOG);
		fileSelBox.setApproveButtonText("Save");

		log.debug("Save location starts at : " + fileSelBox.getCurrentDirectory());

		// Nothing in the outputFilename field, open a dialog
		// box to prompt for a save to location.
		final int returnVal = fileSelBox.showSaveDialog(parent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileSelBox.getSelectedFile();

			final File currentDir = fileSelBox.getCurrentDirectory();

			System.setProperty("user.dir", currentDir.toString());
			log.debug("Current System directory: " + System.getProperty("user.dir"));
			_prefs.put(PATH, currentDir.toString());

			try {
				outFile = new FileOutputStream(file);
				log.debug("File: " + file);
				writeData(outFile);
				outFile.close();

			} catch (final IOException ex) {
				synchronized (this) {
					ExceptionHandler.logger(ex, log);
				}
				return;
			}
			log.debug("file");
		}

	}

	public void setSavedObject(final Object savedObject) {
		final Object oldObject = this.savedObject;
		this.savedObject = savedObject;
		firePropertyChange("savedObject", oldObject, savedObject);
	}

	/**
	 * @param outFile
	 */
	protected void writeData(final FileOutputStream outFile) {
		log.debug("saving:" + savedObject.toString());
		final XMLEncoder encode = new XMLEncoder(new BufferedOutputStream(outFile));
		encode.writeObject(savedObject);
		encode.close();
	}

}
