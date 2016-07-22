import java.io.IOException;

public class DroneController extends Thread{  
	
	/* Flow-control flag */
	private boolean progress = true;
	
	/* Communication with speed controller */	
	private SpeedCtrlCOMM speedctrl = new SpeedCtrlCOMM();
    
	/*
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		speedctrl.initialize(MainAction.GUI.getControllerCOMPort());
		int speed;
		while(true){
			if(progress){
				try{
					if( (speed = speedctrl.readData()) != -1 ){
						MainAction.arduino.sendMove(COMM.MOVE_Z_MODE, speed);
					}				
				} catch (IOException e){
					MainAction.showInputIOException(e);
				}
			}
		}
	}
}