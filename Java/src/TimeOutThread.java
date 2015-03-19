
public class TimeOutThread extends Thread {
	
	private long time;
	private int timeOut;
	private boolean progress;
	
	public void run(){
		while(true){
			if(progress){
				if(System.currentTimeMillis() - time > timeOut){
					MainAction.window1.println("[NOC]: Connection lost");
					pause();
					MainAction.pauseSystem();
				}
			}
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void pause(){
		progress = false;
	}
	
	public void timeOut(int timeOut){
		this.timeOut = timeOut;
		refreshTime();
		progress = true;
	}
	
	public void refreshTime(){
		time = System.currentTimeMillis();
	}

}
