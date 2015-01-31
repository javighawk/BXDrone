#ifndef IMUManager_H
#define IMUManager_H


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "I2Cdev.h"
#include "MPU6050.h"

/*****************************************/
/**************** DEFINES ****************/
/*****************************************/



/*****************************************/
/**************** VARIABLES **************/
/*****************************************/


/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

void IMUInit();
void computeIMU();
double getPitch();
double getRoll();
int16_t getRawAccel();
int16_t getRawAccelY();
int16_t getRawAccelZ();
int16_t getRawGyroX();
int16_t getRawGyroY();
int16_t getRawGyroZ();
double getAccelXG();
double getAccelYG();
double getAccelZG();
double getGyroXDPS();
double getGyroYDPS();
double getGyroZDPS();
int getAccelCurrentRes();
int getGyroCurrrentRes();

#endif
