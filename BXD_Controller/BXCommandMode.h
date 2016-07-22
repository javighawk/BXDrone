#ifndef BXCommandMode_h
#define BXCommandMode_h


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "Arduino.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define CMD_PIDKV             0x40
#define CMD_PIDSWITCH         0x41
#define CMD_MOTORSWITCH       0x42
#define CMD_STOPMOTORS        0x43
#define CMD_TESTMOTORS        0x44


/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

void CMD_run();	


#endif
