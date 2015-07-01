#ifndef BXInitCOMM_h
#define BXInitCOMM_h


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define MAX_ID         15
#define MIN_ID         10
		

/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

void BX_initCOMM();
void listen();
byte getIDvisitor();
byte getBXDID();
boolean waitFor(int message);
boolean waitFor_TimeOut(int message, int timeOutDefined);
boolean waitForIdle();
boolean waitForIdle_TimeOut(int timeOutDefined);



#endif
