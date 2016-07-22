#ifndef Telemetry_h
#define Telemetry_h

/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "Arduino.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define TEL_USERSPEED           0x10
#define TEL_PIDSPEED            0x11
#define TEL_MSWITCH             0x12
#define TEL_TIMES               0x13
#define TEL_TIMELABELS          0x14
#define TEL_PIDKV               0x15
#define TEL_PIDSWITCH           0x16
#define TEL_YPR                 0x17
#define TEL_GYRO                0x18
#define TELEMETRYTIME             20
#define TELEMETRYTIME_THRESH    1000


/*****************************************/
/************** FUNCTIONS ****************/
/*****************************************/

void TM_start();
void TM_check();
void TM_pendTimeLabelsTM();
void TM_pendMSwitchTM();
void TM_pendMUserSpeedTM();
void TM_pendMPIDSpeedTM();
void TM_pendPIDKvTM();
void TM_pendPIDSwitchTM();
void TM_cancel();


#endif
