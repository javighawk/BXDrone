#include "BXCommandMode.h"
#include "BXCOMM.h"
#include "BXMoveMode.h"
#include "IMUManager.h"
#include "PIDManager.h"
#include "Telemetry.h"
#include "TimeOut.h"


/*
 * Set a PID value
 */
void CMD_PIDKv(){
    // Get command args
    int PIDCtrl_id = COMM_read_wTimeOut();
    int param = COMM_read_wTimeOut();
    int value = COMM_read_wTimeOut();
    value = (value << 8) + COMM_read_wTimeOut();

    // Set parameters
    PID_setKv(PIDCtrl_id, param, ((double)value)/100);

    // Pending telemetry
    TM_pendPIDKvTM();
}


/*
 * Switch PID ON/OFF
 */
void CMD_PIDSwitch(){
    // Switch PID
    PID_switch();

    // Pending telemetry
    TM_pendPIDSwitchTM();
}


/*
 * Test a motor
 */
void CMD_TestMotors(){
    // Test motors
    MVM_testMotors();

    // Update timeout to avoid false disconnection
    TMO_feed();
}


/*
 * Switch a motor ON/OFF
 */
void CMD_MotorSwitch(){
    // Get motor
    int m = COMM_read_wTimeOut();

    // Switch motor
    MVM_switchMotor(m);

    // Pending telemetry
    TM_pendMSwitchTM();
}


/*
 * Stop all motors immediately
 */
void CMD_StopMotors(){
    // Stop all motors
    MVM_setTotalSpeed(0);

    // Pending telemetry
    TM_pendMUserSpeedTM();
}


/*
 * Run command mode. Execeute incoming command
 * 
 * @d Pointer to data
 * @l Length of data
 */
void CMD_run(){
    // Save data
    int command = COMM_read_wTimeOut();

    // Run
    switch(command){
       case CMD_PIDKV: CMD_PIDKv(); break;
       case CMD_PIDSWITCH: CMD_PIDSwitch(); break;
       case CMD_TESTMOTORS: CMD_TestMotors(); break;
       case CMD_MOTORSWITCH: CMD_MotorSwitch(); break;
       case CMD_STOPMOTORS: CMD_StopMotors(); break;
    }
}
