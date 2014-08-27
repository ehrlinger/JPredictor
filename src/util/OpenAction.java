package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import support.ExceptionHandler;

public class OpenAction extends BaseAction {

	protected static final String PATH = "path";

	/**
	 * 
	 */
	private static final long serialVersionUID = -7160691815889010199L;

	/**
	 * Preferences hold the sizing/directory preferences of the application.
	 */
	private final Preferences _prefs = Preferences.userNodeForPackage(this.getClass());

	private File file = null;

	private JFileChooser fileSelBox;

	protected Logger log = Logger.getLogger(OpenAction.class);

	private JFrame parent;

	protected Updatable updateObject;

	public OpenAction(final JFrame parent, final Updatable updateObject) {
		super("Open Patient", new ImageIcon("images/actions/document-open.png"),
				"Open Patient", new Integer(KeyEvent.VK_O), KeyStroke.getKeyStroke(
						KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		this.parent = parent;
		this.updateObject = updateObject;

	}

	public void actionPerformed(final ActionEvent e) {
		log.debug("Reading DATA");

		// Get the text from the responseText field
		FileInputStream inFile = null;

		// Test the preferences path to see if it exists.
		final String prefPath = _prefs.get(PATH, path);
		file = new File(prefPath);
		if (file.isDirectory()) {
			path = prefPath;
		}

		fileSelBox = new JFileChooser(path);

		// Change the title and button on the file selection box.
		fileSelBox.setDialogType(JFileChooser.SAVE_DIALOG);
		fileSelBox.setApproveButtonText("Open");

		log.debug("Open location starts at : " + fileSelBox.getCurrentDirectory());

		// Nothing in the outputFilename field, open a dialog
		// box to prompt for a save to location.
		final int returnVal = fileSelBox.showOpenDialog(parent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileSelBox.getSelectedFile();

			final File currentDir = fileSelBox.getCurrentDirectory();

			System.setProperty("user.dir", currentDir.toString());
			log.debug("Current System directory: " + System.getProperty("user.dir"));
			_prefs.put(PATH, currentDir.toString());

			try {
				inFile = new FileInputStream(file);
				log.debug("File: " + file);
				readData(inFile);
				inFile.close();
			} catch (final IOException ex) {
				synchronized (this) {
					ExceptionHandler.logger(ex, log);
				}
				return;
			}
		}

	}

	/**
	 * @param inFile
	 */
	protected void readData(FileInputStream inFile) {

		// Mark the start of the input file, so we can rewind it on error.
		inFile.mark(0);

		XMLDecoder decode = new XMLDecoder(new BufferedInputStream(inFile));
		Object savedObject;
		try {
			savedObject = decode.readObject();
			log.debug(savedObject.toString());

		} catch (final NullPointerException ex) {
			// If we got a null pointer then we may need to rewrite an old object file
			// into a new one.

			log
					.debug("NullPointer, we're going to rewrite the file to another filename.");

			// Read the file into an output stream
			decode.close();
			inFile = updateClassDefinitionFile();
			decode = new XMLDecoder(new BufferedInputStream(inFile));
			savedObject = decode.readObject();
			log.debug(savedObject.toString());

		}
		updateObject.update(savedObject);
		decode.close();

	}

	/**
	 * In case we refactor our code, and rename a package or class...
	 * 
	 * Renaming classes breaks our persistance model. It's OK, in development, but
	 * as soon as you relase you must provide an upgrade path. So, we document
	 * changes here, open the old persistance file, modify the name, and overwrite
	 * the old one. The only downside is that it modifies the file timestamp.
	 * 
	 * @return FileInputStream to the rewritten persistance file so we can decode
	 *         that instead.
	 */
	protected FileInputStream updateClassDefinitionFile() {

		log.debug("Found a bad file object!");
		// We have the filename, so we need to read it into memory, modifying the
		// classname to something that exists...
		try {
			// We need to define the
			final BufferedReader inFile = new BufferedReader(new FileReader(file));

			// ...a buffer to contain the file contents.
			final StringBuffer contents = new StringBuffer();
			// use buffering, reading one line at a time
			String line = null; // not declared within while loop
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS the
			 * newline. it returns null only for the END of the stream. it returns an
			 * empty String if two newlines appear in a row.
			 */
			while ((line = inFile.readLine()) != null) {
				// We have a line, look for a string match.
				line = line.replace("TransplantSupport.TransplantSupport",
						"transplantSupport.TransplantSupport");
				line = line.replace("PostInfarctVSD.PostInfarctVSD",
						"postInfarctVSD.PostInfarctVSD");
				log.debug(line);
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}

			// We've done the read... close the file.
			inFile.close();

			// Now open it for writeing.
			// declared here only to make visible to finally clause; generic reference
			Writer output = null;
			try {
				// use buffering
				// FileWriter always assumes default encoding is OK!
				output = new BufferedWriter(new FileWriter(file));
				output.write(new String(contents));
			} finally {
				// flush and close both "output" and its underlying FileWriter
				if (output != null) {
					output.close();
				}
			}
		} catch (final FileNotFoundException ex) {
			ExceptionHandler.logger(ex, log);
		} catch (final IOException ex) {
			ExceptionHandler.logger(ex, log);
		}

		FileInputStream inFile = null;
		// So we've rewritten the corrected file, now we return a FileInputStream so
		// we can try to decode it again.
		try {
			inFile = new FileInputStream(file);
		} catch (final IOException ex) {
			ExceptionHandler.logger(ex, log);
		}

		return inFile;
	}
}
