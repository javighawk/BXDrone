#include "IMUManager.h"

/* IMU definition */
MPU6050 IMU;

/* Accelerometer readings */
int accel[3];

/* Accelerometer readings in [G] units */
double accelG[3];

/* Available Accel resolutions (in "g" units) */
int accelRes[4] = {2, 4, 8, 16};

/* Gyroscope readings */
int gyro[3];

/* Gyroscope readings in [deg/sec] units */
double gyroDPS[3];

/* Available Gyro resolutions (degrees per second) */
int gyroRes[4] = {250, 500, 1000, 2000};

/* Current resolution for Accel and Gyro  */
/* The number indicates the element index */
/* of available resolutions               */
int accelCurrentRes = 0;
int gyroCurrentRes = 0;

/* Accel and Gyro reading offsets */
int accelOffsets[3] = {0,0,0};
int gyroOffsets[3] = {0,0,0};
bool accelOffReady[3] = {true,true,true};
bool gyroOffReady[3] = {true,true,true};

/* Angles calculated from accelerometer readings in degrees */
double accelPitchAngle, accelRollAngle;

/* Time variables to compute angles difference with gyro info */
unsigned long auxtime, delta, maxdelta = 0;

/* Auxilair variable to calculate the average of delta ERASE LATER */
int times = 0;
unsigned long compute = 0, avgDelta = 0;
bool activateMaxDelta = false;

/* Angle values in degrees */
double pitchAngle, rollAngle;

/* LPF Parameters */
double ALPHA_ACCEL = 1, ALPHA_GYRO = 1, ALPHA_DEG = 1;

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
    
    /* Calculate pitch and roll angles from accelerometer readings */
    accelPitchAngle = atan2(accel[1], accel[2]);
    accelRollAngle = atan2(accel[0], sqrt(pow(accel[1],2) + pow(accel[2],2)));
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
    
    /* Apply offsets */
    for( int i=0 ; i<3 ; i++ ){
        accel[i] += accelOffsets[i];
        gyro[i] += gyroOffsets[i];
    }
    
    /* Refresh time variables to compute pitch and roll */
    delta = micros() - auxtime;
    auxtime = delta + auxtime;
    
    if( delta > maxdelta ) maxdelta = delta;
    compute += delta;
    times++;
    if( times == 100 ){
        avgDelta = compute/100;
        times = 0;
        compute = 0;
    }    
    
    /* Compute pitch and roll */
//    computeAccel();
//    computeGyro();
    
    /* Calculate angles from gyro and acceleroeter readings */
//    pitchAngle = accelPitchAngle; //0.9*(pitchAngle + gyroDPS[1]) + 0.1*accelPitchAngle;
//    rollAngle = accelRollAngle; //0.9*(rollAngle + gyroDPS[0]) + 0.1*accelRollAngle;
    
    /* Compute offsets if needed */
    if( !gyroOffReady[0] || !gyroOffReady[1] || !gyroOffReady[2] )
        computeGyroOffsets();
    
    if( !accelOffReady[0] || !accelOffReady[1] || !accelOffReady[2] )
        computeAccelOffsets();
}


/* 
 * Compute Accel offsets 
 */
void computeAccelOffsets(){
  
  int defAccelVal[3] = {0,0,1};
    
  for( int i=0 ; i<3 ; i++ ){
    if( !accelOffReady[i] ){
      double aVal = double(accel[i])*accelRes[accelCurrentRes]/32767;
      if( aVal > defAccelVal[i] + 0.01 ) accelOffsets[i] -= 1;
      else if( aVal < defAccelVal[i] - 0.01 ) accelOffsets[i] +=1;
      else accelOffReady[i] = true;
    }
  }
  
  pendAccelOffTM();    
}

/* 
 * Compute Gyro offsets 
 */
void computeGyroOffsets(){
    
  for( int i=0 ; i<3 ; i++ ){
    if( !gyroOffReady[i] ){
      double gVal = double(gyro[i])*gyroRes[gyroCurrentRes]/32767;
      if( gVal > 0.01 ) gyroOffsets[i] -= 1;
      else if( gVal < -0.01 ) gyroOffsets[i] +=1;
      else gyroOffReady[i] = true;
    }
  }
  
  pendGyroOffTM();    
}


/*
 * Set a value to the offsets of gyro/accel on any axis reading
 */
void setOffsets( int dev, int axis, int val ){
  
    if( dev == 0 ){
        if( axis == 0xFF ){
            accelOffsets[0] = DEFAULTACCELXOFFSET;
            accelOffsets[1] = DEFAULTACCELYOFFSET;
            accelOffsets[2] = DEFAULTACCELZOFFSET;
        } else
            accelOffsets[axis] = val;
    } else {
        if( axis == 0xFF ){
            gyroOffsets[0] = DEFAULTGYROXOFFSET;
            gyroOffsets[1] = DEFAULTGYROYOFFSET;
            gyroOffsets[2] = DEFAULTGYROZOFFSET;
        } else
            gyroOffsets[axis] = val;
    }      
}

/* Other setters */
void setDegLPFAlpha( double alpha ){ ALPHA_DEG = alpha; }
void setAccelLPFAlpha( double alpha ){ ALPHA_ACCEL = alpha; }
void setGyroLPFAlpha( double alpha ){ ALPHA_GYRO = alpha; }


/* Getters */
double getPitch(){ return pitchAngle; }
double getRoll(){ return rollAngle; }
int16_t *getAccelValues(){ return accel; }
int16_t *getGyroValues(){ return gyro; }
int16_t getRawAccelX(){ return accel[0]; }
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
int getAccelOffsetX(){ return accelOffsets[0]; }
int getAccelOffsetY(){ return accelOffsets[1]; }
int getAccelOffsetZ(){ return accelOffsets[2]; }
int getGyroOffsetX(){ return gyroOffsets[0]; }
int getGyroOffsetY(){ return gyroOffsets[1]; }
int getGyroOffsetZ(){ return gyroOffsets[2]; }
double getDegLPFAlpha(){ return ALPHA_DEG; }
double getGyroLPFAlpha(){ return ALPHA_GYRO; }
double getAccelLPFAlpha(){ return ALPHA_ACCEL; }

/* TEMPORARY FUNCTIONS */
unsigned long getDelta(){ return delta; }
unsigned long getAvgDelta(){ return avgDelta; }
unsigned long getMaxDelta(){
    unsigned long auxTime = maxdelta;
    maxdelta = 0;    
    return auxTime;
}

void startAccelOffsets(){
    accelOffReady[0] = false;
    accelOffReady[1] = false;
    accelOffReady[2] = false;
}
void startGyroOffsets(){
    gyroOffReady[0] = false;
    gyroOffReady[1] = false;
    gyroOffReady[2] = false;
}
