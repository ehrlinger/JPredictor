package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class ExitAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8405894886373676497L;

	public ExitAction() {
		// Initialize our look and behavior
		super("Exit", new ImageIcon("images/actions/process-stop.png"), "Exit",
				new Integer(KeyEvent.VK_X), KeyStroke.getKeyStroke(KeyEvent.VK_X,
						ActionEvent.CTRL_MASK));
		// Save a reference to our owner
	}

	public void actionPerformed(final ActionEvent e) {
		System.exit(0);
	}

}
