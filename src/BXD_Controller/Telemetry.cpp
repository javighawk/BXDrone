#include "Telemetry.h"
#include "BXCOMM.h"
#include "BXMoveMode.h"
#include "IMUManager.h"
#include "PIDManager.h"

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
    // Get the time records
    unsigned long times[1] = {millis()};

    // Header
    uint8_t header[3] = {sizeof(times[0]), sizeof(times)/sizeof(times[0]), TEL_TIMES};
    
    // Send time data
    COMM_write( header, 3 );
    for( int i=0 ; i<header[1] ; i++ )
        COMM_write( (uint8_t*)&times[i], header[0] );
}


/*
 * Send the time records labels
 */
void TM_timeLabelsTelemetry(){
    /* TBD
    char labels[5][LABEL_LENGTH];
    String labels_str[5] = {tLoop.TIME_getLabel(),
                            tTelemetry.TIME_getLabel(),
                            tConnected.TIME_getLabel(),
                            tAvailable.TIME_getLabel(),
                            tInfoRead.TIME_getLabel()};

    for( int i=0 ; i<LABEL_LENGTH ; i++ )
        for( int j=0 ; j<5 ; j++ )
            if( i < labels_str[0].length() ) labels[j][i] = labels_str[j][i];
            else labels[j][i] = 0;

    uint8_t header[3] = {sizeof(labels[0]), sizeof(labels)/sizeof(labels[0]), TEL_TIMELABELS};
    COMM_write( header, 3 );
    COMM_write( (uint8_t*)labels[0], header[0] );
    COMM_write( (uint8_t*)labels[1], header[0] );
    COMM_write( (uint8_t*)labels[2], header[0] );
    COMM_write( (uint8_t*)labels[3], header[0] );
    COMM_write( (uint8_t*)labels[4], header[0] );
    */
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
