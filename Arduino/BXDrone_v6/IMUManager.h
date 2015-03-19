#ifndef IMUManager_H
#define IMUManager_H


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "I2Cdev.h"
#include "MPU6050.h"
#include "Telemetry.h"

/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define DEFAULTACCELXOFFSET    0
#define DEFAULTACCELYOFFSET    0
#define DEFAULTACCELZOFFSET    0

#define DEFAULTGYROXOFFSET     0
#define DEFAULTGYROYOFFSET     0
#define DEFAULTGYROZOFFSET     0


/*****************************************/
/**************** VARIABLES **************/
/*****************************************/


/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

void IMUInit();
void computeIMU();
void computeGyroOffsets();
void computeAccelOffsets();
void setOffsets( int dev, int axis, int val );
void setDegLPFAlpha( double alpha );
void setAccelLPFAlpha( double alpha );
void setGyroLPFAlpha( double alpha );
double getPitch();
double getRoll();
int16_t getRawAccelX();
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
int getAccelOffsetX();
int getAccelOffsetY();
int getAccelOffsetZ();
int getGyroOffsetX();
int getGyroOffsetY();
int getGyroOffsetZ();
double getDegLPFAlpha();
double getGyroLPFAlpha();
double getAccelLPFAlpha();
void startGyroOffsets();
void startAccelOffsets();

#endif
