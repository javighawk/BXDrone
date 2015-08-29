#ifndef PIDMANAGER_H
#define PIDMANAGER_H


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "IMUManager.h"
#include "BXMoveMode.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define DEFAULT_PVALUE_ANGLES   1000
#define DEFAULT_IVALUE_ANGLES      0
#define DEFAULT_DVALUE_ANGLES      0
#define DEFAULT_PVALUE_GYRO     5000
#define DEFAULT_IVALUE_GYRO        0
#define DEFAULT_DVALUE_GYRO        0


/*****************************************/
/*************** FUNCTIONS ***************/
/*****************************************/

void PIDInit();
void PIDCompute();
double getPitch();
double getRoll();
double getDiffMotor(int motor);
double getPIDValues( int roll, int pid );
void setPIDValues( int roll, int pid, double value );
void setPIDEnabled( boolean enabled );
boolean isPIDEnabled();
void PID_setDesiredAngles(int pitch, int roll);
int PID_getDesiredPitch();
int PID_getDesiredRoll();
double PID_getSetPointAX();
double PID_getSetPointAY();
double PID_getSetPointAZ();

#endif
