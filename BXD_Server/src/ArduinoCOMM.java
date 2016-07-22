import java.io.IOException;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

public class ArduinoCOMM extends COMM{

    
	/*
	 * Constructor
	 */
	public ArduinoCOMM(){
        Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
        while(portList.hasMoreElements()){
             CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
             MainAction.GUI.addCOMPort(portId.getName());
        }
	}
	
	
    /*
     * Create the command to move the drone along the Z axis
     * 
     * @param mode The mode we want to set
     * @param speed The speed we want to set the propellers to
     */
    public void sendMove( short mode, int speed ){
    	// Initial asserts
    	if( mode != MOVE_Z_MODE && 
    		mode != MOVE_PR_MODE &&
    		mode != MOVE_YAW_MODE )
    		return;
    	
    	// Get speed (>0)
    	speed = Math.max(speed, 0);
    	short spd1 = (short) ((speed >> 8) & 0xFF);
    	short spd2 = (short) (speed & 0xFF);
    	
    	// Send mode and speed
   		MainAction.OutputCOMMLock.lock();
   		MainAction.outputS.push( mode );
   		MainAction.outputS.push( spd1 );
   		MainAction.outputS.push( spd2 );
       	MainAction.OutputCOMMLock.unlock();
    }
    
    
    /*
     * Send PID switch command
     */
    public void sendPIDKv( short PIDCtrl_id, short param, int value ){
    	
    	// Send command and args
   		MainAction.OutputCOMMLock.lock();
   		MainAction.outputS.push( COMMAND_MODE );
   		MainAction.outputS.push( CMD_PIDKV );
   		MainAction.outputS.push( PIDCtrl_id );
   		MainAction.outputS.push( param );
   		MainAction.outputS.push( value >> 8 );
   		MainAction.outputS.push( (short) value );
       	MainAction.OutputCOMMLock.unlock();
    }
    
    
    /*
     * Send Motor switch command
     * 
     * @param motor The motor id to be switched
     */
    public void sendMotorSwitch( short motor ){
    	
    	// Send command and args
   		MainAction.OutputCOMMLock.lock();
   		MainAction.outputS.push( COMMAND_MODE );
   		MainAction.outputS.push( CMD_MOTORSWITCH );
   		MainAction.outputS.push( motor );
       	MainAction.OutputCOMMLock.unlock();
    }
    
    
    /*
     * Send PID parameter
     */
    public void sendPIDSwitch(){
    	
    	// Send command and args
   		MainAction.OutputCOMMLock.lock();
   		MainAction.outputS.push( COMMAND_MODE );
   		MainAction.outputS.push( CMD_PIDSWITCH );
       	MainAction.OutputCOMMLock.unlock();
    }
    
    
    /*
     * Send Stop motors command
     */
    public void sendStopMotors(){
    	
    	// Send command and args
   		MainAction.OutputCOMMLock.lock();
   		MainAction.outputS.push( COMMAND_MODE );
   		MainAction.outputS.push( CMD_STOPMOTORS );
       	MainAction.OutputCOMMLock.unlock();
    }
    
    
    /*
     * Send test motors command
     */
    public void sendTestMotors(){
    	
    	// Send command and args
   		MainAction.OutputCOMMLock.lock();
   		MainAction.outputS.push( COMMAND_MODE );
   		MainAction.outputS.push( CMD_TESTMOTORS );
       	MainAction.OutputCOMMLock.unlock();
    }
    
    
    /*
     * Send data to Arduino over Serial
     * 
     * @param data Data to send
     * @throws IOException if communication is cut
     */
    public void sendData(int data) throws IOException{
		output.write(data);
		MainAction.GUI.printOutputData(data);
    }
	
	
    /*
     * Read data from Arduino over Serial
     * 
     * @return Data read. -1 if nothing available
     * @throws IOException if communication is cut
     */
    public int readData() throws IOException{
    	int data = -1;
    	data = input.read();
    	
    	// If received byte is a flag, we have to discard it
    	// and take the following byte
    	if( data == ESC )
			while( (data = input.read()) < 0 );
    	
    	if( data >= 0 ) MainAction.GUI.printInputData(data);
    	return data;
    }
}
