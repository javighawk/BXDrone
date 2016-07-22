#ifndef PIDManager_h
#define PIDManager_h


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "Arduino.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define N_PID_CONTROLLERS         6
#define PID_PITCH_RATE            0
#define PID_ROLL_RATE             1
#define PID_YAW_RATE              2
#define PID_PITCH_STAB            3
#define PID_ROLL_STAB             4
#define PID_YAW_STAB              5


/*****************************************/
/*************** FUNCTIONS ***************/
/*****************************************/

void PID_init();
void PID_compute();
void PID_setKv( int PIDCtrl_id, int param, double value );
double PID_getKv( int PIDCtrl_id, int param );
void PID_switch();
uint8_t PID_getSwitch();
void PID_setDesired(int PIDCtrl_id, double ds);



#endif
