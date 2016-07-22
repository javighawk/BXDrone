import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class OutputCommThread extends Thread {

	/* Flow-control flag */
	private boolean progress = true;
	
	/* Size of the output queue */
	private int QUEUE_SIZE = 100;
	
	/* Output queue */
	private BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(QUEUE_SIZE);
	
	
	/*
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		try{
			while(true){
				if(progress){
					// Try to take the first element on queue and send it
					try {
						int d = queue.take();
						MainAction.arduino.sendData((short) d);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e){
			MainAction.showOutputIOException(e);
		}
	}
	
	
	/*
	 * Pause the thread
	 */
	public void pause(){
		progress = false;
	}
	
	
	/*
	 * Resume thread
	 */
	public void threadContinue(){
		progress = true;
	}	
	
	
	/*
	 * Push new data into the output queue
	 * 
	 * @param d The data to be pushed into the queue
	 */
	public void push(int d){
		try {
			// Push new data into the queue if the thread is still alive
			if( getState() != Thread.State.TERMINATED )
				queue.put(d);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
