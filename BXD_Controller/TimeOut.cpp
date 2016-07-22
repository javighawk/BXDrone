#include "TimeOut.h"
#include "BXMoveMode.h"
#include "Telemetry.h"

/* Last time TimeOut was refreshed */
unsigned long currentTime;


/*
 * Update the timing variable
 */
void TMO_feed(){
    currentTime = millis();
}


/*
 * Perform Time out checking operation
 * 
 * @return 1 if connection has timed out. 0 otherwise.
 */
int TMO_check(){
    // Check if connection has timed out
    if( (millis() - currentTime) < TIMEOUT_EXPIRED )
        return 0;

    // Close COMM socket

    // Stop drone
    MVM_setTotalSpeed(0);

    // Reset all LED indicators

    // Perform COMM initialization

    // Reset the time out counter
    currentTime = millis();

    // Cancel any telemetry petition
    TM_cancel();

    // Set pending all TM
    TM_pendTimeLabelsTM();
    TM_pendMSwitchTM();
    TM_pendMUserSpeedTM();
    TM_pendPIDKvTM();
    TM_pendPIDSwitchTM();
    
    return 1;  
}


/*
 * Getter
 * 
 * @return Time in milliseconds since last timeout update
 */
unsigned long TMO_getCurrentTime(){
    return (millis() - currentTime);
}
