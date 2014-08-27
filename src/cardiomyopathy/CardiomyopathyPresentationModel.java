package cardiomyopathy;

import patient.PatientPresentationModel;

import com.jgoodies.validation.ValidationResult;

public class CardiomyopathyPresentationModel extends
		PatientPresentationModel {

	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = -8161490816893941656L;

	public CardiomyopathyPresentationModel(Cardiomyopathy patientModel) {
		super(patientModel);

	}

	// Event Handling *********************************************************

	protected void updateValidationResult() {
		Cardiomyopathy patientModel = (Cardiomyopathy) getBean();
		ValidationResult result = new CardiomyopathyValidator(patientModel)
				.validate();
		validationResultModel.setResult(result);
	}

}
