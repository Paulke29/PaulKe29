
/**
 * @author PaulKe
 *
 */
public class TaskMaster {
	/** initial WorkQueue */
	private final WorkQueue tasks;
	/** The amount of pending (or unfinished) work. */
	private int pending;

	/**
	 * @param numThread number of threads needs
	 */
	TaskMaster(int numThread) {
		this.tasks = new WorkQueue(numThread);
		this.pending = 0;
	}

	/**
	 * @author PaulKe
	 *
	 */
	private class Task implements Runnable {

		@Override
		public void run() {

		}

	}
	/**
	 * Safely increments the shared pending variable.
	 */
	private synchronized void incrementPending() {
		pending++;
	}
}
