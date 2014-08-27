/*
 * AboutDialog 1.0
 *
 * Copyright (c) 2002 The Cleveland Clinic Foundation.
 *  All rights reserved.
 *  @author John Ehrlinger
 */

package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import support.Property;

/**
 * AboutDialog class displays a dialog box displaying the name of application,
 * logo and copyright notice.
 * 
 * @author John Ehrlinger
 * @version 3.0 
 * @see javax.swing.JDialog
 */
public class AboutDialog extends JDialog {
	private static final String downloadLocation = "wiki/index.php/JPredictor";

	/**
	 * Logger log;
	 */
	private static final Logger log = Logger.getLogger(AboutDialog.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -2359802393871439747L;

	/**
	 * 
	 */
	private Box aboutBox;
	final JButton ok = new JButton("Close This Dialog");
	final JButton update = new JButton("Get the update");
	private boolean enable = true;
	
	public boolean isEnable() {
		
		return enable;
	}

	public void setEnable(boolean enable) {
		update.setEnabled(enable);
		ok.setEnabled(enable);
		this.enable = enable;
	}

	private Version version;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *          parent frame
	 */
	public AboutDialog(final JFrame parent, final String progName, final String title) {
		super(parent, title, true);
		setResizable(false);
		final Container contentPane = getContentPane();
		// Creating Box to display the information
		aboutBox = Box.createVerticalBox();
		final JPanel logopanel = new JPanel();
		logopanel.setBackground(Color.WHITE);
		final JLabel logolabel = new JLabel(new ImageIcon("images/cc-logo.png"));
		logopanel.add(logolabel);
		logopanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		aboutBox.add(logopanel);

		// Add the title and version information.
		final JLabel namelabel = new JLabel("<html><font color=green>"
				+ Property.getTitle() + " - " + Property.getVersion()
				+ "</font></html>", SwingConstants.CENTER);
		namelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		aboutBox.add(namelabel);

		// Department label
		final JLabel tclabel = new JLabel("<html><font color=green>"
				+ Property.getVendor() + "</font></html>", SwingConstants.CENTER);
		tclabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		aboutBox.add(tclabel);

		version = Version.getInstance(progName);
		// Creating panel for OK/update button
		final JPanel p = new JPanel();
		p.setOpaque(false);
		// Check for application updates here.
		// Creating OK button
		p.add(ok);

		// Adding private ActionListener on OK button
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				setVisible(false);
			}
		});
		
		//We want users to only dismiss this dialog when not a splash...
		update.setEnabled(enable);
		ok.setEnabled(enable);
		
		if (!version.isUpToDate(downloadLocation)) {
			final String updateMessage = "<html><center><font size=+1 color=\"red\">A PROGRAM UPDATE HAS BEEN RELEASED!<br><br>"
					+ "Install the update from:<br>"
					+ version.getVersionURL()
					+ downloadLocation
					+ "<br><br></font>"
					+ "The \"Get the update\" button will point your browser <br>to the correct location to update your program.<br>"
					+ "<br><br></center>";

			final JLabel updatelabel = new JLabel(updateMessage, SwingConstants.CENTER);
			updatelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			aboutBox.add(updatelabel);
			// Creating OK button
			p.add(update);

			// Adding private ActionListener on OK button
			update.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent evt) {
					BareBonesBrowserLaunch.openURL(version.getVersionURL()
							+ downloadLocation);
				}
			});

		}

		// Copyright label.
		final JLabel cplabel = new JLabel("<html><font color=green>"
				+ "Copyright \u00A9 2006-2010</font></html>", SwingConstants.CENTER);
		cplabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		aboutBox.add(cplabel);
		aboutBox.add(Box.createVerticalGlue());

		contentPane.add(aboutBox, "Center"); // Adding box in Center

		// p is the button panel
		getContentPane().add(p, "South"); // Adding panel in South

		// Creating a rectangle to obtain the parent bounds
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension size = getPreferredSize();

		log.debug("screen size (width, height) : (" + screenSize.width + ", "
				+ screenSize.height + ")");
		log.debug("splash size (width, height) : (" + size.width + ", "
				+ size.height + ")");

		setLocation(screenSize.width / 2 - (size.width / 2), screenSize.height / 2
				- (size.height / 2));

		pack();
	}

}
