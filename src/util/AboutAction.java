package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class AboutAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -651334152551264179L;

	private JFrame frame;

	private String programName;

	public AboutAction(final JFrame owner, final String title) {
		// Initialize our look and behavior
		super("About", new ImageIcon("images/status/dialog-information.png"),
				"About", new Integer(KeyEvent.VK_A), KeyStroke.getKeyStroke(
						KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		// Save a reference to our owner
		frame = owner;
		programName = title;
	}

	public void actionPerformed(final ActionEvent e) {
		final AboutDialog dialog = new AboutDialog(frame, programName, "About");
		dialog.setVisible(true);
	}

}
