#ifndef Accel_H
#define Accel_H


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "Telemetry.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define ACCELX_PIN          0
#define ACCELY_PIN          1
#define ACCELZ_PIN          2
#define ACCEL_SLEEPPIN      7
#define ACCEL_GSELECTPIN   10
#define MAX_ACCEL         676



/*****************************************/
/**************** VARIABLES **************/
/*****************************************/

const char DEFAULTXOFFSET = 0;
const char DEFAULTYOFFSET = 0;
const char DEFAULTZOFFSET = 0;



/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

void accelInit();
double getXG();
double getYG();
double getZG();
void setAccelOffsets( int axis, char value );
char getAccelOffset( int axis );
void setAccelLPFAlpha( double k );
double getAccelPitch();
double getAccelRoll();
double getAccelLPFAlpha();
void computeAccelerometer();
void computeAccelMedValue();
void startGNDLevel();

#endif
