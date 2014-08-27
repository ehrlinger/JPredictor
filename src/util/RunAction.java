package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public abstract class RunAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1189910365441825289L;

	public RunAction() {
		super("Run Analysis", new ImageIcon(
				"images/categories/applications-system.png"), "Run Analysis",
				new Integer(KeyEvent.VK_R), KeyStroke.getKeyStroke(KeyEvent.VK_R,
						ActionEvent.CTRL_MASK));
	}

	public abstract void actionPerformed(ActionEvent e);

}
