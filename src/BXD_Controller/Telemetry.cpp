#include "Telemetry.h"
#include "BXCOMM.h"
#include "BXMoveMode.h"
#include "IMUManager.h"
#include "PIDManager.h"
#include "TimeRecord.h"

/* Extern declarations */
int MAIN_getNAllTimeRecord();
TimeRecord **MAIN_getAllTimeRecord();
extern TimeRecord tTM;

/* Auxiliar variable to measure Telemetry period */
unsigned long timer;

/* Turn for TM */
int turn = 0;

/* Auxiliary variables */
boolean pendingTM = false;

/* Other TM Pending Boolean Variables */
boolean pendingTimeLabelsTM = true;
boolean pendingMSwtichTM = true;
boolean pendingMUserSpeedTM = true;
boolean pendingPIDKvTM = true;
boolean pendingPIDSwitchTM = true;
    

/*
 * Send motors user speed
 */
void TM_mUserSpeedTelemetry(){
    // Motors speed
    int mUserSpeed[4] = {MVM_getMUserSpeed(0),
                         MVM_getMUserSpeed(1),
                         MVM_getMUserSpeed(2),
                         MVM_getMUserSpeed(3)};
                     
    // Header      
    uint8_t header[3] = {sizeof(mUserSpeed[0]), sizeof(mUserSpeed)/sizeof(mUserSpeed[0]), TEL_USERSPEED};
    
    // Send Motors speed
    COMM_write( header, 3 );
    for( int i=0 ; i<header[1] ; i++ )
        COMM_write( (uint8_t*)&mUserSpeed[i], header[0] );
}


/*
 * Send motors PID speed
 */
void TM_mPIDSpeedTelemetry(){
    // Motors speed
    int mPIDSpeed[4] = {MVM_getMPIDSpeed(0),
                        MVM_getMPIDSpeed(1),
                        MVM_getMPIDSpeed(2),
                        MVM_getMPIDSpeed(3)};
                     
    // Header      
    uint8_t header[3] = {sizeof(mPIDSpeed[0]), sizeof(mPIDSpeed)/sizeof(mPIDSpeed[0]), TEL_PIDSPEED};
    
    // Send Motors speed
    COMM_write( header, 3 );
    for( int i=0 ; i<header[1] ; i++ )
        COMM_write( (uint8_t*)&mPIDSpeed[i], header[0] );
}


/*
 * Send motor state (ON/OFF) telemetry
 */
void TM_mSwitchTelemetry(){
    // Get the motors state
    uint8_t mSwitch[4] = {MVM_getMSwitch(0),
                          MVM_getMSwitch(1),
                          MVM_getMSwitch(2),
                          MVM_getMSwitch(3)};

    // Header
    uint8_t header[3] = {sizeof(mSwitch[0]), sizeof(mSwitch)/sizeof(mSwitch[0]), TEL_MSWITCH};
    
    // Send motors state
    COMM_write( header, 3 );
    for( int i=0 ; i<header[1] ; i++ )
        COMM_write( (uint8_t*)&mSwitch[i], header[0] );  
}


/*
 * Send time telemetry
 */
void TM_timeTelemetry(){
    // Get array of all TimeRecord pointers and how many
    TimeRecord **allTR = MAIN_getAllTimeRecord();
    int nAllTR = MAIN_getNAllTimeRecord();

    // Get current time
    unsigned long nowTime = millis();

    // Get average time of all TimeRecord objects
    unsigned long avgTimeRecord[nAllTR];
    for( int i=0 ; i<nAllTR ; i++ ){
        avgTimeRecord[i] = allTR[i]->getAvgTime();

        // Reset all maxTime for TimeRecord objects
        allTR[i]->resetMaxTRec();
    }

    // Prepare Header
    uint8_t header[3] = {sizeof(nowTime), (uint8_t)(nAllTR + 1), TEL_TIMES};
    
    // Send first the current time, and then all the TimeRecord average times
    COMM_write( header, 3 );
    COMM_write( (uint8_t*)&nowTime, header[0] );
    for( int i=0 ; i<nAllTR ; i++ )
        COMM_write( (uint8_t*)&avgTimeRecord[i], header[0] );        
}


/*
 * Send the time records labels
 */
void TM_timeLabelsTelemetry(){
    // Get array of all TimeRecord pointers and how many
    TimeRecord **allTR = MAIN_getAllTimeRecord();
    int nAllTR = MAIN_getNAllTimeRecord();
    
    // Initialize variables to extract the TimeRecord labels
    char labels[nAllTR][LABEL_LENGTH];
    String labels_str[nAllTR];

    // Iterate through all TimeRecord objects
    for( int i=0 ; i<nAllTR ; i++ ){
        // Get label
        labels_str[i] = allTR[i]->getLabel();

        // Copy the character array into a String. This will force all
        // labels to have the same length
        for( int j=0 ; j<LABEL_LENGTH ; j++ ){
            if( j < labels_str[i].length() ) labels[i][j] = labels_str[i][j];
            else labels[i][j] = 0;
        }
    }

    // Prepare header to send
    uint8_t header[3] = {sizeof(labels[0]), sizeof(labels)/sizeof(labels[0]), TEL_TIMELABELS};
    COMM_write( header, 3 );

    // Send all labels
    for( int i=0 ; i<nAllTR ; i++ )
        COMM_write( (uint8_t*)labels[i], header[0] );
}


/*
 * Send PID parameters 
 */
void TM_PIDKvTelemetry(){
    uint16_t PIDKv[N_PID_CONTROLLERS][3];

    // Get PID values. Scale them (x1000) so we can send them as integers
    for( int i=0 ; i<N_PID_CONTROLLERS ; i++ )
        for( int j=0 ; j<3 ; j++ )
            PIDKv[i][j] = (uint16_t)(PID_getKv(i,j) * 1000);

    // Header
    uint8_t header[3] = {sizeof(PIDKv[0][0]), sizeof(PIDKv)/sizeof(PIDKv[0][0]), TEL_PIDKV};

    // Send values
    COMM_write( header, 3 );
    for( int i=0 ; i<N_PID_CONTROLLERS ; i++ )
        for( int j=0 ; j<3 ; j++ )
            COMM_write( (uint8_t*)&PIDKv[i][j], header[0] );
}


/*
 * Send PID switch 
 */
void TM_PIDSwitchTelemetry(){
    // Get PID switch
    uint8_t PIDSwitch[1] = {PID_getSwitch()};

    // Header
    uint8_t header[3] = {sizeof(PIDSwitch[0]), sizeof(PIDSwitch)/sizeof(PIDSwitch[0]), TEL_PIDSWITCH};

    // Send values
    COMM_write( header, 3 );
    for( int i=0 ; i<header[1] ; i++ )
        COMM_write( (uint8_t*)&PIDSwitch[i], header[0] );
}


/*
 * Send yaw, pitch and roll
 */
void TM_YPRTelemetry(){
    int ypr[3];

    // Get yaw, pitch and roll
    ypr[0] = (int)(IMU_getYaw() * 100);
    ypr[1] = (int)(IMU_getPitch() * 100);
    ypr[2] = (int)(IMU_getRoll() * 100);

    // Header
    uint8_t header[3] = {sizeof(ypr[0]), sizeof(ypr)/sizeof(ypr[0]), TEL_YPR};

    // Send values
    COMM_write( header, 3 );
    for( int i=0 ; i<header[1] ; i++ )
        COMM_write( (uint8_t*)&ypr[i], header[0] );
}


/*
 * Send gyro values
 */
void TM_gyroTelemetry(){
    // Get gyro values
    int16_t gyro[3] = {IMU_getGyroX(),
                       IMU_getGyroY(),
                       IMU_getGyroZ()};

    // Header
    uint8_t header[3] = {sizeof(gyro[0]), sizeof(gyro)/sizeof(gyro[0]), TEL_GYRO};

    // Send values
    COMM_write( header, 3 );
    for( int i=0 ; i<header[1] ; i++ )
        COMM_write( (uint8_t*)&gyro[i], header[0] );
}


/*
 * Notifies the end of Telemetry transmission
 */
void TM_end(){
    COMM_write( ENDOFTM );
    COMM_flush();
    pendingTM = false;
}


/*
 * Start sending telemetry
 */
void TM_start(){
    if( pendingTM ){
        // Record Telemetry time
        tTM.trigger();
      
        // Send TM identifier
        COMM_write( TELEMETRY );

        // Send time telemetry
        TM_timeTelemetry();

        // Send PID speed telemetry
        TM_mPIDSpeedTelemetry();

        // Send yaw/pitch/roll telemetry
        TM_YPRTelemetry();

        // Send gyro telemetry
        TM_gyroTelemetry();

        // Check pending time labels TM
        if( pendingTimeLabelsTM ){
            TM_timeLabelsTelemetry();
            pendingTimeLabelsTM = false;
        }
        
        // Check pending motor user speed telemetry
        else if( pendingMUserSpeedTM ){
            TM_mUserSpeedTelemetry();
            pendingMUserSpeedTM = false;
        }

        // Check pending motor switch telemetry
        else if( pendingMSwtichTM ){
            TM_mSwitchTelemetry();
            pendingMSwtichTM = false;
        }

        // Check pending PID Kv telemetry
        else if( pendingPIDKvTM ){
            TM_PIDKvTelemetry();
            pendingPIDKvTM = false;
        }

        // Check pending PID switch telemetry
        else if( pendingPIDSwitchTM ){
            TM_PIDSwitchTelemetry();
            pendingPIDSwitchTM = true;
        }

        // End telemetry
        TM_end();

        // Stop recording Telemetry time
        tTM.stop();
    }
}


/*
 * Check if a telemetry petition needs to be sent
 */
void TM_check(){
    unsigned long wait = millis() - timer;

    // Send TM petition each TELEMETRYTIME period
    if( (!pendingTM) && (wait >= TELEMETRYTIME) ){
        timer += wait;
        COMM_directWrite( TM_PETITION );
        pendingTM = true;

    // Else, after TELEMETRYTIME_THRESH, cancel TM petition
    } else if( wait > TELEMETRYTIME_THRESH ){
        pendingTM = false;
        timer += wait;
    }
}


/*
 * Cancel pending telemetry
 */
void TM_cancel(){
    pendingTM = false;
}


/*
 * Pending telemetry functions
 */
void TM_pendTimeLabelsTM(){ pendingTimeLabelsTM = true; }
void TM_pendMSwitchTM(){ pendingMSwtichTM = true; }
void TM_pendMUserSpeedTM(){ pendingMUserSpeedTM = true; }
void TM_pendPIDKvTM(){ pendingPIDKvTM = true; }
void TM_pendPIDSwitchTM(){ pendingPIDSwitchTM = true; }
void TM_cancelTM(){ pendingTM = false; }
