#ifndef Telemetry_h
#define Telemetry_h

/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "BXInitCOMM.h"
#include "BXMoveMode.h"
#include "PIDManager.h"
#include "TimeOut.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define TELEMETRY         0x0E
#define TM_PETITION       0x0F
#define TEL_SPEED         0x00
#define TEL_BYTEINFO      0x02
#define TEL_TIMEOUT       0x03
#define TEL_PITCH         0x04
#define TEL_ROLL          0x05
#define TEL_X             0x06
#define TEL_Y             0x07
#define TEL_Z             0x08
#define TEL_DIFFMOTOR     0x09
#define TEL_PIDVALUES     0x0A
#define TEL_ALPHA_ACCEL   0x0B
#define TEL_OFFSETS       0x0C
#define TEL_DELTAPITCH    0x0D
#define TEL_DELTAROLL     0x0E
#define TEL_OLEVELS       0x0F
#define TEL_ALPHA_GYRO    0x10
#define TEL_ALPHA_DEG     0x11
#define TEL_STILLLEVELS   0x12
#define TEL_MOTORSPOWER   0x13
#define TEL_MOTOROFFSETS  0x14
#define ENDOFPCK          0x1B
#define ENDOFTM           0x05
#define TELEMETRYTIME       12


/*****************************************/
/************** FUNCTIONS ****************/
/*****************************************/

void startTM();
void checkTelemetry();
void pendPIDTM();
void pendAlphaTM();
void pendAccelOffTM();
void pendGyroStillTM();
void pendMotorsPowerTM();
void pendMotorOffsetsTM();
void cancelTM();

#endif
