public class InitComm{

	private final int MAX_ID = 15;
	private final int MIN_ID = 10;
	
	private final int ID_MASK = 0xF0;
	private final int MSG_MASK = 0x0F;
	
	private int IDvisitor, inputData, NOC_ID;
	
	public InitComm(int MyID){
		NOC_ID = MyID;
	}
	
	public void run(){
		inputData = 0;
		MainAction.window1.println("[NOC]: ID_NOC = " + NOC_ID);
		while(true){
			if((inputData = MainAction.arduino.readData()) != -1){
				if(identify())
					if(startComm())
						break;
			}
		}
	}
			
	private boolean identify(){
		
		if((inputData & MSG_MASK) != Comm.BEACON){
			return false;
		}
		
		int ID = ((inputData & ID_MASK) >> 4);
		MainAction.window1.println("[NOC]: IDvisitor = " + ID);
		
		if(isIDValid(ID)){
			IDvisitor = ID;
			MainAction.window1.println("[NOC]: Recieved BEACON message from " + IDvisitor);
			return true;
		}
		
		return false;
	}
		
	private boolean startComm(){
		
		if(IDvisitor == NOC_ID){
			if(isIDValid(NOC_ID + 1))
				NOC_ID = NOC_ID + 1;
			else NOC_ID = NOC_ID - 1;
			MainAction.window1.println("[NOC]: Changed NOC_ID to " + NOC_ID + ": match with visitor");
		}
		
		try {
			MainAction.OCOMMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainAction.arduino.sendData((short) (Comm.I_AM + (IDvisitor << 4)));
		MainAction.arduino.sendData((short) NOC_ID);

		MainAction.window1.println("[NOC]: Sent I_AM message to " + IDvisitor);
		
		if(!waitFor(Comm.ACK + (NOC_ID << 4))) return false;
		MainAction.window1.println("[NOC]: Received ACK of I_AM message");

		MainAction.arduino.sendData((short) (Comm.ACK + (IDvisitor << 4)));
		MainAction.OCOMMSemaphore.release();
		MainAction.window1.println("[NOC]: Sent ACK to " + IDvisitor);
		
		MainAction.window1.println("[NOC]: ESTABLISHED COMMUNICATION WITH " + IDvisitor);
		
		return true;
	}	

	private boolean isIDValid(int ID) {
		if(ID > MAX_ID || ID < MIN_ID)
			return false;
		return true;
	}

	private boolean waitFor(int message){
		
		long timeOut = System.currentTimeMillis();
		
		while((System.currentTimeMillis() - timeOut) < 5000){
			if((inputData = MainAction.arduino.readData()) != -1)
				if(inputData == message){
					return true;
				}
		}
		return false;
	}
	
	public int getIDvisitor(){
		return IDvisitor;
	}
	
	public int getNOC_ID(){
		return NOC_ID;
	}
	
}