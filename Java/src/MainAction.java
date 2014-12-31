import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

public class MainAction{

	public static OutputCommThread outputS;
	public static InputCommThread inputS;
	
	public static InterfazGrafica window1;
	public static Comm arduino;
	public static InitComm startComm;
	
	public static final Semaphore OCOMMSemaphore = new Semaphore(1);

	
	public static void main(String[] args){
		
		arduino = new Comm();
		window1 = new InterfazGrafica();
		window1.setInitial();
	}
	
	public static void showError(String errorMessage){
        JOptionPane.showMessageDialog(window1,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
	
	public static void connect(){
		window1.println("-----Connecting-----");
		arduino.initialize();
		startComm = new InitComm(13);
		startComm.run();
		window1.setConnected();
		arduino.setSpeed((short) window1.slider.getValue());
		arduino.setLightPower(window1.sliderLight.getValue());
		outputS = new OutputCommThread();
		inputS = new InputCommThread();
		window1.addKeyListener(new ArduinoKeyListener());
		outputS.start();
		inputS.start();
	}	
	
	public static void pauseSystem(){
		inputS.pause();
		outputS.pause();
		arduino.serialPort.close();
		window1.setInitial();
	}

	public static void reconnect(){
		System.out.println("Reconnect");
		arduino.initialize();
		startComm.run();
		window1.setConnected();
		arduino.setSpeed((short) window1.slider.getValue());
		arduino.setLightPower(window1.sliderLight.getValue());
		inputS.threadContinue();
		inputS.setStandBy(true);
	}
	
	public static double strToDouble( String str ){
		
		double num = 0;
		boolean decimal = false, negative = false;
		int dec = 0;
		
		for( int i=0; i<str.length(); i++ ){
			int ch = (int)str.charAt(i) - 48;
			
			if( i==0 && ch==-3 ){ negative=true; continue; }
			else if( i!=0 && ch==-3 ) return 0;
			
			if( ch == -2 && !decimal ){ decimal = true; continue; }
			else if ( ch == -2 && decimal ) return 0;
			
			if ( ch > 9 ) return 0;
			
			if( !decimal ) num = num*10 + ch;
			else{ dec--; num += ch*Math.pow(10, dec); }
		}
		
		if( negative ) return -num;
		else return num;
		
	}
}
