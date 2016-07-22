import java.io.IOException;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

public class SpeedCtrlCOMM extends COMM {
	
	
	/*
	 * Constructor
	 */
	public SpeedCtrlCOMM(){
        Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
        while(portList.hasMoreElements()){
             CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
             MainAction.GUI.addControllerCOMPort(portId.getName());
        }
	}
	
	
    /*
     * Read data from Speed controller over Serial
     * 
     * @return Data read. -1 if nothing available
     * @throws IOException if communication is cut
     */
	public int readData() throws IOException {
		int dataMSB, dataLSB;
		
		// Read first byte
    	dataMSB = input.read();
    	
    	// If data not available, return -1
    	if( dataMSB == -1 )
    		return -1;
    	
    	// Else wait for second byte
		while( (dataLSB = input.read()) < 0 );
    	
    	return (dataMSB << 8) + dataLSB;
	}

}
