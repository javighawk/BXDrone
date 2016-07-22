import java.io.IOException;

public class InputCommThread extends Thread {
	
	/* Flag to determine thread flow */
	private boolean progress = true;
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		MainAction.timeOut.timeOut();
		int inputData = 0;
		
		while(true){
			try{
				if(progress){
					if((inputData = MainAction.arduino.readData()) != -1){
						
						// Update time out
						MainAction.timeOut.refreshTime();
						
						// Telemetry petition
						if( inputData == ArduinoCOMM.TM_PETITION ){
							MainAction.OutputCOMMLock.lock();
							MainAction.outputS.push(ArduinoCOMM.TM_CONFIRMATION);
							MainAction.OutputCOMMLock.unlock();
						}
						
						// Telemetry incoming
						else if( inputData == ArduinoCOMM.TELEMETRY ){
							Telemetry.readTelemetry();
							continue;
						}
						
						// Unidentified data
						else{
							MainAction.GUI.println("[Server]: Input Data not identified: 0x" + Integer.toHexString(inputData));
						}
					}
				}
			} catch (IOException e) {
				MainAction.showInputIOException(e);
			}
		}
	}
	
	
	/*
	 * Pause thread
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
}