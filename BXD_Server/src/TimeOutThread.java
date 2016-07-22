public class TimeOutThread extends Thread {
	
	/* Keeps the last time anything was received */
	private long time;
	
	/* Time out in milliseconds */
	private int timeOut = 3500;
	
	/* Flow-control flag */
	private boolean progress;
	
	
	/*
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		while(true){
			if(progress){
				if(System.currentTimeMillis() - time > timeOut){
					MainAction.GUI.println("[Server]: Connection lost");
					MainAction.pauseSystem();
				}
			}
		}
	}
	
	
	/*
	 * Pause the thread
	 */
	public void pause(){
		progress = false;
	}
	
	
	/*
	 * Restarts the time out counter
	 */
	public void timeOut(){
		refreshTime();
		progress = true;
	}
	
	
	/*
	 * Updates the time variable
	 */
	public void refreshTime(){
		time = System.currentTimeMillis();
	}
}