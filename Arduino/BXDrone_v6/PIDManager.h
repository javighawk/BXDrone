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

#define PR_PERIOD_MS        10

#define DEFAULT_PVALUE     50
#define DEFAULT_IVALUE      2
#define DEFAULT_DVALUE      3


/*****************************************/
/*************** FUNCTIONS ***************/
/*****************************************/

void PIDInit();
void PIDCompute();
void runPitchRoll();
double getPitch();
double getRoll();
double getDiffMotor(int motor);
int getPIDValues( int roll, int pid );
void setPIDValues( int roll, int pid, int value );
void setPIDEnabled( boolean enabled );
boolean isPIDEnabled();
void PIDSetCurrentOLevels();
double PIDGetPitchOLevel();
double PIDGetRollOLevel();
double getDegreesLPFAlpha();
void setDegreesLPFAlpha( double k );

#endif
