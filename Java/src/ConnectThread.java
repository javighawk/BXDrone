
public class ConnectThread extends Thread {
	
	private boolean progress;
	
	public void run(){
		MainAction.connect();
		progress = false;
		
		while(true)
			if(progress){
				MainAction.reconnect();
				progress = false;
			}
		
	}
	
	public void threadContinue(){
		progress = true;
	}
	
	public void pause(){
		progress = false;
	}

}
