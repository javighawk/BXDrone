#include "Gyro.h"

/* History gyro input samples and outputs */
double Xout[2], Zout[2], Xin, Zin;

unsigned long auxtime;
unsigned long delta;

// 2.44 º/s of resolution
const double res = 2.44;

/* PI constant */
const float pi = 3.14159;

double GYRO_MEDVALUE_X = 258;
double GYRO_MEDVALUE_Z = 258;

/* Degrees read from gyroscope */
double gyroDeltaPitch, gyroDeltaRoll;

/* LPF parameter */
double ALPHAGYRO = 0.6;

boolean isStill = false;
boolean xReady = true, zReady = true;
unsigned long xTime, zTime;

void gyroInit(){
      Xout[0] = 0; Xout[1] = 0;
      
      Zout[0] = 0; Zout[1] = 0;

      Xin = 0; Zin = 0;
      
      auxtime = millis();
      
      gyroDeltaPitch = 0;
      gyroDeltaRoll = 0;
     
      delay(1); 
      computeGyro(); 
}

void gyroLPF(){
    
    Xout[0] = ALPHAGYRO*Xin + (1-ALPHAGYRO)*Xout[1];
    Zout[0] = ALPHAGYRO*Zin + (1-ALPHAGYRO)*Zout[1];
}

void computeGyroDeltaPitch(){
  
    gyroDeltaPitch = Xout[0] * res;    
    gyroDeltaPitch /= 1000;    
    gyroDeltaPitch *= delta;    
    gyroDeltaPitch *= pi / 180;
}

void computeGyroDeltaRoll(){    

    gyroDeltaRoll = Zout[0] * res;
    gyroDeltaRoll /= 1000;
    gyroDeltaRoll *= delta;
    gyroDeltaRoll *= pi / 180;
}

void computeGyro(){
    
    /* Refresh history */
    Zout[1] = Zout[0];
    Xout[1] = Xout[0];
    
    /* Get raw gyro data */
    Xin = analogRead(GYROPITCH_PIN) - GYRO_MEDVALUE_X;
    Zin = analogRead(GYROROLL_PIN) - GYRO_MEDVALUE_Z;
    
    delta = millis() - auxtime;
    auxtime = millis();
    
    gyroLPF();
    computeGyroDeltaPitch();
    computeGyroDeltaRoll();   
    
    if( isStill ) computeStillLevel();
}

double getGyroDeltaPitch(){
    return gyroDeltaPitch;
}

double getGyroDeltaRoll(){
    return gyroDeltaRoll;
}

double getGyroLPFAlpha(){
    return ALPHAGYRO;
}

void setGyroLPFAlpha( double k ){
    ALPHAGYRO = k;
}

void startStillLevel(){
    isStill = true;
    xReady = false;
    zReady = false;
    xTime = millis();
    zTime = xTime;
}

void computeStillLevel(){
  
  if( !xReady ){
      if( Xin > 0.01 ){
          GYRO_MEDVALUE_X += 0.1;
          xTime = millis();
      }
      else if( Xin < -0.01 ){
          GYRO_MEDVALUE_X -= 0.1;
          xTime = millis();
      } else if( millis() - xTime >= 100 ) xReady = true;
  }
  
  if( !zReady ){
      if( Zin > 0.01 ){
          GYRO_MEDVALUE_Z += 0.1;
          zTime = millis();
      }
      else if( Zin < -0.01 ){
          GYRO_MEDVALUE_Z -= 0.1;
          zTime = millis();
      } else if( millis() - zTime >= 100 ) zReady = true;
  }
  
  if( zReady && xReady ) isStill = false;
  
  pendGyroStillTM();
  
}

double getGyroXStillLevel(){
    return GYRO_MEDVALUE_X;
}

double getGyroZStillLevel(){
    return GYRO_MEDVALUE_Z;
}
