/**
 * 
 */
package picabg;

import org.apache.log4j.Logger;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;

/**
 * @author ehrlinger
 * 
 * @version $Revision: 1.0 $
 */
public final class PICABGValidator implements Validator<PICABG> {
	/**
	 * Logger log;
	 */
	private static Logger log = Logger.getLogger(PICABGValidator.class);

	private PICABG picabg;

	/**
	 * Validates PICABG. This Validator style is the first choice for a domain
	 * object validation. It allows to use different validators for the same
	 * domain class. This can be useful if you have strict and relaxed validator
	 * for different validation contexts, such as saving (strict) and printing
	 * (relaxed). If the domain class validates itself (as in the
	 * ValidatingOrder), it is more difficult to implement this feature, or it may
	 * be more difficult to understand.
	 * 
	 * 
	 * @see ValidatingOrder
	 */

	// Instance Creation ******************************************************
	/**
	 * Constructs an OrderValidator on the given Order.
	 * 
	 * @param order
	 *          the order to be validated
	 */
	public PICABGValidator(PICABG patientModel) {
		picabg = patientModel;
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
		
		PropertyValidationSupport support = new PropertyValidationSupport(picabg,
				"picabg");

		// Place validations here.
		log.debug(picabg.getAge());

		if (picabg.getAge() < 18 || picabg.getAge() > 99)
			support.addError("Patient Age", "must be between 18 and 99 years");

		if (picabg.getHeight() < 100 || picabg.getHeight() > 225)
			support.addError("Patient Height", "must be between 100 and 225 cm");

		if (picabg.getWeight() < 40 || picabg.getWeight() > 200)
			support.addError("Patient Weight", "must be between 40 and 200 kg");
		// -----
		if (picabg.getCreat_pr() < 0.3 || picabg.getCreat_pr() > 10)
			support.addError("Creatinine", "must be between 0.3 and 10 mg/dL");

		if (picabg.getHct_pr() < 7 || picabg.getHct_pr() > 55)
			support.addError("Hematocrit", "must be between 7 and 55 %");

		if (picabg.getTrig_pr() < 50 || picabg.getTrig_pr() > 1000)
			support.addError("Triglycerides", "must be between 50 and 1000 mg/dL");

		if (picabg.getLmt() < 0 || picabg.getLmt() > 100)
			support.addError("LMT Disease", "must be between 0 and 100 % stenosis");

		if (picabg.getLad() < 0 || picabg.getLad() > 100)
			support.addError("LAD Disease",
					"must be between 0 and 100 max % stenosis");

		if (picabg.getLcx() < 0 || picabg.getLcx() > 100)
			support.addError("LCX Disease",
					"must be between 0 and 100 max % stenosis");

		if (picabg.getRca() < 0 || picabg.getRca() > 100)
			support.addError("Right Coronary System Disease",
					"must be between 0 and 100 % stenosis");

		// Return what we found...
		return support.getResult();
	}

	public ValidationResult validate(PICABG arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
