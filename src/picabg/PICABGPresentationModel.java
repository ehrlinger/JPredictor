package picabg;

import org.apache.log4j.Logger;

import patient.PatientPresentationModel;

import com.jgoodies.validation.ValidationResult;

public class PICABGPresentationModel extends PatientPresentationModel {

	// Instance Creation ******************************************************

	private static Logger log = Logger.getLogger(PICABGPresentationModel.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -7044950341053705875L;

	public PICABGPresentationModel(PICABG patientModel) {
		super(patientModel);
		
	}

	// Event Handling *********************************************************

	protected void updateValidationResult(){
		log.debug("PICABG Validation model");
		PICABG patientModel = (PICABG) getBean();
		ValidationResult result = new PICABGValidator(patientModel).validate();
		validationResultModel.setResult(result);
	}

}
