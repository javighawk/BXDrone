#include "IMUManager.h"

/* IMU definition */
MPU6050 IMU;

/* Accelerometer readings */
int16_t accel[3];

/* Gyroscope readings */
int16_t gyro[3];

/* Time variables to compute angles difference with gyro info */
unsigned long auxtime;
unsigned long delta;

/* Initialize the IMU and I2C communication */
void IMUInit(){

    /* Intialize communication I2C */
    Wire.begin();
    
    /* Initialize IMU. Default resolution is set to minimum, no offsets */
    IMU.initialize();

    /* Get first values for accel and gyro */
    IMU.getMotion6(&accel[0], &accel[1], &accel[2], &gyro[0], &gyro[1], &gyro[2]);
}

/* Calculate the value of roll angle */
void computeRoll(){
    
    computeAccelRoll();
    computeGyroDeltaRoll();
}

/* Calculate the value of pitch angle */
void computePitch(){
    
    computeAccelPitch();
    computeGyroDeltaPitch();
}

/* NOT DEFINED YET */
void computeAccelPitch(){}
void computeAccelRoll(){}
void computeGyroDeltaPitch(){}
void computeGyroDeltaRoll(){}

/* Get readings from IMU and compute them to get the angles */
void computeIMU(){
       
    /* Get Accel and Gyro readings */
    IMU.getMotion6(&accel[0], &accel[1], &accel[2], &gyro[0], &gyro[1], &gyro[2]);
    
    /* Refresh time variables to compute pitch and roll */
    delta = micros() - auxtime;
    auxtime = micros();
    
    /* Compute pitch and roll */
    computeRoll();
    computePitch();
}

/* NOT DEFINED YET */
double getPitch(){ return 0; }
double getRoll(){ return 0; }
int16_t getAccelX(){ return accel[0]; }
int16_t getAccelY(){ return accel[1]; }
int16_t getAccelZ(){ return accel[2]; }
int16_t getGyroX(){ return gyro[0]; }
int16_t getGyroY(){ return gyro[1]; }
int16_t getGyroZ(){ return gyro[2]; }







//void setAccelOffsets( int axis, char value ){
//    switch(axis){
//        case 0:
//            offX = value;
//            break;
//            
//        case 1:
//            offY = value;
//            break;
//            
//        case 2:
//            offZ = value;
//            break;
//    }
//}
//
//char getAccelOffset( int axis ){
//    
//    switch(axis){
//        case 0: return offX;
//        case 1: return offY;
//        case 2: return offZ;
//    }
//}
//
//void computeAccelMedValue(){
//    
//  if( !GNDX ){
//    if( accelXDeg > 0.01 || accelXDeg < -0.01 ){
//        if( accelXDeg > 0 ) offX--; else offX++;
//    } else GNDX = true;
//  }
//  
//  if( !GNDY ){
//    if( accelYDeg > 0.01 || accelYDeg < -0.01 ){
//        if( accelYDeg > 0 ) offY--; else offY++;
//    } else GNDY = true;
//  }
//  
//  if( !GNDZ ){
//    if( accelZDeg > 1.01 || accelZDeg < 0.99 ){
//        if( accelZDeg > 1 ) offZ--; else offZ++;
//    } else GNDZ = true;
//  }
//  
//  if( GNDX && GNDY && GNDZ ) isGrounding = false;
//  pendAccelOffTM();
//    
//}
//
//void startGNDLevel(){
//    isGrounding = true;
//    GNDX = false;
//    GNDY = false;
//    GNDZ = false;
//}
