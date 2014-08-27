/**
 * 
 */
package postInfarctVSD;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;

/**
 * @author ehrlinger
 * 
 */
public final class PostInfarctVSDValidator implements Validator<PostInfarctVSD> {
	/**
	 * Validates postInfarctVSD. This Validator style is the first choice for a
	 * domain object validation. It allows to use different validators for the
	 * same domain class. This can be useful if you have strict and relaxed
	 * validator for different validation contexts, such as saving (strict) and
	 * printing (relaxed). If the domain class validates itself (as in the
	 * ValidatingOrder), it is more difficult to implement this feature, or it may
	 * be more difficult to understand.
	 * 
	 * @author Karsten Lentzsch
	 * @version $Revision: 1.1 $
	 * 
	 * @see ValidatingOrder
	 */

	/**
	 * Logger log;
	 */
	private static Logger log = Logger.getLogger(PostInfarctVSDValidator.class);

	// Instance Creation ******************************************************

	/**
	 * Holds the order to be validated.
	 */
	private final PostInfarctVSD dataModel;

	/**
	 * Constructs an OrderValidator on the given Order.
	 * 
	 * @param order
	 *          the order to be validated
	 */
	public PostInfarctVSDValidator(PostInfarctVSD postInfarctVSD) {
		this.dataModel = postInfarctVSD;

	}

	/**
	 * Validates this Validator's Order and returns the result as an instance of
	 * {@link ValidationResult}.
	 * 
	 * @return the ValidationResult of the order validation
	 */
	public ValidationResult validate() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		log.debug("Validate " + this.getClass().getSimpleName());
		PropertyValidationSupport support = new PropertyValidationSupport(
				dataModel, "postInfarctVSD");

		// Place validations here.
		if (dataModel.getIv_misg() < 0 || dataModel.getIv_misg() > 670)
			support.addError("Time between MI and operation",
					"must be between 0 and 670 days");

		if (dataModel.getBun() <= 0 || dataModel.getBun() > 145)
			support.addError("Blood Urea Nitrogen", "must be between 0 and 145");

		if (dataModel.getOpDate() == null)
			support.addError("Operation Date", "is mandatory");

		if (dataModel.getOpDate() != null
				&& dataModel.getOpDate().before(dataModel.getEpochDate()))
			support.addError("Operation Date", "must be after the epoch date of "
					+ formatter.format(dataModel.getEpochDate()));

		// Return what we found...
		return support.getResult();
	}

	public ValidationResult validate(PostInfarctVSD arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
