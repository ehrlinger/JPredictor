package util;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public abstract class BaseAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3426534538659963487L;
	protected String path = System.getProperty("user.dir");

	public BaseAction(final String name, final ImageIcon icon, final String tooltip,
			final Integer mnemonic, final KeyStroke accelerator) {
		this.putValue(Action.NAME, name);
		if (icon != null) {
			this.putValue(Action.SMALL_ICON, icon);
		} else {

			this.putValue(Action.SMALL_ICON, name);
		}
		this.putValue(Action.SHORT_DESCRIPTION, tooltip);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
		this.putValue(Action.ACCELERATOR_KEY, accelerator);
	}

	public void setPath(final String path) {
		this.path = path;
	}

}