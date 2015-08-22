#ifndef TimeOut_h
#define TimeOut_h

/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "BXInitCOMM.h"
#include "BXMoveMode.h"

/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define TIMEOUT_EXPIRED       2000


/*****************************************/
/**************** FUNCTIONS **************/
/*****************************************/

void feedTimeOut();
int checkTimeOut();
unsigned long getCurrentTimeOut();

#endif
