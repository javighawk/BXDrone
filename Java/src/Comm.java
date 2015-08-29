import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Enumeration;
 
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class Comm{
		
	public final static short DIR_UP = 0;
	public final static short DIR_DOWN = 16;
	public final static short DIR_LEFT = 32;
	public final static short DIR_RIGHT = 48;
	
	public final static short SPIN_U_L = 0;
	public final static short SPIN_D_L = 16;
	public final static short SPIN_U_R = 32;
	public final static short SPIN_D_R = 48;
	
	public final static short SETPID = 0x10;
	public final static short SWITCHPID = 0x11;
	public final static short TESTMOTORS = 0x12;
	public final static short SETALPHA = 0x13;
	public final static short SETOFFSETS = 0x40;
	public final static short SET0LEVEL = 0x41;
	public final static short STARTACCELOFFSETS = 0x42;
	public final static short STARTGYROOFFSETS = 0x43;
	public final static short SWITCHMOTOR = 0x50;
	public final static short MOTOROFFSETS = 0x51;
	public final static short STOPMOTORS = 0x52;
	
	public final static short MOVEMODE = 0;
	public final static short COMMANDMODE = 8;
	public final static short SHORTCUTCOMMAND = 32; 
	public final static short SPINMODE = 12;
	public final static short TEXTMODE = 4;
	
	public final static short DIRECTIONMASK = 207;
	public final static short SPEEDMASK = 252;
	public final static short MODEMASK = 243;
	
	public final static short I_AM = 1;
	public final static short ACK = 6;
	public final static short EOT = 4;
	public final static short BEACON = 7;
	public final static short LF = 10;
	public final static short TELEMETRY = 14;
	public final static short TM_PETITION = 15;
	public final static short ENDOFPCK = 0x1B1B;
	public final static short ESC = 0x7E;
	public static short outputData, inputData;
	private static short lastSent;		
	
	/** The streams to the port */
    public OutputStream output = null;
    public InputStream input = null;
 
    SerialPort serialPort;
    private String PORT_NAME = "COM3";
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 115200;
    
    public void initialize(){
    	 
        CommPortIdentifier portId = null;
        Enumeration<?> portEnum = CommPortIdentifier.
                                getPortIdentifiers();
 
        // iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier)
                    portEnum.nextElement();
 
            if (PORT_NAME.equals(currPortId.getName())) {
                portId = currPortId;
                break;
            }
        }
 
        if (portId == null) {
            MainAction.showError("Could not find COM port.");
            return;
        }
 
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass()
                    .getName(), TIME_OUT);
 
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            // open the streams
            output = serialPort.getOutputStream();
            input = serialPort.getInputStream();
        } catch (Exception e) {
            MainAction.showError("Connection problem");
        }
        outputData = 0;
    }
    
    //**************************************************************//
    //********************* General functions **********************//
    //**************************************************************//
    
    /**
     * Set outputData parameter to 0
     */
    public void clearOutputData(){
    	outputData = 0;
    }

    //**************************************************************//
    //********************* Movement functions *********************//
    //**************************************************************//
    
    /**
     * Sets Mode bits into Movement mode.
     */
    public void setMove(){
    	outputData = (short) (outputData & MODEMASK);
    	outputData = (short) (outputData | MOVEMODE);
    }
    
    /**
     * Sets Mode bits into Spin mode.
     */
    public void setSpin(){
    	outputData = (short) (outputData & MODEMASK);
    	outputData = (short) (outputData | SPINMODE);
    }
    
    /**
     * Sets direction
     * 
     * @param direction Direction to set
     */
    public void setDirection(int direction){
    	direction = (direction & 48);
    	outputData = (short) (outputData & DIRECTIONMASK);
    	outputData = (short) (outputData | direction);
    }
    
    /**
     * Sets speed
     * 
     * @param speed Speed to set
     */
    public void setSpeed(short speed){
    	speed = (short) (speed & 3);
		outputData = (short) (outputData & SPEEDMASK);
		outputData = (short) (outputData | speed);
    }
 
    
    //**************************************************************//
    //********************* Command functions **********************//
    //**************************************************************//
    
    public void setSHRTCMDMode(){
    	outputData = (short) (outputData & MODEMASK);
    	outputData = (short) (outputData | COMMANDMODE | SHORTCUTCOMMAND );    	
    }
    
    public void setStopAllMotors(){
    	outputData = 0;
    	setSHRTCMDMode();
    	outputData = (short) (outputData | STOPMOTORS );
    }
 
    public void sendPID(int roll, int pid){

    	try {
			if( pid==-1 && roll==-1 ){
				MainAction.OCOMMSemaphore.acquire();
				sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | SETPID));
				sendData((short)0xFF);
				sendData((short)0xFF);
				sendData((short)0xFF);
				MainAction.OCOMMSemaphore.release();
				return;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if( pid<0 || pid >2) return;
    	if( roll != 0 && roll != 1) return;
    	
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | SETPID));
    	sendData((short) roll);
    	sendData((short) pid);
    	
    	short value = 0;
    	
    	switch(pid){
    	
    		case 0:
    			if(roll == 0)
    				value = (short) MainAction.window1.sliderP0.getValue();
    			if(roll == 1)
    				value = (short) MainAction.window1.sliderP1.getValue();
    			break;
    			
    			
    		case 1:
    			if(roll == 0)
    				value = (short) MainAction.window1.sliderI0.getValue();
    			if(roll == 1)
    				value = (short) MainAction.window1.sliderI1.getValue();
    			break;
    			
    			
    		case 2:
    			if(roll == 0)
    				value = (short) MainAction.window1.sliderD0.getValue();
    			if(roll == 1)
    				value = (short) MainAction.window1.sliderD1.getValue();
    			break;
    	}
    	sendData((short) (value >> 8));
    	sendData(value);
    	MainAction.OCOMMSemaphore.release();
    }
    
    public void sendTestM(int motor){
    	
    	if( motor<1 || motor>4 ) return;
    	
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | TESTMOTORS));
    	sendData((short) motor);
    	
    	MainAction.OCOMMSemaphore.release();
    	
    }
    
    public void sendSwitchPID(){
    	
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | SWITCHPID));    	
    	MainAction.OCOMMSemaphore.release();
    }
    
    public void sendAlpha(int device){
    	int value;
    	
    	switch( device ){
    		
    		case 0: value = MainAction.window1.sliderAlphaDeg.getValue(); break;
    		case 1: value = MainAction.window1.sliderAlphaAccel.getValue(); break;
    		case 2: value = MainAction.window1.sliderAlphaGyro.getValue(); break;
    		default: return;
    	}
    	
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | SETALPHA));
    	sendData((short) device);
    	sendData((short) value);
    	
    	MainAction.OCOMMSemaphore.release();
    }
    
    public void sendOffsets(int dev, int axis){
    	
    	if( axis<-1 || axis>2 ) return;
    	int[] off = new int[3];
    	if( dev == 0 ){
    		off[0] = MainAction.window1.sliderOffX.getValue();
    		off[1] = MainAction.window1.sliderOffY.getValue();
    		off[2] = MainAction.window1.sliderOffZ.getValue();
    	} else if( dev == 1 ){
    		off[0] = MainAction.window1.sliderGOffX.getValue();
    		off[1] = MainAction.window1.sliderGOffY.getValue();
    		off[2] = MainAction.window1.sliderGOffZ.getValue();
    	} else return;
    	
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | SETOFFSETS));
		sendData((short) dev);
    	
    	if( axis == -1 ){
    		sendData((short) 0xFF);
    	} else {
    		sendData((short) axis);
    		sendData((short) ((off[axis] >> 8) & 0xFF));
    		sendData((short) (off[axis] & 0xFF));
    	}
    	MainAction.OCOMMSemaphore.release();
    }
    
    public void send0Level(){
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | SET0LEVEL));
    	
    	MainAction.OCOMMSemaphore.release();
    }
    
    public void startAccelOffsets(){
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | STARTACCELOFFSETS));
    	
    	MainAction.OCOMMSemaphore.release();
    }
    
    public void startGyroOffsets(){
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | STARTGYROOFFSETS));
    	
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	MainAction.OCOMMSemaphore.release();
    }
    
    
    public void sendSwitchMotor(int motor){
    	
    	if( motor < 1 || motor > 4 ) return;
    	
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | SWITCHMOTOR));    
    	sendData((short) motor);
    	
    	MainAction.OCOMMSemaphore.release();
    }
    
    
    public void sendMotorOffset(int motor){
    	
    	if( motor < 1 || motor > 4 ) return;
    	
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	sendData((short) (COMMANDMODE | SHORTCUTCOMMAND | MOTOROFFSETS));    
    	sendData((short) motor);
    	
    	switch(motor){
    		
    		case 1:
    			sendData((short) MainAction.window1.sldOffM1.getValue());
    			break;
    		case 2:
    			sendData((short) MainAction.window1.sldOffM2.getValue());
    			break;
    		case 3:
    			sendData((short) MainAction.window1.sldOffM3.getValue());
    			break;
    		case 4:
    			sendData((short) MainAction.window1.sldOffM4.getValue());
    			break;
    	}

    	
    	MainAction.OCOMMSemaphore.release();
    }
    
    
    
    //**************************************************************//
    //********************** Other functions ***********************//
    //**************************************************************//
    
    /**
     * Sends text to Arduino
     * 
     * @param text Text to send
     */
    public void sendText(String text){
    	try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	sendData(TEXTMODE);
    	for(int i=0; i<text.length(); i++){
    		sendData((short)text.charAt(i));
    	}
    	sendData((short) 0);
    	MainAction.OCOMMSemaphore.release();
    }
    
    /**
     * Sends outputData to Arduino
     */
    public void sendData(){
    	try {
            output.write(outputData);
        } catch (IOException e) {
            MainAction.showError("Error sending data");
            System.exit(1);
        }
    }    
    
    /**
     * Sends data to Arduino
     * 
     * @param data Data to send
     */
    public void sendData(short data){
    	try {
            output.write(data);
        } catch (IOException e) {
            MainAction.showError("Error sending data");
            System.exit(1);
        } 
    }    
    
    /**
     * Reads data from Arduino
     * 
     * @return Data read
     */
    public int readData(){
    	try {
			inputData = (short) input.read();
		} catch (IOException e) {
			MainAction.showError("Error reading data");
            System.exit(1);
		}
    	
    	/* If received byte is a flag, we have to discard it
    	 * and take the following byte */
    	if( inputData == ESC )
			try {
				while( (inputData = (short) input.read()) == -1);
			} catch (IOException e) {
				MainAction.showError("Error reading data");
	            System.exit(1);
			}
    	
    	/* Otherwise if we receive a ENDOFPCK byte, it's not data
    	 * but a real END-OF-PACKAGE byte */
		else if( inputData == (ENDOFPCK & 0xFF))
    		return ENDOFPCK;
    	
    	return inputData;
    }    
    
    /**
     * Getter function
     * 
     * @return Last data sent
     */
    public short getLastSent(){
    	return lastSent;
    }    
    
    /**
     * Sets COM port name
     * 
     * @param port Port name
     */
    public void setPortName(String port){
    		PORT_NAME = port;
    }
    
}
