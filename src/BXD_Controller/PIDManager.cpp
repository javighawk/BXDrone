#include "PIDManager.h"
#include <PID_v1.h>
#include "BXD.h"
#include "IMUManager.h"
#include "BXMoveMode.h"

/* Enabled PID variable */
uint8_t PIDSwitch = ON;

/* PID's controlling Pitch and Roll, calculated from Accelerometer readings */
PID* PIDCtrl[N_PID_CONTROLLERS];

/* PID inputs (actual values) */
double pidInputs[N_PID_CONTROLLERS];

/* PID outputs */
double pidOutputs[N_PID_CONTROLLERS];

/* PID desired values */
double pidDesired[N_PID_CONTROLLERS];

/* PID parameters */
double PIDKv[N_PID_CONTROLLERS][3];


/*
 * Initializer
 */
void PID_init(){
    // Initialize Rate PIDs
    for( int i=0 ; i<N_PID_CONTROLLERS ; i++ ){
        PIDCtrl[i] = new PID(&pidInputs[i], 
                             &pidOutputs[i], 
                             &pidDesired[i], 
                             PIDKv[i][0], 
                             PIDKv[i][1], 
                             PIDKv[i][2], 
                             DIRECT);

        // Turn on PID
        PIDCtrl[i]->SetMode(AUTOMATIC);

        // Set sample time to 1ms
        PIDCtrl[i]->SetSampleTime(1);

        // Set output within -200 and 200
        PIDCtrl[i]->SetOutputLimits(-200,200);
    }
}


/*
 * Compute PID operations
 */
void PID_compute(){
    // Get gyro last values
    pidInputs[PID_PITCH_RATE] = IMU_getGyroY();
    pidInputs[PID_ROLL_RATE] = IMU_getGyroX();
    pidInputs[PID_YAW_RATE] = IMU_getGyroZ();

    // Compute Rate PIDs
    PIDCtrl[PID_PITCH_RATE]->Compute();
    PIDCtrl[PID_ROLL_RATE]->Compute();
    PIDCtrl[PID_YAW_RATE]->Compute();

    // Apply PID speeds to motors
    MVM_setMPIDSpeed(MOTOR_FL, int( -pidOutputs[PID_PITCH_RATE] - pidOutputs[PID_ROLL_RATE]));
    MVM_setMPIDSpeed(MOTOR_FR, int( -pidOutputs[PID_PITCH_RATE] + pidOutputs[PID_ROLL_RATE]));
    MVM_setMPIDSpeed(MOTOR_BL, int(  pidOutputs[PID_PITCH_RATE] - pidOutputs[PID_ROLL_RATE]));
    MVM_setMPIDSpeed(MOTOR_BR, int(  pidOutputs[PID_PITCH_RATE] + pidOutputs[PID_ROLL_RATE]));
}


/*
 * Modifies a PID value
 * 
 * @param PIDCtrl_id The PID that will be modified
 * @param param The parameter that will be modified
 * @param value The value to be set to
 */
void PID_setKv( int PIDCtrl_id, int param, double value ){
    // Initial assert
    if( PIDCtrl_id >= N_PID_CONTROLLERS || PIDCtrl_id < 0 ) return;
    if( param >= 3 || param < 0 ) return;
    if( value < 0 ) return;

    // Assign value
    PIDKv[PIDCtrl_id][param] = value;

    // Update PID values
    PIDCtrl[PIDCtrl_id]->SetTunings(PIDKv[PIDCtrl_id][0], PIDKv[PIDCtrl_id][1], PIDKv[PIDCtrl_id][2]);
}


/*
 * Getter
 */
double PID_getKv( int PIDCtrl_id, int param ){
    // Initial assert
    if( PIDCtrl_id >= N_PID_CONTROLLERS ) return -1;
    if( param >= 3 ) return -1;

    return PIDKv[PIDCtrl_id][param];
}


/*
 * Switch PID ON/OFF
 */
void PID_switch(){
    PIDSwitch = PIDSwitch ^ ON;
    for( int i=0 ; i<N_PID_CONTROLLERS ; i++ ){
        if( PIDSwitch ) PIDCtrl[i]->SetMode(AUTOMATIC);
        else PIDCtrl[i]->SetMode(MANUAL);
    }    
}


/*
 * Getter
 * 
 * @return 1 if PID is on. 0 otherwise
 */
uint8_t PID_getSwitch(){
    return PIDSwitch;
}


/*
 * Set PID desired point
 * 
 * @param PIDCtrl_id The PID that will be modified
 * @param param The parameter that will be modified
 */
void PID_setDesired(int PIDCtrl_id, double ds){
    // Initial assert
    if( PIDCtrl_id >= N_PID_CONTROLLERS ) return;

    pidDesired[PIDCtrl_id] = ds;
}
