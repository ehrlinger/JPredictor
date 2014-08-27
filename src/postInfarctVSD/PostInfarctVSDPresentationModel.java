package postInfarctVSD;

import patient.PatientPresentationModel;

import com.jgoodies.validation.ValidationResult;

public class PostInfarctVSDPresentationModel extends PatientPresentationModel {

	// Instance Creation ******************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = -44814912782372458L;

	public PostInfarctVSDPresentationModel(PostInfarctVSD patientModel) {
		super(patientModel);

	}

	// Event Handling *********************************************************

	protected void updateValidationResult() {
		PostInfarctVSD patientModel = (PostInfarctVSD) getBean();
		ValidationResult result = new PostInfarctVSDValidator(patientModel)
				.validate();
		validationResultModel.setResult(result);
	}

}
