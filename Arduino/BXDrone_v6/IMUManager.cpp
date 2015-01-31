#include "IMUManager.h"

/* IMU definition */
MPU6050 IMU;

/* Accelerometer readings */
int16_t accel[3];

/* Accelerometer readings in [G] units */
double accelG[3];

/* Available Accel resolutions (in "g" units) */
int accelRes[4] = {2, 4, 8, 16};

/* Gyroscope readings */
int16_t gyro[3];

/* Gyroscope readings in [deg/sec] units */
double gyroDPS[3];

/* Available Gyro resolutions (degrees per second) */
int gyroRes[4] = {250, 500, 1000, 2000};

/* Current resolution for Accel and Gyro  */
/* The number indicates the element index */
/* of available resolutions               */
int accelCurrentRes = 0;
int gyroCurrentRes = 0;

/* Angles calculated from accelerometer readings in degrees */
double accelPitchAngle, accelRollAngle;

/* Time variables to compute angles difference with gyro info */
unsigned long auxtime, delta;

/* Angle values in degrees */
double pitchAngle, rollAngle;

/* Constant Pi */
const float pi = 3.14159;

/* Initialize the IMU and I2C communication */
void IMUInit(){

    /* Intialize communication I2C */
    Wire.begin();
    
    /* Initialize IMU. Default resolution is set to minimum, no offsets */
    IMU.initialize();

    /* Get first values for accel and gyro */
    IMU.getMotion6(&accel[0], &accel[1], &accel[2], &gyro[0], &gyro[1], &gyro[2]);
}

/* Compute the accelerometer readings */
void computeAccel(){
    
    /* Convert readings to [G] units */
    accelG[0] = double(accel[0])*accelRes[accelCurrentRes]/32767;
    accelG[1] = double(accel[1])*accelRes[accelCurrentRes]/32767;
    accelG[2] = double(accel[2])*accelRes[accelCurrentRes]/32767;
    
    /* Calculate pitch and roll angles from accelerometer readings */
    accelPitchAngle = atan2(accelG[1], accelG[2]);
    accelRollAngle = atan2(accelG[0], sqrt(accelG[1]*accelG[1] + accelG[2]*accelG[2]));
    
    /* Convert it to degrees */
    accelPitchAngle *= 180.0/pi;
    accelRollAngle *= 180.0/pi;
}

/* Compute the gyroscope readings */
void computeGyro(){
    
    /* Convert readings to degrees per seconds */
    gyroDPS[0] = double(gyro[0])*gyroRes[gyroCurrentRes]/32767;
    gyroDPS[1] = double(gyro[1])*gyroRes[gyroCurrentRes]/32767;
    gyroDPS[2] = double(gyro[2])*gyroRes[gyroCurrentRes]/32767;
    
    /* Converting into degreess to get the degrees variation in each axis */
    gyroDPS[0] *= delta;  gyroDPS[0] /= 1000000;
    gyroDPS[1] *= delta;  gyroDPS[1] /= 1000000;
    gyroDPS[2] *= delta;  gyroDPS[2] /= 1000000;
}

/* Get readings from IMU and compute them to get the angles */
void computeIMU(){
       
    /* Get Accel and Gyro readings */
    IMU.getMotion6(&accel[0], &accel[1], &accel[2], &gyro[0], &gyro[1], &gyro[2]);
    
    /* Refresh time variables to compute pitch and roll */
    delta = micros() - auxtime;
    auxtime = micros();
    
    /* Compute pitch and roll */
    computeAccel();
    computeGyro();
    
    /* Calculate angles from gyro and acceleroeter readings */
    pitchAngle = 0.95*(pitchAngle + gyroDPS[1]) + 0.05*accelPitchAngle;
    rollAngle = 0.95*(rollAngle + gyroDPS[0]) + 0.05*accelRollAngle;
}


/* Getters */
double getPitch(){ return pitchAngle; }
double getRoll(){ return rollAngle; }
int16_t getRawAccel(){ return accel[0]; }
int16_t getRawAccelY(){ return accel[1]; }
int16_t getRawAccelZ(){ return accel[2]; }
int16_t getRawGyroX(){ return gyro[0]; }
int16_t getRawGyroY(){ return gyro[1]; }
int16_t getRawGyroZ(){ return gyro[2]; }
double getAccelXG(){ return accelG[0]; }
double getAccelYG(){ return accelG[1]; }
double getAccelZG(){ return accelG[2]; }
double getGyroXDPS(){ return gyroDPS[0]; }
double getGyroYDPS(){ return gyroDPS[1]; }
double getGyroZDPS(){ return gyroDPS[2]; }
int getAccelCurrentRes(){ return accelCurrentRes; }
int getGyroCurrrentRes(){ return gyroCurrentRes; }
