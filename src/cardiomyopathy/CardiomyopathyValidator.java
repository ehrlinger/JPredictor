/**
 * 
 */
package cardiomyopathy;

import org.apache.log4j.Logger;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;

/**
 * @author ehrlinger
 * 
 */
public final class CardiomyopathyValidator implements Validator<Cardiomyopathy> {
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
	private static Logger log = Logger.getLogger(CardiomyopathyValidator.class);

	/**
	 * Holds the order to be validated.
	 */
	private final Cardiomyopathy dataModel;

	// Instance Creation ******************************************************

	/**
	 * Constructs an OrderValidator on the given Order.
	 * 
	 * @param order
	 *          the order to be validated
	 */
	public CardiomyopathyValidator(Cardiomyopathy dataModel) {
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
				dataModel, "Cardiomyopathy");

		// Place validations here.
		if (dataModel.getChol_pr() < 9 || dataModel.getChol_pr() > 400)
			support.addError("Cholesterol", "must between 9 and 400");

		if (dataModel.getBun_pr() < 4 || dataModel.getBun_pr() > 120)
			support.addError("Blood Urea Nitrogen", "must between 3.4 and 128 mg/dL");
		if (dataModel.getBlrbn_pr() < 0.1 || dataModel.getBlrbn_pr() > 9)
			support.addError("Bilirubin", "must between 0.1 and 9 mg/dL");

		if (dataModel.getHct_pr() < 9 || dataModel.getHct_pr() > 54)
			support.addError("Hematocrit", "must between 9 and 54%");

		if (dataModel.getEf_com() < 5 || dataModel.getEf_com() > 77)
			support.addError("LV ejection fraction", "must between 5 and 77 %");

		if (dataModel.getIv_pmi() < 0 || dataModel.getIv_pmi() > 38)
			support.addError("iv_pmi", "must between 0 and 38 years");
		
		if (dataModel.getAge() < 6.6 || dataModel.getAge() > 85)
			support.addWarning("age", " Outside of data range of 6.6 years to 85 years");

		if (dataModel.getBirthDate() != null && dataModel.getSurgeryDate() != null
				&& dataModel.getBirthDate().after(dataModel.getSurgeryDate()))
			support.addError("Surgical Date", "must be after the patient birth date");

		if (dataModel.getBirthDate() != null && dataModel.getMiDate() != null
				&& dataModel.getBirthDate().after(dataModel.getMiDate()))
			support.addError("MI Date", "must be after the patient birth date");

		if (dataModel.getMiDate() != null && dataModel.getSurgeryDate() != null
				&& dataModel.getMiDate().after(dataModel.getSurgeryDate()))
			support.addError("MI Date", "must be before the patient surgery date");

		return support.getResult();
	}

	public ValidationResult validate(Cardiomyopathy arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
