package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import support.ExceptionHandler;

public class NewAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7160691815889010199L;

	private final Logger log = Logger.getLogger(NewAction.class);

	private String type;

	private Updatable updateObject;

	public NewAction(final ImageIcon newImage, final Updatable updateObject) {
		super("New Patient", newImage, "New Patient", new Integer(KeyEvent.VK_N),
				KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		this.updateObject = updateObject;

	}

	public NewAction(final Updatable updateObject) {
		this(new ImageIcon("images/actions/document-new.png"), updateObject);
	}

	public void actionPerformed(final ActionEvent e) {
		clearPatient();
	}

	/**
	 * @param inFile
	 */
	private void clearPatient() {

		Object savedObject;
		try {
			savedObject = Class.forName(type).newInstance();
			updateObject.update(savedObject);
		} catch (final Exception e) {
			ExceptionHandler.logger(e, log);
		}

	}

	public void setObjectType(final String type) {
		this.type = type;
	}
}
