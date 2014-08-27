package util;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import support.ExceptionHandler;

/**
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an
 * abstract class that you subclass to perform GUI-related work in a dedicated
 * thread. For instructions on using this class, see:
 * 
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 * 
 * Note that the API changed slightly in the 3rd version: You must now invoke
 * start() on the SwingWorker after creating it.
 * 
 * @author JavaWorld article.
 * @version %I% %G%
 */
public abstract class SwingWorker {

	/**
	 * Class to maintain reference to current worker thread under separate
	 * synchronization control.
	 */
	private static class ThreadVar {

		/**
		 * Thread thread
		 */
		private Thread thread;

		/**
		 * ThreadVar
		 * 
		 * @param t
		 *          Thread to work on
		 * 
		 */
		ThreadVar(final Thread t) {
			thread = t;
		}

		/**
		 * clear
		 */
		synchronized void clear() {
			thread = null;
		}

		/**
		 * get
		 * 
		 * @return thread
		 */
		synchronized Thread get() {
			return thread;
		}
	}

	/**
	 * Logger log
	 */
	private static final Logger log = Logger.getLogger(SwingWorker.class);

	/**
	 * ThreadVar
	 */
	protected ThreadVar threadVar;

	/**
	 * value
	 * 
	 * @see getValue
	 * @see setValue
	 */
	private Object value;

	/**
	 * Start a thread that will call the <code>construct</code> method and then
	 * exit.
	 */
	public SwingWorker() {
		log.debug("SwingWorker");

		final Runnable doFinished = new Runnable() {
			public void run() {
				finished();
			}
		};

		final Runnable doConstruct = new Runnable() {
			public void run() {
				try {
					setValue(construct());
				} finally {
					threadVar.clear();
				}

				SwingUtilities.invokeLater(doFinished);
			}
		};

		final Thread t = new Thread(doConstruct);
		threadVar = new ThreadVar(t);
	}

	/**
	 * Compute the value to be returned by the <code>get</code> method.
	 * 
	 * @return abstract
	 */
	public abstract Object construct();

	/**
	 * Called on the event dispatching thread (not on the worker thread) after the
	 * <code>construct</code> method has returned.
	 */
	public void finished() {
	}

	/**
	 * Return the value created by the <code>construct</code> method. Returns
	 * null if either the constructing thread or the current thread was
	 * interrupted before a value was produced.
	 * 
	 * @return the value created by the <code>construct</code> method
	 */
	public Object get() {
		while (true) {
			final Thread t = threadVar.get();
			if (t == null) {
				return getValue();
			}
			try {
				t.join();
			} catch (final InterruptedException e) {
				ExceptionHandler.logger(e, log);
				Thread.currentThread().interrupt(); // propagate

				return null;
			}
		}
	}

	/**
	 * Get the value produced by the worker thread, or null if it hasn't been
	 * constructed yet.
	 * 
	 * @return value
	 */
	protected synchronized Object getValue() {
		return value;
	}

	/**
	 * A new method that interrupts the worker thread. Call this method to force
	 * the worker to stop what it's doing.
	 */
	public void interrupt() {
		final Thread t = threadVar.get();
		if (t != null) {
			t.interrupt();
		}
		threadVar.clear();
	}

	/**
	 * Set the value produced by worker thread
	 * 
	 * @param x
	 *          set value to x
	 */
	protected synchronized void setValue(final Object x) {
		value = x;
	}

	/**
	 * Start the worker thread.
	 */
	public void start() {
		final Thread t = threadVar.get();
		if (t != null) {
			t.start();
		}
	}
}
