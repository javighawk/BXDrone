#include "TimeOut.h"

/* Last time TimeOut was refreshed */
unsigned long currentTime;


void feedTimeOut(){
    currentTime = millis();
}

int checkTimeOut(){
    if( (millis() - currentTime) < TIMEOUT_EXPIRED )
        return 0;
    
    setTotalSpeed(0);
    
    digitalWrite(SERIALPIN, LOW);
    
    listen();    
    
    digitalWrite(SERIALPIN, HIGH);
    
    currentTime = millis();
    cancelTM();
    pendPIDTM();
    pendAlphaTM();
    pendAccelOffTM();
    pendGyroStillTM();
    pendMotorsPowerTM();
    pendMotorOffsetsTM();
    return 1;    
}

unsigned long getCurrentTimeOut(){
    return (millis() - currentTime);
}
