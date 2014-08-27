package patient;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

public abstract class PatientPresentationModel extends PresentationModel<PatientModel> {

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

	/**
	 * 
	 */
	private static final long serialVersionUID = -5431484040583620631L;

	// Instance Creation ******************************************************

	protected final ValidationResultModel validationResultModel;

	// Exposing Models ********************************************************

	public PatientPresentationModel(PatientModel patientModel) {
		super(patientModel);
		validationResultModel = new DefaultValidationResultModel();
		initEventHandling();
		updateValidationResult();
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

	protected abstract void updateValidationResult();

}
