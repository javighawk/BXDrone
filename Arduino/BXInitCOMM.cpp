#include "BXInitCOMM.h"

static byte _IDvisitor = 0;


void beacon()
{
	Serial.write(BEACON + (BXD_ID << 4));
}
		
boolean identify()
{
        if(Serial.read() != (I_AM + (BXD_ID << 4))) return false;
	if(!waitForIdle()) return false;
	_IDvisitor = Serial.read();
	if(_IDvisitor > MAX_ID || _IDvisitor < MIN_ID) return false;
	return true;
}	


boolean startComm()
{
	Serial.write(ACK + (_IDvisitor << 4));
	if(!waitFor(ACK + (BXD_ID << 4))) return false;
	return true;
}


void listen()
{
        digitalWrite(REDLEDPIN, HIGH);
	unsigned long time = 0;
	while(1){
		if((millis() - time) >= 1000){
			time = millis();
			beacon();
		}
		if(Serial.available() > 0){		
			if(!identify()) continue;		
			if(startComm()) break;
		}
	}		
        digitalWrite(REDLEDPIN, LOW);
}    

boolean waitFor(int message)
{
	unsigned long timeOut = millis();
		while((millis() - timeOut) < 5000)
			if(Serial.available() > 0)
				if(Serial.read() == message)
					return true;
	return false;
}

boolean waitFor_TimeOut(int message, int timeOutDefined)
{
	unsigned long timeOut = millis();
		while((millis() - timeOut) < timeOutDefined)
			if(Serial.available() > 0)
				if(Serial.read() == message)
					return true;
	return false;
}

boolean waitForIdle()
{
	unsigned long timeOut = millis();
		while((millis() - timeOut) < 5000)
			if(Serial.available() > 0)
				return true;
	return false;
}

boolean waitForIdle_TimeOut(int timeOutDefined)
{
	unsigned long timeOut = millis();
		while((millis() - timeOut) < timeOutDefined)
			if(Serial.available() > 0)
				return true;
	return false;
}
	
byte getIDvisitor(){
    return _IDvisitor;
}

byte getBXDID(){
    return BXD_ID;
}


	
