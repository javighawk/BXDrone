#include "BXD.h"
#include "BXCOMM.h"
#include "BXCommandMode.h"
#include "BXMoveMode.h"
#include "IMUManager.h"
#include "PIDManager.h"
#include "Telemetry.h"
#include "TimeRecord.h"
#include "TimeOut.h"

/* Time Record objects */
TimeRecord tLoop("LOOP");
TimeRecord tTM("TM");
TimeRecord tIMU("IMU");

/* Array with pointers to all TimeRecord objects we have */
TimeRecord *allTimeRecord[3] = {&tLoop, &tIMU, &tTM};

/* Number of TimeRecord objects we have */
int nAllTimeRecord = sizeof(allTimeRecord)/sizeof(allTimeRecord[0]);

/*
 * Setup function
 */
void setup(){
    // Setup LEDs
    pinMode(SETUPLED_PIN,OUTPUT);
    pinMode(COMMLED_PIN,OUTPUT);
    pinMode(CONNECTIONLED_PIN,OUTPUT);
    digitalWrite(SETUPLED_PIN, LOW);
    digitalWrite(COMMLED_PIN, LOW);
    digitalWrite(CONNECTIONLED_PIN, LOW);
    
    // Setup modules
    COMM_init();
    MVM_init();
    IMU_init();
    PID_init();

    // Update timeout
    TMO_feed();
}


/*
 * Loop function
 */
void loop(){
    // Trigger time recording
    tLoop.trigger();
  
    // Compute IMU
    IMU_compute();
    
    // Compute PID
    PID_compute();

    // Check if telemetry petition needs to be sent
    TM_check();

    // Read incoming data if available
    int data = COMM_read();

    // Check if we received data
    if( data >= 0 ){
        // Check for telemetry transmission
        if( data == TM_CONFIRMATION )
            TM_start();

        // Identify the mode
        else{
            switch( data & MODE_MASK ){
                // Move mode
                case MOVE_Z_MODE:
                case MOVE_PR_MODE:
                case MOVE_YAW_MODE:
                    MVM_run( data );
                    break;

                // Command mode
                case COMMAND_MODE:
                    CMD_run();
                    break;
            }
        }

        // Feed time out
        TMO_feed();
    } else {
        // Otherwise, check if connection has timed out
        TMO_check();
    }

    // Stop time recording
    tLoop.stop();
}


/*
 * Getter
 * 
 * @returns Number of TimeRecord objects in the program
 */
int MAIN_getNAllTimeRecord(){
    return nAllTimeRecord;
}


/*
 * Getter
 * 
 * @returns Array of TimeRecord pointers
 */
TimeRecord **MAIN_getAllTimeRecord(){
    return allTimeRecord;
}
