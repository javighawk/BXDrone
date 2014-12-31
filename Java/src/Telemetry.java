import java.awt.Color;


public class Telemetry{
	
	public final static byte TEL_SPEED  	  = 0x00;
	public final static byte TEL_PITCH 		  = 0x04;
	public final static byte TEL_ROLL  		  = 0x05;
	public final static byte TEL_X     	 	  = 0x06;
	public final static byte TEL_Y   		  = 0x07;
	public final static byte TEL_Z		      = 0x08;
	public final static byte TEL_DIFFMOTOR    = 0x09;
	public final static byte TEL_PIDVALUES	  = 0x0A;
	public final static byte TEL_ALPHA_ACCEL  = 0x0B;
	public final static byte TEL_OFFSETS	  = 0x0C;
	public final static byte TEL_0LEVELS	  = 0x0F;
	public final static byte TEL_DELTAPITCH	  = 0x0D;
	public final static byte TEL_DELTAROLL    = 0x0E;
	public final static byte TEL_ALPHA_GYRO	  = 0x10;
	public final static byte TEL_ALPHA_DEG	  = 0x11;
	public final static byte TEL_STILLLEVELS  = 0x12;
	public final static byte TEL_MOTORSPOWER  = 0x13;
	public final static byte TEL_MOTOROFFSETS = 0x14;
	public final static byte ENDOFTM		  = 0x05;
	
	public static void readTelemetry(){
		int inputTelemetry, m1=0, m2=0, m3=0, m4=0;
		
		while( (inputTelemetry = MainAction.arduino.readData()) != 
			 (  Telemetry.ENDOFTM + (MainAction.startComm.getNOC_ID() << 4))){
			
			switch(inputTelemetry){
				
				case Telemetry.TEL_SPEED:
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m1 = (m1 << 8) + inputTelemetry;
					MainAction.window1.labelM1v.setText(Integer.toString(m1));
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m2 = (m2 << 8) + inputTelemetry;
					MainAction.window1.labelM2v.setText(Integer.toString(m2));
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m3 = (m3 << 8) + inputTelemetry;
					MainAction.window1.labelM3v.setText(Integer.toString(m3));
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m4 = (m4 << 8) + inputTelemetry;
					MainAction.window1.labelM4v.setText(Integer.toString(m4));
					
					if( m1==0 && m2==0 && m3==0 & m4==0 ){
						MainAction.window1.enablePIDCommand();
						MainAction.window1.btnTestM1.setEnabled(true);
						MainAction.window1.btnTestM2.setEnabled(true);
						MainAction.window1.btnTestM3.setEnabled(true);
						MainAction.window1.btnTestM4.setEnabled(true);
						MainAction.window1.sliderAlphaAccel.setEnabled(true);
						MainAction.window1.sliderAlphaGyro.setEnabled(true);
						MainAction.window1.sliderAlphaDeg.setEnabled(true);
						MainAction.window1.sliderOffX.setEnabled(true);
						MainAction.window1.sliderOffY.setEnabled(true);
						MainAction.window1.sliderOffZ.setEnabled(true);
						MainAction.window1.btnDefaultOffsets.setEnabled(true);
						MainAction.window1.btnSetLevel.setEnabled(true);
						MainAction.window1.btnSetGround.setEnabled(true);
						MainAction.window1.btnSetStillLevel.setEnabled(true);
						MainAction.window1.btnSwitchM1.setEnabled(true);
						MainAction.window1.btnSwitchM2.setEnabled(true);
						MainAction.window1.btnSwitchM3.setEnabled(true);
						MainAction.window1.btnSwitchM4.setEnabled(true);
						MainAction.window1.sldOffM1.setEnabled(true);
						MainAction.window1.sldOffM2.setEnabled(true);
						MainAction.window1.sldOffM3.setEnabled(true);
						MainAction.window1.sldOffM4.setEnabled(true);
					} else {
						MainAction.window1.disablePIDCommand();
						MainAction.window1.btnTestM1.setEnabled(false);
						MainAction.window1.btnTestM2.setEnabled(false);
						MainAction.window1.btnTestM3.setEnabled(false);
						MainAction.window1.btnTestM4.setEnabled(false);
						MainAction.window1.sliderAlphaAccel.setEnabled(false);
						MainAction.window1.sliderAlphaGyro.setEnabled(false);
						MainAction.window1.sliderAlphaDeg.setEnabled(false);
						MainAction.window1.sliderOffX.setEnabled(false);
						MainAction.window1.sliderOffY.setEnabled(false);
						MainAction.window1.sliderOffZ.setEnabled(false);
						MainAction.window1.btnDefaultOffsets.setEnabled(false);
						MainAction.window1.btnSetLevel.setEnabled(false);
						MainAction.window1.btnSetGround.setEnabled(false);
						MainAction.window1.btnSetStillLevel.setEnabled(false);
						MainAction.window1.btnSwitchM1.setEnabled(false);
						MainAction.window1.btnSwitchM2.setEnabled(false);
						MainAction.window1.btnSwitchM3.setEnabled(false);
						MainAction.window1.btnSwitchM4.setEnabled(false);
						MainAction.window1.sldOffM1.setEnabled(false);
						MainAction.window1.sldOffM2.setEnabled(false);
						MainAction.window1.sldOffM3.setEnabled(false);
						MainAction.window1.sldOffM4.setEnabled(false);
					}
					
					break;
					
				
				case Telemetry.TEL_DIFFMOTOR:
					String dm1 = new String();	
						while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
							if(inputTelemetry != -1)
								dm1 += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelDiffM1.setText(dm1);
				
					String dm2 = new String();	
						while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
							if(inputTelemetry != -1)
								dm2 += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelDiffM2.setText(dm2);
					
					String dm3 = new String();	
						while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
							if(inputTelemetry != -1)
								dm3 += String.valueOf((char)inputTelemetry);
						MainAction.window1.labelDiffM3.setText(dm3);
							
					String dm4 = new String();	
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							dm4 += String.valueOf((char)inputTelemetry);
						MainAction.window1.labelDiffM4.setText(dm4);
					
					break;	
					
					
					
					
					
				case Telemetry.TEL_PITCH:
					String pitch = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							pitch += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelPitch.setText(pitch + " Rad");
					
					break;
					
				case Telemetry.TEL_ROLL:
					String roll = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							roll += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelRoll.setText(roll + " Rad");
					
					break;
					
				case Telemetry.TEL_X:
					String X = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							X += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelX.setText(X + " G");
					
					break;
					
				case Telemetry.TEL_Y:
					String Y = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							Y += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelY.setText(Y + " G");
					
					break;
					
				case Telemetry.TEL_Z:
					String Z = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							Z += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelZ.setText(Z + " G");
					
					break;
					
					
					
				case Telemetry.TEL_PIDVALUES:
					
					int[][] PIDKv = new int[2][3];
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							if(inputTelemetry == 0){
								MainAction.window1.lblPID.setText("OFF");
								MainAction.window1.lblPID.setForeground(Color.RED);
							}else if(inputTelemetry == 1){
								MainAction.window1.lblPID.setText("ON");
								MainAction.window1.lblPID.setForeground(Color.GREEN);
							}

					for(int i=0 ; i<2; i++){
						for(int j=0 ; j<3 ; j++){
							while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
								if(inputTelemetry != -1)
									PIDKv[i][j] = (PIDKv[i][j] << 8) + inputTelemetry;						
						}
					}
					
					MainAction.window1.lblP0v.setText(Integer.toString(PIDKv[0][0]));
					MainAction.window1.lblI0v.setText(Integer.toString(PIDKv[0][1]));
					MainAction.window1.lblD0v.setText(Integer.toString(PIDKv[0][2]));
					MainAction.window1.lblP1v.setText(Integer.toString(PIDKv[1][0]));
					MainAction.window1.lblI1v.setText(Integer.toString(PIDKv[1][1]));
					MainAction.window1.lblD1v.setText(Integer.toString(PIDKv[1][2]));
					
					MainAction.window1.sliderP0.setValue(PIDKv[0][0]);
					MainAction.window1.sliderI0.setValue(PIDKv[0][1]);
					MainAction.window1.sliderD0.setValue(PIDKv[0][2]);
					MainAction.window1.sliderP1.setValue(PIDKv[1][0]);
					MainAction.window1.sliderI1.setValue(PIDKv[1][1]);
					MainAction.window1.sliderD1.setValue(PIDKv[1][2]);
					
					break;
					
				case Telemetry.TEL_ALPHA_ACCEL:
					String alphaAccel = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							alphaAccel += String.valueOf((char)inputTelemetry);
					MainAction.window1.lblAlphaAccel.setText(alphaAccel);
					break;
					
				case Telemetry.TEL_ALPHA_GYRO:
					String alphaGyro = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							alphaGyro += String.valueOf((char)inputTelemetry);
					MainAction.window1.lblAlphaGyro.setText(alphaGyro);
					break;
					
				case Telemetry.TEL_ALPHA_DEG:
					String alphaDeg = new String();
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							alphaDeg += String.valueOf((char)inputTelemetry);
					MainAction.window1.lblAlphaDeg.setText(alphaDeg);
					break;
					
					
					
				case Telemetry.TEL_OFFSETS:
					byte offX=0, offY=0, offZ=0;
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							offX = (byte) ((offX << 8) + inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							offY = (byte) ((offY << 8) + inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							offZ = (byte) ((offZ << 8) + inputTelemetry);		
					
					MainAction.window1.lblOffX.setText(Integer.toString(offX));
					MainAction.window1.lblOffY.setText(Integer.toString(offY));
					MainAction.window1.lblOffZ.setText(Integer.toString(offZ));
					MainAction.window1.sliderOffX.setValue(offX);
					MainAction.window1.sliderOffY.setValue(offY);
					MainAction.window1.sliderOffZ.setValue(offZ);
					break;
					
				case Telemetry.TEL_0LEVELS:
					String Opitch = new String();
					String Oroll = new String();
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							Opitch += String.valueOf((char)inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							Oroll += String.valueOf((char)inputTelemetry);
					int Op = (int)(MainAction.strToDouble(Opitch) * 180/3.14159);
					int Or = (int)(MainAction.strToDouble(Oroll) * 180/3.14159);
					MainAction.window1.lbl0Pitch.setText(Op + " Deg");
					MainAction.window1.lbl0Roll.setText(Or + " Deg");		
					break;
					
				case Telemetry.TEL_DELTAPITCH:
					String dPitch = new String();
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							dPitch += String.valueOf((char)inputTelemetry);
					//int dP = (int)(MainAction.strToDouble(dPitch) * 180/3.14159);
					MainAction.window1.lblDeltaPitch.setText(dPitch + " Rad/s");
//					byte dPitch = 0;
//					
//					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
//						if(inputTelemetry != -1)
//							dPitch = (byte) ((dPitch << 8) + inputTelemetry);
//					MainAction.window1.lblDeltaPitch.setText(Integer.toString(dPitch));
					break;
					
				case Telemetry.TEL_DELTAROLL:
					String dRoll = new String();
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							dRoll += String.valueOf((char)inputTelemetry);
					//int dR = (int)(MainAction.strToDouble(dRoll) * 180/3.14159);
					MainAction.window1.lblDeltaRoll.setText(dRoll + " Rad/s");
//					byte dRoll = 0;
//					
//					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
//						if(inputTelemetry != -1)
//							dRoll = (byte) ((dRoll << 8) + inputTelemetry);
//					MainAction.window1.lblDeltaRoll.setText(Integer.toString(dRoll));
					break;
					
					
					
				case Telemetry.TEL_STILLLEVELS:
					String XMed = new String();
					String ZMed = new String();
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							XMed += String.valueOf((char)inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							ZMed += String.valueOf((char)inputTelemetry);
					
					MainAction.window1.lblGyroXMed.setText(XMed);
					MainAction.window1.lblGyroZMed.setText(ZMed);
					break;
					
				
				case Telemetry.TEL_MOTORSPOWER:
					
					int m1p=0, m2p=0, m3p=0, m4p=0;
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m1p = inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m2p = inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m3p = inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m4p = inputTelemetry;
					
					if( m1p == 0 ){
						MainAction.window1.lblM1Pow.setText("OFF");
						MainAction.window1.lblM1Pow.setForeground(Color.RED);
					} else {
						MainAction.window1.lblM1Pow.setText("ON");
						MainAction.window1.lblM1Pow.setForeground(Color.GREEN);
					}
					if( m2p == 0 ){
						MainAction.window1.lblM2Pow.setText("OFF");
						MainAction.window1.lblM2Pow.setForeground(Color.RED);
					} else {
						MainAction.window1.lblM2Pow.setText("ON");
						MainAction.window1.lblM2Pow.setForeground(Color.GREEN);
					}
					if( m3p == 0 ){
						MainAction.window1.lblM3Pow.setText("OFF");
						MainAction.window1.lblM3Pow.setForeground(Color.RED);
					} else {
						MainAction.window1.lblM3Pow.setText("ON");
						MainAction.window1.lblM3Pow.setForeground(Color.GREEN);
					}
					if( m4p == 0 ){
						MainAction.window1.lblM4Pow.setText("OFF");
						MainAction.window1.lblM4Pow.setForeground(Color.RED);
					} else {
						MainAction.window1.lblM4Pow.setText("ON");
						MainAction.window1.lblM4Pow.setForeground(Color.GREEN);
					}
					break;
					
				
				case Telemetry.TEL_MOTOROFFSETS:
					
					byte m1off=0, m2off=0, m3off=0, m4off=0;
					
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m1off = (byte) inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m2off = (byte) inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m3off = (byte) inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != MainAction.arduino.ENDOFPCK)
						if(inputTelemetry != -1)
							m4off = (byte) inputTelemetry;
					
					MainAction.window1.lblOffM1.setText(Integer.toString(m1off));
					MainAction.window1.lblOffM2.setText(Integer.toString(m2off));
					MainAction.window1.lblOffM3.setText(Integer.toString(m3off));
					MainAction.window1.lblOffM4.setText(Integer.toString(m4off));
					MainAction.window1.sldOffM1.setValue(m1off);
					MainAction.window1.sldOffM2.setValue(m2off);
					MainAction.window1.sldOffM3.setValue(m3off);
					MainAction.window1.sldOffM4.setValue(m4off);
					break;
			}
			
		}
				
		
		
		
		
		
		
		
	}
	
}
