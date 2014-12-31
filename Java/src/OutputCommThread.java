
public class OutputCommThread extends Thread {
	
	private static boolean progress;
	
	public OutputCommThread(){
		super();
		progress = false;
	}
	
	public void run(){
		while(true){
			if(progress){
				try{
					MainAction.OCOMMSemaphore.acquire();
					MainAction.arduino.sendData();
					MainAction.OCOMMSemaphore.release();
					sleep(100);
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void pause(){
		progress = false;
	}
	
	public void threadContinue(){
		progress = true;
	}
	
	public void setProgress(boolean prog){
		progress = prog;
	}
	
	public boolean getProgress(){
		return progress;
	}
}
