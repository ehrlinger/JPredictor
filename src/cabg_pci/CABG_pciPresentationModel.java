package cabg_pci;

import patient.PatientPresentationModel;

import com.jgoodies.validation.ValidationResult;

public class CABG_pciPresentationModel extends
		PatientPresentationModel {

	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = -8161490816893941656L;

	public CABG_pciPresentationModel(CABG_pci patientModel) {
		super(patientModel);

	}

	// Event Handling *********************************************************

	protected void updateValidationResult() {
		CABG_pci patientModel = (CABG_pci) getBean();
		ValidationResult result = new CABG_pciValidator(patientModel)
				.validate();
		validationResultModel.setResult(result);
	}

}
