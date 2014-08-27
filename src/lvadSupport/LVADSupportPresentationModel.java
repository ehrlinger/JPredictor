package lvadSupport;

import patient.PatientPresentationModel;

import com.jgoodies.validation.ValidationResult;

public class LVADSupportPresentationModel extends
		PatientPresentationModel {

	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = -8161490816893941656L;

	public LVADSupportPresentationModel(LVADSupport patientModel) {
		super(patientModel);

	}

	// Event Handling *********************************************************

	protected void updateValidationResult() {
		LVADSupport patientModel = (LVADSupport) getBean();
		ValidationResult result = new LVADSupportValidator(patientModel)
				.validate();
		validationResultModel.setResult(result);
	}

}
