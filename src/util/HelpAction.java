package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

public class HelpAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2444143023395608748L;

	private String helpPath = "http://ctresearch.ccf.org/wiki/index.php";

	private final Logger log = Logger.getLogger(HelpAction.class);

	public HelpAction(final JFrame frame) {
		// Initialize our look and behavior
		super("Help", new ImageIcon("images/apps/help-browser.png"),
				"Display Application Help", new Integer(KeyEvent.VK_F1), KeyStroke
						.getKeyStroke(KeyEvent.VK_F1, ActionEvent.CTRL_MASK));

	}

	public void actionPerformed(final ActionEvent e) {
		log.debug("HELP!!!");
		// Help feature
		// Since we'd like to wiki our help, instead of
		// writing specific application package help files...
		BareBonesBrowserLaunch.openURL(helpPath);
	}

	public void setHelpPath(final String helpPath) {
		this.helpPath = helpPath;
	}

}
