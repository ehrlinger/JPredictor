/**
 * 
 */
package transplantSupport;


import org.apache.log4j.Logger;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;

/**
 * @author ehrlinger
 * 
 */
public final class TransplantSupportValidator implements Validator<TransplantSupport> {
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
	private static Logger log = Logger.getLogger(TransplantSupportValidator.class);

	/**
	 * Holds the order to be validated.
	 */
	private final TransplantSupport dataModel;

	// Instance Creation ******************************************************

	/**
	 * Constructs an OrderValidator on the given Order.
	 * 
	 * @param order
	 *          the order to be validated
	 */
	public TransplantSupportValidator(TransplantSupport dataModel) {
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
				dataModel, "TransplantSupport");

		// Place validations here.
		if (dataModel.getHeight() < 100 || dataModel.getHeight() > 225)
			support.addError("Height", "must be between 100 and 225 cm");

		if (!dataModel.getFemale() && dataModel.getNumPreg() > 0)
			support.addError("Number of Pregnancies", "must be 0 for male patients");

		if (dataModel.getNumPreg() < 0 || dataModel.getNumPreg() > 15)
			support.addError("Number of Pregnancies", "must between 0 and 15");

		if (dataModel.getInit_t() < 0 || dataModel.getInit_t() > 100)
			support.addError("Cytotoxic T-cell PRA", "must between 0 and 100 %");

		if (dataModel.getInit_b() < 0 || dataModel.getInit_b() > 100)
			support.addError("Cytotoxic B-cell PRA", "must between 0 and 100 %");

		// if (dataModel.getBpraflls() < 0 || dataModel.getBpraflls() > 100)
		// support.addError("B-cell cytometry", "must between 0 and 100 %");

		// if (dataModel.getAlbls() < 0.5 || dataModel.getAlbls() > 10)
		// support.addError("Albumin", "must between 0.5 and 10 g/dL");

		if (dataModel.getBunls() < 1 || dataModel.getBunls() > 150)
			support.addError("BUN", "must between 1 and 150 mg/dL");

		if (dataModel.getBilils() < 0.1 || dataModel.getBilils() > 14)
			support.addError("Bilirubin", "must between 0.1 and 14 mg/dL");
		if (dataModel.getCreals() < 0.3 || dataModel.getCreals() > 10)
			support.addError("Creatinine", "must be between 0.3 and 10 mg/dL");
		if (dataModel.getHgbls() < 7 || dataModel.getHgbls() > 22)
			support.addError("Hemoglobin", "must between 7 and 22");

		if (dataModel.getPaspr() < 12 || dataModel.getPaspr() > 120)
			support.addError("PA systolic pressure", "must between 12 and 120 mmHg");

//		if (dataModel.getPre_pvr() < 0 || dataModel.getPre_pvr() > 120)
//			support.addError("Pulmonary vascular resistance",
//					"must between 0 and 120 Wood Units");

		if (dataModel.getPcwpr() < 1 || dataModel.getPcwpr() > 55)
			support.addError("Pulmonary capillary wedge pressure",
					"must between 1 and 55 mmHg");

		if (dataModel.getLvefpr() < 5 || dataModel.getLvefpr() > 60)
			support.addError("LV ejection fraction", "must between 5 and 60 %");

		if (dataModel.getBirthDate() == null)
			support.addError("Birth Date", "is mandatory");
		if (dataModel.getListDate() == null)
			support.addError("List Date", "is mandatory");

		if (dataModel.getBirthDate() != null && dataModel.getListDate() != null
				&& dataModel.getBirthDate().after(dataModel.getListDate()))
			support.addWarning("List Date", "must be after the patient birth date");

		return support.getResult();
	}

	public ValidationResult validate(TransplantSupport arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
