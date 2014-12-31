#ifndef Gyro_h
#define Gyro_h

/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "Accel.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define GYROPITCH_PIN       3
#define GYROROLL_PIN        4


/*****************************************/
/************** FUNCTIONS ****************/
/*****************************************/

void gyroInit();
void computeGyro();
double getGyroDeltaPitch();
double getGyroDeltaRoll();
double getGyroLPFAlpha();
void setGyroLPFAlpha( double k );
double getGyroXStillLevel();
double getGyroZStillLevel();
void computeStillLevel();
void startStillLevel();

#endif
