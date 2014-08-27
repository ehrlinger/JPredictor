package patient;

// This exception is thrown by the execution method. It's designed to be used 
// if we screw up the execution string, by not translating boolean to 0/1 or some other testable error state.
public class ModelExecuteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4309523719079062971L;

	public ModelExecuteException(String cmd) {
		super(cmd);
	}
}
