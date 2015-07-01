#ifndef BXMoveMode_h
#define BXMoveMode_h

/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "TimeOut.h"
#include "Telemetry.h"
#include "PIDManager.h"
#include <Servo.h>


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define MOTOR_PIN1           3
#define MOTOR_PIN2           5
#define MOTOR_PIN3           6
#define MOTOR_PIN4           9
#define MAX_SIGNAL           2000
#define MIN_SIGNAL           1000
#define MAX_SPEED            ( MAX_SIGNAL - MIN_SIGNAL )
#define MEDIUM_SPEED         300
#define SPEED_DEVIATION      30


/*****************************************/
/************** FUNCTIONS ****************/
/*****************************************/

void BX_initMoveMode();
void runMoveMode();
int getMotorSpeed( int motor );
void XYMoveBot(byte speedT, byte direction);
void ZMoveBot(byte speedT, byte direction);
void setMotorSpeed( int motor, int speed );
void setAddingMotorSpeed( int motor );
void setTotalSpeed( int speed );
int getTotalSpeed();
boolean getMotorPower(int motor);
void setMotorPower(int motor, boolean power);
void setMotorOffset( byte mot, byte sp );
byte getMotorOffset( int mot );
bool isBXDMoving();

#endif
