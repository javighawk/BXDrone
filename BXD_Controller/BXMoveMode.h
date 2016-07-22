#ifndef BXMoveMode_h
#define BXMoveMode_h

/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "Arduino.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define DIRECTION_MASK    0x30
#define MOVE_PR_FRONT     0x00
#define MOVE_PR_BACK      0x10
#define MOVE_PR_LEFT      0x20
#define MOVE_PR_RIGHT     0x30
#define MOVE_YAW_LEFT     0x20
#define MOVE_YAW_RIGHT    0x30
#define MOTOR_FL          0
#define MOTOR_FR          1
#define MOTOR_BR          2
#define MOTOR_BL          3
#define MAX_SIGNAL        2000
#define MIN_SIGNAL        1000
#define MAX_SPEED         ( MAX_SIGNAL - MIN_SIGNAL )
#define USER_SP_THRESH    100


/*****************************************/
/************** FUNCTIONS ****************/
/*****************************************/

void MVM_init();
void MVM_run( int data );
void MVM_setMPIDSpeed( int m, int pidSp );
void MVM_setTotalSpeed( int sp );
void MVM_switchMotor( int m );
int MVM_getMUserSpeed(int m);
int MVM_getMPIDSpeed(int m);
uint8_t MVM_getMSwitch(int m);
void MVM_testMotors();


#endif
