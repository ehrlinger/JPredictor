package util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import predictor.PredictorFrame;
import support.ExceptionHandler;

public class PrintAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5951431448615726769L;

	private final Logger log = Logger.getLogger(PrintAction.class);

	private OrientationRequested orient = OrientationRequested.PORTRAIT;

	private PredictorFrame owner;

	public PrintAction(final PredictorFrame frame) {
		// Initialize our look and behavior
		super("Print", new ImageIcon("images/actions/document-print.png"),
				"Print this view", new Integer(KeyEvent.VK_P), KeyStroke.getKeyStroke(
						KeyEvent.VK_P, ActionEvent.CTRL_MASK));

		// Save a reference to our owner
		this.owner = frame;
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(final ActionEvent event) {
		log.debug("PrintAction: Action caught");
		printRequest();
	}

	/**
	 * Sends the print request and brings up the print dialog box
	 */
	public void printRequest() {
		log.debug("printRequest");
		final PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new JobName("JPredictor", null));

		aset.add(MediaSizeName.NA_LETTER);
		aset.add(orient);
		aset.add(new MediaPrintableArea(0.5f, 0.5f, 7.5f, 10.0f,
				MediaPrintableArea.INCH));

		final PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(owner);

		final PrintService[] services = PrinterJob.lookupPrintServices();

		if (services.length > 0) {
			try {
				printJob.setPrintService(services[0]);
				if (printJob.printDialog(aset)) {
					log.debug("print command");
					printJob.print(aset);
				}
			} catch (final PrinterException ex) {
				JOptionPane.showMessageDialog(null, ex.toString(), "Printer Error",
						JOptionPane.ERROR_MESSAGE);
				ExceptionHandler.logger(ex, log);
			}
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"No printer service found. Probably no printer is installed on this machine",
							"Printer Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setOrientation(final OrientationRequested or) {
		orient = or;
	}

}
