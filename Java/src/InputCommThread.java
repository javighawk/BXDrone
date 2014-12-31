public class InputCommThread extends Thread {
	
	private static boolean wait = false;
	private boolean progress;
	private boolean acceptTM = false;
	
	private TimeOutThread timeOut = new TimeOutThread();
	
	private final int LF_TIME_OUT = 3000;
	
	public void run(){
		timeOut.start();
		timeOut.timeOut(LF_TIME_OUT);
		progress = true;
		int inputData = 0;
		
		while(true){
			if(progress){
				if((inputData = MainAction.arduino.readData()) != -1){
					
					timeOut.refreshTime();
					
					if(inputData == (MainAction.arduino.EOT + (MainAction.startComm.getNOC_ID() << 4))){
						MainAction.outputS.pause();
						setStandBy(true);
						wait = false;
						continue;
					}
				
					if(inputData == (MainAction.arduino.TM_PETITION + (MainAction.startComm.getNOC_ID() << 4))){
						
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
					
					if(inputData == (MainAction.arduino.TELEMETRY + (MainAction.startComm.getNOC_ID() << 4))){
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
				
					if(inputData != 13){
						MainAction.window1.print(Character.toString((char)inputData));
						System.out.println("InputDataNotIdentified: 0x" + Integer.toHexString(inputData));
						continue;
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
	
	public void waitForEOT(boolean waitEOT){
		wait = waitEOT;
	}
	
	public boolean isWaiting(){
		return wait;
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