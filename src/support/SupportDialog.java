/*
 * OR Scheduler, Version 1.0
 *
 * Copyright (c) 2002 The Cleveland Clinic Foundation.
 *  All rights reserved.
 */

package support;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

/**
 * SupportDialog class extends from the JDialog class. It is the dialog for
 * sending the support request.
 * 
 * @author Sangeeta Huria
 * @version %I%, %G%
 * @version 1.0, 08/05/02
 * @see javax.swing.JDialog
 */
public class SupportDialog extends JDialog implements ActionListener {
	/**
	 * Logger log
	 */
	private static Logger log = Logger.getLogger(SupportDialog.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -2375931607308310263L;

	/**
	 * JButton closeButton
	 */
	private JButton closeButton;

	/**
	 * JButton sendButton
	 */
	private JButton sendButton;

	/**
	 * JTextArea textarea;
	 */
	private JTextArea textarea;

	/**
	 * JTextField textfield;
	 */
	private JTextField textfield;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *          parent frame
	 */
	public SupportDialog(JFrame parent) {
		super(parent, "Support Request", true);
		log.debug("Support Dialog");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel mainpanel = new JPanel();
		mainpanel.setOpaque(false);
		mainpanel.setLayout(new BorderLayout(5, 5));
		TitledBorder titleBorder = new TitledBorder(BorderFactory
				.createLineBorder(Color.BLACK), "Submit Comments/Support Request");
		mainpanel.setBorder(titleBorder);

		JPanel toppanel = new JPanel();
		toppanel.setLayout(new BorderLayout(10, 0));
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		JLabel sublabel = new JLabel(
				"<html><font color=green>Subject :</font></html>");
		p.add(sublabel, BorderLayout.NORTH);
		textfield = new JTextField();
		p.add(textfield, BorderLayout.CENTER);
		toppanel.add(p, BorderLayout.CENTER);
		toppanel.add(new JPanel(), BorderLayout.EAST);
		toppanel.add(new JPanel(), BorderLayout.WEST);
		mainpanel.add(toppanel, BorderLayout.NORTH);

		JPanel centerpanel = new JPanel();
		centerpanel.setLayout(new BorderLayout());
		JLabel msglabel = new JLabel(
				"<html><font color=green>Detailed Description " + ":</font></html>");
		centerpanel.add(msglabel, BorderLayout.NORTH);
		textarea = new JTextArea();
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(textarea);
		centerpanel.add(scroll);
		mainpanel.add(centerpanel, BorderLayout.CENTER);
		mainpanel.add(new JPanel(), BorderLayout.EAST);
		mainpanel.add(new JPanel(), BorderLayout.WEST);

		JPanel buttonpanel = new JPanel();
		buttonpanel.setOpaque(false);
		buttonpanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.LINE_AXIS));
		closeButton = new JButton("Cancel");
		closeButton.addActionListener(this);
		closeButton.setMnemonic(KeyEvent.VK_C);
		buttonpanel.add(closeButton);
		buttonpanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		sendButton.setMnemonic(KeyEvent.VK_S);
		buttonpanel.add(sendButton);

		mainpanel.add(buttonpanel, BorderLayout.SOUTH);
		contentPane.add(mainpanel, BorderLayout.CENTER);

		int width = 350;
		int height = 350;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);
	}

	/**
	 * Implementing actionPerformed function of interface ActionListener
	 * 
	 * @param evt
	 *          Event
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		// if Send button is clicked
		if (source == sendButton) {
			// checking if both subject and description are
			// blank
			if (textfield.getText().equals("") || textarea.getText().equals("")) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "Cannot send when subject or "
						+ "description is blank.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				// Creating a support request from subject and
				// description entered by user
				SupportRequest request = SupportRequest.getInstance();
				request.send(textfield.getText(), textarea.getText(), Property
						.getUinfo());
				setVisible(false);
				dispose();
			}
		} else if (source == closeButton) {
			// if Close button is clicked

			setVisible(false);
			dispose();
		}
	}
}
