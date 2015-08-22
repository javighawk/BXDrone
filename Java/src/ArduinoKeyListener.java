import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ArduinoKeyListener implements KeyListener{
	
    public void keyPressed ( KeyEvent e){  
    	    	
	  	switch(e.getKeyCode()){
	  		 
	  		case KeyEvent.VK_A:
	  			MainAction.arduino.setMove();
	  			MainAction.arduino.setDirection(Comm.DIR_LEFT);
	  			MainAction.arduino.setSpeed((short) MainAction.window1.slider.getValue());
	  			break;
	  		case KeyEvent.VK_D:
	  			MainAction.arduino.setMove();
	  			MainAction.arduino.setDirection(Comm.DIR_RIGHT);
	  			MainAction.arduino.setSpeed((short) MainAction.window1.slider.getValue());
	  			break;
	  		case KeyEvent.VK_W: 
	  			MainAction.arduino.setMove();
	  			MainAction.arduino.setDirection(Comm.DIR_UP);
	  			MainAction.arduino.setSpeed((short) MainAction.window1.slider.getValue());
	  			break;
	  		case KeyEvent.VK_S:
	  			MainAction.arduino.setMove();
	  			MainAction.arduino.setDirection(Comm.DIR_DOWN);
	  			MainAction.arduino.setSpeed((short) MainAction.window1.slider.getValue());
	  			break;	
	  		case KeyEvent.VK_SPACE:
	  			MainAction.arduino.setStopAllMotors();
	  			break;	  	
		   case KeyEvent.VK_BACK_SPACE:
	  			MainAction.arduino.setSpin();
	  			MainAction.arduino.setDirection(Comm.DIR_DOWN);
	  			break;
	  		default:
	  			return;
	  	  }
  	  MainAction.outputS.threadContinue();
    }  
    
    public void keyReleased ( KeyEvent e ){ 
    	
    	MainAction.outputS.pause();
    	
      switch(e.getKeyCode()){
    	
    	case KeyEvent.VK_A:
    		MainAction.arduino.clearOutputData();
  			break;
  		case KeyEvent.VK_D:
  			MainAction.arduino.clearOutputData();
  			break;
  		case KeyEvent.VK_W: 
  			MainAction.arduino.clearOutputData();
  			break;
  		case KeyEvent.VK_S:
  			MainAction.arduino.clearOutputData();
  			break; 		
  		case KeyEvent.VK_SPACE:
  			MainAction.arduino.clearOutputData();
  			break;
  		case KeyEvent.VK_BACK_SPACE:
  			MainAction.arduino.clearOutputData();
  			break;
  		default:
  			return;
    	}
      
  		MainAction.outputS.threadContinue();

    }
    
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}  

}
