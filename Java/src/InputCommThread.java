public class InputCommThread extends Thread {
	
	private boolean progress;
	private boolean acceptTM = false;
	
	private TimeOutThread timeOut = new TimeOutThread();
	
	private final int LF_TIME_OUT = 3500;
	
	public void run(){
		timeOut.start();
		timeOut.timeOut(LF_TIME_OUT);
		progress = true;
		int inputData = 0;
		
		while(true){
			if(progress){
				if((inputData = MainAction.arduino.readData()) != -1){
					
					timeOut.refreshTime();
					
					if(inputData == (Comm.EOT + (MainAction.startComm.getNOC_ID() << 4))){
						MainAction.outputS.pause();
						setStandBy(true);
						continue;
					}
				
					if(inputData == (Comm.TM_PETITION + (MainAction.startComm.getNOC_ID() << 4))){
						
						try {
							MainAction.OCOMMSemaphore.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						MainAction.arduino.sendData((short) 0x80);
						MainAction.OCOMMSemaphore.release();
						acceptTM = true;
						
						continue;
					}
					
					if(inputData == (Comm.TELEMETRY + (MainAction.startComm.getNOC_ID() << 4))){
						if( acceptTM ){
							Telemetry.readTelemetry();
							acceptTM = false;
						} else {
							while( MainAction.arduino.readData() != Telemetry.ENDOFTM ){}
						}
						continue;
					}
				
					if(inputData == 10){
						MainAction.window1.print(Character.toString('\n'));
						continue;
					}
				
					if( inputData == (Comm.BEACON + (MainAction.startComm.getIDvisitor() << 4)) ){
						MainAction.window1.println("[NOC]: BEACON Received. Cut COMM");
						MainAction.pauseSystem();
						timeOut.pause();
						continue;
					}
					
					else{
						MainAction.window1.println("[NOC]: Input Data not identified: 0x" + Integer.toHexString(inputData));
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
	}
	
	public void pause(){
		progress = false;
	}
	
	public void threadContinue(){
		progress = true;
	}	
	
	public void setStandBy(boolean standBy){
		if(standBy)
			timeOut.timeOut(LF_TIME_OUT);
		else timeOut.pause();
	}
}