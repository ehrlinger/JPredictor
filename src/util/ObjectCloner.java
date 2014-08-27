package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import support.ExceptionHandler;

/**
 * Haven't got this to work.. but serializing to XML has worked so far.
 * 
 * @author ehrlinger
 * 
 */
public class ObjectCloner {
	// returns a deep copy of an object
	static public Object deepCopy(final Object oldObj) throws Exception {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream(); // A
			oos = new ObjectOutputStream(bos); // B
			// serialize and pass the object
			oos.writeObject(oldObj); // C
			oos.flush(); // D
			final ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); // E
			ois = new ObjectInputStream(bin); // F
			// return the new object
			return ois.readObject(); // G
		} catch (final Exception e) {
			ExceptionHandler.logger(e, Logger.getLogger(ObjectCloner.class));
			throw (e);
		} finally {
			oos.close();
			ois.close();
		}
	}

	// so that nobody can accidentally create an ObjectCloner object
	private ObjectCloner() {
	}
}
