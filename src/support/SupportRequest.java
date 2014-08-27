/*
 * 
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  - Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that Software is not designed, licensed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

package support;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * SupportRequest class represent the singleton class for submitting the support
 * request. Its requires file config/support.properties for reading the Mail
 * host, Mail interface header properties etc.
 * 
 * @author Sangeeta Huria
 * @version 1.0, 10/10/02
 */
public class SupportRequest {
	private static SupportRequest _instance;

	private static Logger log = Logger.getLogger(SupportRequest.class);

	/**
	 * Singletons getInstance() function
	 */
	public static SupportRequest getInstance() {

		log.debug("SupportRequest.getInstance");
		if (_instance == null) {
			_instance = new SupportRequest();
		}
		return _instance;
	}

	private String host = "";

	private String msgText1 = "";

	private String msgText2 = "";

	private String msgText3 = "";

	private String msgText4 = "";

	private String msgText5 = "";

	private String msgText6 = "";

	private String msgText7 = "";

	private String recipient = "";

	private String sender = "";

	/**
	 * Constructor
	 */
	private SupportRequest() {
		Properties props = new Properties();
		try {
			InputStream input;
			File file = new File("config/support.properties");
			props.load(input = new FileInputStream(file));
			input.close();
			// Creating the header entries as required by Mail interface for Bugzilla
			msgText1 = "@product=" + props.getProperty("product") + "\n";
			msgText2 = "@component=" + props.getProperty("component") + "\n";

			msgText4 = "@bug_severity=normal\n";
			msgText5 = "@priority=P1\n";
			msgText6 = "@rep_platform=PC\n";
			msgText7 = "@op_sys=" + System.getProperty("os.name") + "\n";
			host = props.getProperty("mailhost");
			sender = props.getProperty("sender");
			recipient = props.getProperty("recipient");
			msgText3 = "@version=";
			/*
			 * + Package.getPackage(props.getProperty("product"))
			 * .getImplementationVersion() + "\n";
			 */
		} catch (IOException e) {
			msgText3 = "@version=" + Package.getPackage(props.getProperty("product"));
			log.debug(e.toString());
		}
	}

	/**
	 * Sends the email containing subject and description entered by user to the
	 * recipient as specified in the support.properties file
	 */
	public void send(String subject, String desc, String userinfo) {
		// Create properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);

		Session session = Session.getDefaultInstance(props, null);

		try {
			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			InternetAddress[] address = { new InternetAddress(recipient) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// create and fill the message part
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(msgText1);
			MimeBodyPart mbp2 = new MimeBodyPart();
			mbp2.setText(msgText2);
			MimeBodyPart mbp3 = new MimeBodyPart();
			mbp3.setText(msgText3);
			MimeBodyPart mbp4 = new MimeBodyPart();
			mbp4.setText(msgText4);
			MimeBodyPart mbp5 = new MimeBodyPart();
			mbp5.setText(msgText5);
			MimeBodyPart mbp6 = new MimeBodyPart();
			mbp6.setText(msgText6);
			MimeBodyPart mbp7 = new MimeBodyPart();
			mbp7.setText(msgText7);
			MimeBodyPart mbp8 = new MimeBodyPart();
			mbp8.setText("@short_desc=" + subject + "\n");
			MimeBodyPart mbp9 = new MimeBodyPart();
			mbp9.setText("Submitted by " + userinfo + "\n\n" + desc);

			// create the Multipart and its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);
			mp.addBodyPart(mbp3);
			mp.addBodyPart(mbp4);
			mp.addBodyPart(mbp5);
			mp.addBodyPart(mbp6);
			mp.addBodyPart(mbp7);
			mp.addBodyPart(mbp8);
			mp.addBodyPart(mbp9);

			// add the Multipart to the message
			msg.setContent(mp);

			// send the message
			Transport.send(msg);
			log.debug(msg);
		} catch (SecurityException se) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Send Failure. " + se.toString(),
					"Security Exception Error", JOptionPane.ERROR_MESSAGE);
			log.debug(se.toString());
		} catch (MessagingException mex) {
			Toolkit.getDefaultToolkit().beep();
			/*
			 * We could post to the screen, but that's kind of annoying unless we can
			 * get a singleton.
			 * 
			 * JOptionPane.showMessageDialog(null, "Send Failure. " + mex.toString(),
			 * "Messaging Exception Error", JOptionPane.ERROR_MESSAGE);
			 */
			mex.printStackTrace();
			Exception ex = null;
			log.debug(mex.toString());
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Submits the exception and stack trace into Bugzilla
	 */
	public void sendBug(Exception e, StackTraceElement[] st, String userinfo) {
		String subject = e.toString();
		StringBuffer desc = new StringBuffer();
		for (int i = 0; i < st.length; i++)
			desc.append(st[i].toString()).append("\n");

		log.debug("sendBug: " + desc);
		send(subject, desc.toString(), userinfo);
	}
}
