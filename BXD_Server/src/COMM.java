import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class COMM {
	
	/* COMM packages */
	public final static short EOT 				= 0x04;
	public final static short ENDOFTM 			= 0x05;
	public final static short TELEMETRY 		= 0x0E;
	public final static short TM_PETITION		= 0x0F;
	public final static short ESC 				= 0x7E;
	public final static short TM_CONFIRMATION	= 0x80;
	public final static short ENDOFPCK 			= 0x88;
	
	/* Mode identifiers */
	public final static short MODE_MASK			= 0x0F;
	public final static short MOVE_Z_MODE 		= 0x00;
	public final static short MOVE_PR_MODE 		= 0x01;
	public final static short MOVE_YAW_MODE		= 0x02;
	public final static short COMMAND_MODE 		= 0x04;
		
	/* Movement direction identifiers */
	public final static short DIRECTION_MASK	= 0x30;
	public final static short MOVE_PR_FRONT 	= 0x00;
	public final static short MOVE_PR_BACK 		= 0x10;
	public final static short MOVE_PR_LEFT 		= 0x20;
	public final static short MOVE_PR_RIGHT 	= 0x30;
	public final static short MOVE_YAW_LEFT 	= 0x20;
	public final static short MOVE_YAW_RIGHT 	= 0x30;
	
	/* Command identifiers */
	public final static short CMD_PIDKV 		= 0x40;
	public final static short CMD_PIDSWITCH 	= 0x41;
	public final static short CMD_MOTORSWITCH 	= 0x42;
	public final static short CMD_STOPMOTORS 	= 0x43;
	public final static short CMD_TESTMOTORS 	= 0x44;
	
	/* The streams to the port */
    protected OutputStream output = null;
    protected InputStream input = null;
    
    /* Serial port */
    protected SerialPort serialPort;
    
    /* Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    
    /* Default bits per second for COM port. */
    private static final int DATA_RATE = 115200;
    
    
    /*
     * Initialize COMM. Open socket and wait for a client connection
     */
    public void initialize(String port_name){
   	 
        CommPortIdentifier portId = null;
        Enumeration<?> portEnum = CommPortIdentifier.
                                getPortIdentifiers();
 
        // Iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier)
                    portEnum.nextElement();
 
            if (port_name.equals(currPortId.getName())) {
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
            MainAction.showError("Connection problem with BXD");
        }
    }
    
    
    /*
     * Send data to Arduino over Serial
     * 
     * @param data Data to send
     * @throws IOException if communication is cut
     */
    public void sendData(int data) throws IOException{
		output.write(data);
    }
	
	
    /*
     * Read data from Arduino over Serial
     * 
     * @return Data read. -1 if nothing available
     * @throws IOException if communication is cut
     */
    public int readData() throws IOException{
    	return input.read();
    }
    
    
    /*
     * Close Serial port
     */
    public void closePort(){
    	serialPort.close();
    }
}
