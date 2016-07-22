import java.io.*;
import java.util.concurrent.Semaphore;


public class Telemetry{
	
	/* Telemetry identifiers */
	public final static int TEL_USERSPEED	= 0x10;
	public final static int TEL_PIDSPEED	= 0x11;
	public final static int TEL_MSWITCH		= 0x12;
	public final static int TEL_TIMES		= 0x13;
	public final static int TEL_TIMELABELS	= 0x14;
	public final static int TEL_PIDKV		= 0x15;
	public final static int TEL_PIDSWITCH	= 0x16;
	public final static int TEL_YPR			= 0x17;
	public final static int TEL_GYRO		= 0x18;
	
	/* Time data */
	private static long[] times;
	
	/* Time data labels */
	private static String []labels;

	/* Motors speed data */
	private static short[] mUserSpeed, mPIDSpeed;
	
	/* Angles and gyro data */
	private static short[] ypr, gyro;

	/* Flag that determines if we are recording data */
	private static boolean isRecordingData = false;

	/* File descriptors */
	private static File timeData, mSpeedData;
	
	/* File writers */
	private static FileWriter timeWriter, mSpeedWriter;
	
	/* Buffered writers */
	private static BufferedWriter timeBW, mSpeedBW;
	
	/* Semaphore to control the Files access */
	private static Semaphore filesSemaphore = new Semaphore(1);
	
	
	/* 
	 * Read telemetry data and updates the UI 
	 *
	 * @throws IOException if COMM is lost during Telemetry reading
	 */
	public static void readTelemetry() throws IOException{
		
		// Input data
		int inputTelemetry;
		
		// Keep reading until END OF TM flag is received
		while( (inputTelemetry = MainAction.arduino.readData()) != ( ArduinoCOMM.ENDOFTM )){
			
			// Record the number of records and size of each for the incoming data
			int nRecs, sizeRecs;
			while((nRecs = MainAction.arduino.readData()) == -1);
			while((sizeRecs = MainAction.arduino.readData()) == -1);

			switch(inputTelemetry){
				// User Speed information
				case TEL_USERSPEED:
					mUserSpeed = new short[nRecs];
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							mUserSpeed[i] = (short) ((mUserSpeed[i] << 8) + inputTelemetry);
						}
					MainAction.GUI.setMUserSpeed(mUserSpeed);					
					break;

				// User Speed information
				case TEL_PIDSPEED:
					mPIDSpeed = new short[nRecs];
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							mPIDSpeed[i] = (short) ((mPIDSpeed[i] << 8) + inputTelemetry);
						}
					MainAction.GUI.setMPIDSpeed(mPIDSpeed);					
					break;
					
				// User Speed information
				case TEL_MSWITCH:
					int[] mSwitch = new int[nRecs];
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							mSwitch[i] = (mSwitch[i] << 8) + inputTelemetry;
						}
					MainAction.GUI.setMEnabled(mSwitch);					
					break;
					
				// Time records
				case TEL_TIMES:
					times = new long[nRecs];
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							times[i] = (times[i] << 8) + inputTelemetry;
						}
					MainAction.GUI.setTimeBarValues(times);
					break;
					
				// Time record labels
				case TEL_TIMELABELS:
					labels = new String[nRecs];
					for( int i=0 ; i<nRecs ; i++ ){
						labels[i] = new String();
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							if( inputTelemetry == 0 ) continue;
							labels[i] = String.valueOf((char)inputTelemetry).concat(labels[i]);
						}
					}
					MainAction.GUI.setTimeLabels(labels);
					break;
					
				// PID parameters
				case TEL_PIDKV:
					if( nRecs != 3*6 ){
						MainAction.GUI.println("[Server]: Wrong length on PIDKv TM. Expected 18, got" + Integer.toString(nRecs));
						for( int i=0 ; i<nRecs*sizeRecs ; i++ )
							MainAction.arduino.readData();
						break;
					}
					int[][] PIDKv = new int[6][3];
					for( int i=0 ; i<6 ; i++ )
						for( int p=0 ; p<3 ; p++ )
							for( int j=0 ; j<sizeRecs ; j++ ){
								while((inputTelemetry = MainAction.arduino.readData()) == -1);
								PIDKv[i][p] = ((PIDKv[i][p] << 8) + inputTelemetry);
							}
					MainAction.GUI.setPIDKv(PIDKv);					
					break;
				
				// PID status (ON/OFF)
				case TEL_PIDSWITCH:
					int[] PIDSwitch = new int[nRecs];
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							PIDSwitch[i] = (PIDSwitch[i] << 8) + inputTelemetry;
						}
					MainAction.GUI.setPIDEnabled(PIDSwitch[0]);					
					break;
				
				// Yaw, Pitch, Roll
				case TEL_YPR:
					ypr = new short[nRecs];
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							ypr[i] = (short) ((ypr[i] << 8) + inputTelemetry);
						}
					MainAction.GUI.setYPR(ypr);					
					break;
				
				// Gyroscope
				case TEL_GYRO:
					gyro = new short[nRecs];
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
							gyro[i] = (short) ((gyro[i] << 8) + inputTelemetry);
						}
					MainAction.GUI.setGyroXYZ(gyro);					
					break;

				// Unidentified Telemetry data
				default:
					MainAction.GUI.println("[Server]: Unknown telemetry ID: 0x" + Integer.toHexString(inputTelemetry));
					for( int i=0 ; i<nRecs ; i++ )
						for( int j=0 ; j<sizeRecs ; j++ ){
							while((inputTelemetry = MainAction.arduino.readData()) == -1);
						}
					break;
			}	
		}
		
		// Record data
		if( isRecordingData )
			recordCurrentData();
	}
	
	/*
	 * Creates external files to record telemetry data and initializes buffered Writers
	 */
	public static void startRecordingData(){
		
		try{
			
			// Request access
			filesSemaphore.acquire();
		
			// Initialize files
			timeData = new File("timeData.txt");
			mSpeedData = new File("mSpeedData.txt");
			
			// Create files (overwrite if they exist) and folder
			timeData.createNewFile();
			mSpeedData.createNewFile();
			
			// Initialize FileWriters
			timeWriter = new FileWriter(timeData.getAbsoluteFile());
			mSpeedWriter = new FileWriter(mSpeedData.getAbsoluteFile());
			
			// Initialize Buffered writers
			timeBW = new BufferedWriter(timeWriter);
			mSpeedBW = new BufferedWriter(mSpeedWriter);
			
			// Turn flag up
			isRecordingData = true;

			// Release access
			filesSemaphore.release();
		
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Records the current Telemetry data
	 */
	private static void recordCurrentData(){
		
		try{
			
			// Request access
			filesSemaphore.acquire();
			
			if( isRecordingData ){
			
				// Write time data
				timeBW.write(Long.toString(times[0]) + "\t");
				timeBW.write(labels[0] + ":" + Long.toString(times[1]) + "\t");
				timeBW.write(labels[1] + ":" + Long.toString(times[2]) + "\t");
				timeBW.write(labels[2] + ":" + Long.toString(times[3]));
				timeBW.newLine();
				
				// Write motors speed data
				mSpeedBW.write(Integer.toString(mUserSpeed[0]) + "\t");
				mSpeedBW.write(Integer.toString(mUserSpeed[1]) + "\t");
				mSpeedBW.write(Integer.toString(mUserSpeed[2]) + "\t");
				mSpeedBW.write(Integer.toString(mUserSpeed[3]) + "\t");
				mSpeedBW.write(Integer.toString(mPIDSpeed[0]) + "\t");
				mSpeedBW.write(Integer.toString(mPIDSpeed[1]) + "\t");
				mSpeedBW.write(Integer.toString(mPIDSpeed[2]) + "\t");
				mSpeedBW.write(Integer.toString(mPIDSpeed[3]));
				mSpeedBW.newLine();				
			}
			
			// Release access
			filesSemaphore.release();
			
		} catch (Exception e){
			e.printStackTrace();
		}	
	}
	
	/*
	 * Closes files and buffered writers
	 */
	public static void stopRecordingData(){
		
		try {
			
			// Request access
			filesSemaphore.acquire();
			
			// Close buffered writers
			timeBW.close();
			mSpeedBW.close();
			
			// Turn flag down
			isRecordingData = false;
			
			// Release access
			filesSemaphore.release();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Check if telemetry is being recorded
	 * 
	 * @return Whether TM is being recorded
	 */
	public static boolean isRecordingData(){
		return isRecordingData;
	}
}

