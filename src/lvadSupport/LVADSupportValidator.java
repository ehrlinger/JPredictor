/**
 * 
 */
package lvadSupport;


import org.apache.log4j.Logger;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;

/**
 * @author ehrlinger
 * 
 */
public final class LVADSupportValidator implements Validator<LVADSupport> {
	/**
	 * Validates PICABG. This Validator style is the first choice for a domain
	 * object validation. It allows to use different validators for the same
	 * domain class. This can be useful if you have strict and relaxed validator
	 * for different validation contexts, such as saving (strict) and printing
	 * (relaxed). If the domain class validates itself (as in the
	 * ValidatingOrder), it is more difficult to implement this feature, or it may
	 * be more difficult to understand.
	 * 
	 * @author Karsten Lentzsch
	 * @version $Revision: 1.2 $
	 * 
	 * @see ValidatingOrder
	 */
	private static Logger log = Logger.getLogger(LVADSupportValidator.class);

	/**
	 * Holds the order to be validated.
	 */
	private final LVADSupport dataModel;

	// Instance Creation ******************************************************

	/**
	 * Constructs an OrderValidator on the given Order.
	 * 
	 * @param order
	 *          the order to be validated
	 */
	public LVADSupportValidator(LVADSupport dataModel) {
		this.dataModel = dataModel;
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result as an instance of
	 * {@link ValidationResult}.
	 * 
	 * @return the ValidationResult of the order validation
	 */
	public ValidationResult validate() {
		log.debug("Validate " + this.getClass().getSimpleName());
		
		PropertyValidationSupport support = new PropertyValidationSupport(
				dataModel, "LVADSupport");

		// Place validations here.
		if (dataModel.getHeight() < 128 || dataModel.getHeight() > 205)
			support.addError("Height", "must be between 128 and 205 cm");
	
		if (dataModel.getWeight() < 24 || dataModel.getWeight() > 172)
			support.addError("Weight", "must be between 24 and 172 kg");

		if (dataModel.getBun_pr() < 9 || dataModel.getBun_pr() > 124)
			support.addError("Bun_pr", "must be between 9 and 124 mg/dL");
	
		if (dataModel.getMcsDate() == null)
			support.addError("MCS_Date", "is mandatory");

		if (dataModel.getMcsDate() != null && dataModel.getMcsDate2() != null
				&& dataModel.getMcsDate().after(dataModel.getMcsDate2()))
			support.addError("MCS_Date_2", "must be after the index MCS date");
		if (dataModel.getMcsDate2() != null && dataModel.getMcsDate3() != null
				&& dataModel.getMcsDate2().after(dataModel.getMcsDate3()))
			support.addError("MCS_Date_3", "must be after the 2nd MCS date");
		if (dataModel.getMcsDate2() == null && dataModel.getMcsDate3() != null)
			support.addError("MCS_Date_3", "must be after the 2nd MCS date");
		
		return support.getResult();
	}

	public ValidationResult validate(LVADSupport arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
