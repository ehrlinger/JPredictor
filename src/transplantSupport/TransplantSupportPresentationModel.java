package transplantSupport;

import patient.PatientPresentationModel;

import com.jgoodies.validation.ValidationResult;

public class TransplantSupportPresentationModel extends
		PatientPresentationModel {

	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = -8161490816893941656L;

	public TransplantSupportPresentationModel(TransplantSupport patientModel) {
		super(patientModel);

	}

	// Event Handling *********************************************************

	protected void updateValidationResult() {
		TransplantSupport patientModel = (TransplantSupport) getBean();
		ValidationResult result = new TransplantSupportValidator(patientModel)
				.validate();
		validationResultModel.setResult(result);
	}

}
