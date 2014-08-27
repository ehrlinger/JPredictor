package patientModelSet;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

import support.ExceptionHandler;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

public class PatientModelSetPresentationModel extends PresentationModel<PatientModelSet> {

	// Instance Creation ******************************************************

	private static Logger log = Logger
			.getLogger(PatientModelSetPresentationModel.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -7044950341053705875L;

	private ValidationResultModel validationResultModel;

	public PatientModelSetPresentationModel(PatientModelSet patientModel) {
		super(patientModel);
		validationResultModel = new DefaultValidationResultModel();
		initEventHandling();
		updateValidationResult();
	}

	// Event Handling *********************************************************

	protected void updateValidationResult() {
		log.debug(" updateValidationResults");
		try {
			PatientModelSet patientModel = (PatientModelSet) getBean();
			ValidationResult result = new PatientModelSetValidator(patientModel)
					.validate();
			validationResultModel.setResult(result);
		} catch (NullPointerException e1) {
			log.debug("");
			log.debug("NullPointer");
			ExceptionHandler.logger(e1, log);
		}
	}

	/**
	 * Validates the PatientModel using an PatientModelValidator and updates the
	 * validation result.
	 */
	protected final class ValidationUpdateHandler implements
			PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			updateValidationResult();
		}

	}

	// Initialization *********************************************************

	public ValidationResultModel getValidationResultModel() {
		return validationResultModel;
	}

	// Event Handling *********************************************************

	/**
	 * Listens to changes in all properties of the current PatientModel and to
	 * PatientModel changes.
	 */
	protected void initEventHandling() {
		PropertyChangeListener handler = new ValidationUpdateHandler();
		addBeanPropertyChangeListener(handler);
		getBeanChannel().addValueChangeListener(handler);
	}

}
