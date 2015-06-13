import java.awt.Color;


public class Telemetry{
	
	public final static byte TEL_SPEED  	  = 0x00;
	public final static byte TEL_PITCH 		  = 0x04;
	public final static byte TEL_ROLL  		  = 0x05;
	public final static byte TEL_ACCELX       = 0x06;
	public final static byte TEL_ACCELY   	  = 0x07;
	public final static byte TEL_ACCELZ		  = 0x08;
	public final static byte TEL_DIFFMOTOR    = 0x09;
	public final static byte TEL_PIDVALUES	  = 0x0A;
	public final static byte TEL_ALPHA_ACCEL  = 0x0B;
	public final static byte TEL_ACCELOFFSETS = 0x0C;
//	public final static byte TEL_0LEVELS	  = 0x0F;
	public final static byte TEL_GYROX	      = 0x0D;
	public final static byte TEL_GYROY        = 0x0E;
	public final static byte TEL_GYROZ		  = 0x0F;
	public final static byte TEL_ALPHA_GYRO	  = 0x10;
	public final static byte TEL_ALPHA_DEG	  = 0x11;
	public final static byte TEL_GYROOFFSETS  = 0x12;
	public final static byte TEL_MOTORSPOWER  = 0x13;
	public final static byte TEL_MOTOROFFSETS = 0x14;
	public final static byte TEL_ACCELRES	  = 0x15;
	public final static byte TEL_GYRORES      = 0x16;
	public final static int TEL_DELTA		  = 0x99;		/* TEMPORARY */
	public final static byte ENDOFTM		  = 0x05;
	
	private final static int[] accelRes = {2, 4, 8, 16},
							   gyroRes = {250, 500, 1000, 2000};
	private static int currentAccelRes = 0,
					   currentGyroRes = 0;
	
	public static void readTelemetry(){
		int inputTelemetry, m1=0, m2=0, m3=0, m4=0;
		
		while( (inputTelemetry = MainAction.arduino.readData()) != 
			 (  Telemetry.ENDOFTM + (MainAction.startComm.getNOC_ID() << 4))){
			
			switch(inputTelemetry){
				
				case Telemetry.TEL_SPEED:
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m1 = (m1 << 8) + inputTelemetry;
					MainAction.window1.labelM1v.setText(Integer.toString(m1));
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m2 = (m2 << 8) + inputTelemetry;
					MainAction.window1.labelM2v.setText(Integer.toString(m2));
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m3 = (m3 << 8) + inputTelemetry;
					MainAction.window1.labelM3v.setText(Integer.toString(m3));
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
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
						MainAction.window1.sliderGOffX.setEnabled(true);
						MainAction.window1.sliderGOffY.setEnabled(true);
						MainAction.window1.sliderGOffZ.setEnabled(true);
						MainAction.window1.btnDefaultOffsets.setEnabled(true);
						MainAction.window1.defaultGOffsets.setEnabled(true);
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
						MainAction.window1.sliderGOffX.setEnabled(false);
						MainAction.window1.sliderGOffY.setEnabled(false);
						MainAction.window1.sliderGOffZ.setEnabled(false);
						MainAction.window1.btnDefaultOffsets.setEnabled(false);
						MainAction.window1.defaultGOffsets.setEnabled(false);
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
						while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
							if(inputTelemetry != -1)
								dm1 += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelDiffM1.setText(dm1);
				
					String dm2 = new String();	
						while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
							if(inputTelemetry != -1)
								dm2 += String.valueOf((char)inputTelemetry);
					MainAction.window1.labelDiffM2.setText(dm2);
					
					String dm3 = new String();	
						while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
							if(inputTelemetry != -1)
								dm3 += String.valueOf((char)inputTelemetry);
						MainAction.window1.labelDiffM3.setText(dm3);
							
					String dm4 = new String();	
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							dm4 += String.valueOf((char)inputTelemetry);
						MainAction.window1.labelDiffM4.setText(dm4);
					
					break;	
					
				case Telemetry.TEL_PITCH:
					int pitch = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							pitch = (pitch << 8) + inputTelemetry;
					String p = Double.toString(((double)pitch)/1000) + " �";
					if (pitch >= 0) p = " " + p;
					MainAction.window1.labelPitch.setText(p);					
					break;
					
				case Telemetry.TEL_ROLL:
					int roll = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							roll = (roll << 8) + inputTelemetry;
					String r = Double.toString(((double)roll)/1000) + " �";
					if (roll >= 0) r = " " + r;
					MainAction.window1.labelRoll.setText(r);
					break;
					
				case Telemetry.TEL_ACCELX:
					short X = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							X = (short) ((X << 8) + inputTelemetry);
					String saX = String.format("%1.3f", (double)X*accelRes[currentAccelRes]/0x7FFF);
					if (X >= 0) saX = " " + saX;
					MainAction.window1.labelX.setText(saX);
					break;
					
				case Telemetry.TEL_ACCELY:
					short Y = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							Y = (short) ((Y << 8) + inputTelemetry);
					String saY = String.format("%1.3f", (double)Y*accelRes[currentAccelRes]/0x7FFF);
					if (Y >= 0) saY = " " + saY;
					MainAction.window1.labelY.setText(saY);					
					break;
					
				case Telemetry.TEL_ACCELZ:
					short Z = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							Z = (short) ((Z << 8) + inputTelemetry);
					String saZ = String.format("%1.3f", (double)Z*accelRes[currentAccelRes]/0x7FFF);
					if (Z >= 0) saZ = " " + saZ;
					MainAction.window1.labelZ.setText(saZ);					
					break;
					
					
					
				case Telemetry.TEL_PIDVALUES:
					
					int[][] PIDKv = new int[2][3];
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
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
							while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
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
					double alphaAccel = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							alphaAccel = ((double)inputTelemetry)/100;
					MainAction.window1.lblAlphaAccel.setText(Double.toString(alphaAccel));
					MainAction.window1.sliderAlphaAccel.setValue((int)(alphaAccel*100));
					break;
					
				case Telemetry.TEL_ALPHA_GYRO:
					double alphaGyro = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							alphaGyro = ((double)inputTelemetry)/100;;
					MainAction.window1.lblAlphaGyro.setText(Double.toString(alphaGyro));
					MainAction.window1.sliderAlphaGyro.setValue((int)(alphaGyro*100));
					break;
					
				case Telemetry.TEL_ALPHA_DEG:
					double alphaDeg = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							alphaDeg = ((double)inputTelemetry)/100;
					MainAction.window1.lblAlphaDeg.setText(Double.toString(alphaDeg));
					MainAction.window1.sliderAlphaDeg.setValue((int)(alphaDeg*100));
					break;
					
					
					
				case Telemetry.TEL_ACCELOFFSETS:
					short aOffX=0, aOffY=0, aOffZ=0;
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							aOffX = (short) ((aOffX << 8) + inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							aOffY = (short) ((aOffY << 8) + inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							aOffZ = (short) ((aOffZ << 8) + inputTelemetry);
					
					MainAction.window1.lblOffX.setText(Integer.toString(aOffX));
					MainAction.window1.lblOffY.setText(Integer.toString(aOffY));
					MainAction.window1.lblOffZ.setText(Integer.toString(aOffZ));
					MainAction.window1.sliderOffX.setValue(aOffX);
					MainAction.window1.sliderOffY.setValue(aOffY);
					MainAction.window1.sliderOffZ.setValue(aOffZ);
					break;
					
//				case Telemetry.TEL_0LEVELS:
//					String Opitch = new String();
//					String Oroll = new String();
//					
//					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
//						if(inputTelemetry != -1)
//							Opitch += String.valueOf((char)inputTelemetry);
//					
//					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
//						if(inputTelemetry != -1)
//							Oroll += String.valueOf((char)inputTelemetry);
//					int Op = (int)(MainAction.strToDouble(Opitch) * 180/3.14159);
//					int Or = (int)(MainAction.strToDouble(Oroll) * 180/3.14159);
//					MainAction.window1.lbl0Pitch.setText(Op + " Deg");
//					MainAction.window1.lbl0Roll.setText(Or + " Deg");		
//					break;
					
				case Telemetry.TEL_GYROX:
					short gyroX = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							gyroX = (short) ((gyroX << 8) + inputTelemetry);
					String sgX = String.format( "%1.3f" , (double)gyroX*gyroRes[currentGyroRes]/0x7FFF) + " �/s";
					if (gyroX >= 0) sgX = " " + sgX;
					MainAction.window1.labelGyroX.setText(sgX);
					break;
					
				case Telemetry.TEL_GYROY:
					short gyroY = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							gyroY = (short) ((gyroY << 8) + inputTelemetry);
					String sgY = String.format( "%1.3f" , (double)gyroY*gyroRes[currentGyroRes]/0x7FFF) + " �/s";
					if (gyroY >= 0) sgY = " " + sgY;
					MainAction.window1.labelGyroY.setText(sgY);
					break;
					
				case Telemetry.TEL_GYROZ:
					short gyroZ = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							gyroZ = (short) ((gyroZ << 8) + inputTelemetry);
					String sgZ = String.format( "%1.3f" , (double)gyroZ*gyroRes[currentGyroRes]/0x7FFF) + " �/s";
					if (gyroZ >= 0) sgZ = " " + sgZ;
					MainAction.window1.labelGyroZ.setText(sgZ);
					break;
					
					
					
				case Telemetry.TEL_GYROOFFSETS:
					short gXOff=0, gYOff=0, gZOff=0;
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							gXOff = (short) ((gXOff << 8) + inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							gYOff = (short) ((gYOff << 8) + inputTelemetry);
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							gZOff = (short) ((gZOff << 8) + inputTelemetry);
					
					MainAction.window1.lblGOffX.setText(Integer.toString(gXOff));
					MainAction.window1.lblGOffY.setText(Integer.toString(gYOff));
					MainAction.window1.lblGOffZ.setText(Integer.toString(gZOff));
					MainAction.window1.sliderGOffX.setValue(gXOff);
					MainAction.window1.sliderGOffY.setValue(gYOff);
					MainAction.window1.sliderGOffZ.setValue(gZOff);
					break;
					
				
				case Telemetry.TEL_MOTORSPOWER:
					
					int m1p=0, m2p=0, m3p=0, m4p=0;
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m1p = inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m2p = inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m3p = inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
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
					
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m1off = (byte) inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m2off = (byte) inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							m3off = (byte) inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
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
					
				case Telemetry.TEL_ACCELRES:
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							currentAccelRes = inputTelemetry;
					break;
					
				case Telemetry.TEL_GYRORES:
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							currentGyroRes = inputTelemetry;
					break;
					
				case Telemetry.TEL_DELTA:
					int delta = 0, maxdelta = 0, avgdelta = 0;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							delta = (delta << 8) + inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							maxdelta = (maxdelta << 8) + inputTelemetry;
					while((inputTelemetry = MainAction.arduino.readData()) != Comm.ENDOFPCK)
						if(inputTelemetry != -1)
							avgdelta = (avgdelta << 8) + inputTelemetry;
					System.out.println("DELTA = " + delta + "us, MAXDELTA = " + maxdelta + "us, AVGDELTA = " + avgdelta + "us");
					break;					
			}	
		}
	}
}
