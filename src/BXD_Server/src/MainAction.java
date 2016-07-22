import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

public class MainAction{

	/* Threads */
	public static InputCommThread inputS;
	public static OutputCommThread outputS;
	public static TimeOutThread timeOut;
	public static DroneController ctrl;
	
	/* Graphic interface */
	public static GraphicInterface GUI;
	
	/* COMM */
	public static ArduinoCOMM arduino;
	
	/* Locks */
	public static final Lock InputCOMMLock = new ReentrantLock();
	public static final Lock OutputCOMMLock = new ReentrantLock();

	
	/*
	 * Main method
	 * 
	 * @param args Arguments
	 */
	public static void main(String[] args){
		
		// Initialize GUI
		GUI = new GraphicInterface();
		GUI.setInitial();
		
		// Initialize COMM
		arduino = new ArduinoCOMM();
		
		// Initialize Controller COMM
		ctrl = new DroneController();
	}
	
	
	/*
	 * Show error on GUI
	 * 
	 * @param errorMessage String containing the error to show
	 */
	public static void showError(String errorMessage){
        JOptionPane.showMessageDialog(GUI,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
	
	
	/*
	 * Show error due to Input IOException
	 * 
	 * @param e The IOException
	 */
	public static void showInputIOException(IOException e){
		MainAction.GUI.println("[Server]: IOException caught on Input stream. Pausing system...");
		pauseSystem();
    }
	
	
	/*
	 * Show error due to Output IOException
	 * 
	 * @param e The IOException
	 */
	public static void showOutputIOException(IOException e){
		MainAction.GUI.println("[Server]: IOException caught on Output stream. Pausing system...");
		pauseSystem();
    }
	
	
	/*
	 * Connect to drone
	 */
	public static void connect(){
		
		// Wait for connection
		GUI.println("-----Connecting-----");
		arduino.initialize(GUI.getCOMPort());
		
		// Print trace
		GUI.println("[Server]: COMM initialized");
		 
		// Set GUI
		GUI.setConnected();
		
		// Initialize threads
		inputS = new InputCommThread();
		outputS = new OutputCommThread();
		timeOut = new TimeOutThread();
		
		// Print trace
		GUI.println("[Server]: Threads initialized");
		
		// Start threads
		inputS.start();
		outputS.start();
		timeOut.start();
		ctrl.start();
		
		// Print trace
		GUI.println("[Server]: Threads running");
	}	
	
	
	/*
	 * Pause the system. Execute when connection is lost
	 */
	public static void pauseSystem(){
		
		// Pause threads
		inputS.pause();
		outputS.pause();
		timeOut.pause();
		
		// Close COMM
		arduino.closePort();
		
		// Set initial window
		GUI.setInitial();
	}
}
