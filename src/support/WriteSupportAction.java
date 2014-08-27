package support;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import util.BaseAction;

public class WriteSupportAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4661159733449215576L;

	public WriteSupportAction(JFrame frame) {
		// Initialize our look and behavior
		super("Write a note to request application support.", new ImageIcon(
				"images/actions/mail-message-new.png"), "Write a support request note",
				new Integer(KeyEvent.VK_W), KeyStroke.getKeyStroke(KeyEvent.VK_W,
						ActionEvent.CTRL_MASK));
		this.putValue(Action.SHORT_DESCRIPTION,
				"Write a note to request application support.");
	}

	public void actionPerformed(ActionEvent e) {
		SupportDialog dlg = new SupportDialog(null);
		dlg.setVisible(true);
	}

}
