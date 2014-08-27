/**
 * 
 */
package patientModelSet;

import org.apache.log4j.Logger;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;

/**
 * @author John Ehrlinger
 * @version $Revision: 1.0 $
 * 
 */
public final class PatientModelSetValidator implements Validator<PatientModelSet> {
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

	/**
	 * Logger log;
	 */
	private static Logger log = Logger.getLogger(PatientModelSetValidator.class);

	/**
	 * Holds the order to be validated.
	 */
	private final PatientModelSet patientSet;

	// Instance Creation ******************************************************

	/**
	 * Constructs an OrderValidator on the given Order.
	 * 
	 * @param order
	 *          the order to be validated
	 */
	public PatientModelSetValidator(PatientModelSet patientSet) {
		this.patientSet = patientSet;
		log.debug("PatientModelSetValidator");
	}

	/**
	 * Validates this Validator's Order and returns the result as an instance of
	 * {@link ValidationResult}.
	 * 
	 * @return the ValidationResult of the order validation
	 */
	public ValidationResult validate() {
		PropertyValidationSupport support = new PropertyValidationSupport(
				patientSet, "PatientModelSet");

		// Place validations here.
		log.debug(patientSet.getGraphTime());

		if (patientSet.getGraphTime() < 0)
			support.addError("Graph Time", "must be positive");

		// Return what we found...
		return support.getResult();
	}

	public ValidationResult validate(PatientModelSet arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
