/**
 * 
 */
package cabg_pci;

import org.apache.log4j.Logger;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;

/**
 * @author ehrlinger
 * 
 */
public final class CABG_pciValidator implements Validator<CABG_pci> {
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
	private static Logger log = Logger.getLogger(CABG_pciValidator.class);

	/**
	 * Holds the order to be validated.
	 */
	private final CABG_pci dataModel;

	// Instance Creation ******************************************************

	/**
	 * Constructs an OrderValidator on the given Order.
	 * 
	 * @param order
	 *          the order to be validated
	 */
	public CABG_pciValidator(CABG_pci dataModel) {
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
				dataModel, "CABG_pci");

		// Place validations here.
		if (dataModel.getHt() < 0 || dataModel.getHt() > 211)
			support.addError("Height", "must between 0 and 211");
		if (dataModel.getWt() < 10 || dataModel.getWt() > 204)
			support.addError("Weight", "must between 10 and 204");
		if (dataModel.getLmt() < 0 || dataModel.getLmt() > 1.0)
			support.addError("Maximum Left Main Trunk Stenosis", "must between 0 and 1.0");
		if (dataModel.getLvef() < 5 || dataModel.getLvef() > 83)
			support.addError("LVEF (Cath) If missing then from Echo ", "must between 5 and 83");
		if (dataModel.getBpsyst() < 11 || dataModel.getBpsyst() > 250)
			support.addError("Syst Blood Pressure", "must between 11 and 250");
		if (dataModel.getBpdias() < 1 || dataModel.getBpdias() > 180)
			support.addError("Weight", "must between 10 and 204");
		if (dataModel.getCreat_pr() < 0.09 || dataModel.getCreat_pr() > 33)
			support.addError("Preop Creatinine ", "must between 0.09 and 33");
		if (dataModel.getBun_pr() < .6 || dataModel.getBun_pr() > 416)
			support.addError("Blood Urea Nitrogen", "must between 3.4 and 128 mg/dL");

		if (dataModel.getHct_pr() < 3.4 || dataModel.getHct_pr() > 67)
			support.addError("Preop Hematocrit", "must between 3.4 and 67%");
	
		if (dataModel.getAge() < 18 || dataModel.getAge() > 97)
			support.addWarning("age", " Outside of data range of 18 years to 97 years");

		return support.getResult();
	}

	public ValidationResult validate(CABG_pci arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
