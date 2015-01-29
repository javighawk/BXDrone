#include "Accel.h"

/* Accelerometer raw readings */
int X, Y, Z;

/* Accelerometer filtered readings history */
double Xn[2], Yn[2], Zn[2];

/* Degrees measured with the Accelerometer info */
double accelXDeg, accelYDeg, accelZDeg;

/* Accelerometer constants */
double medVolt = 1.65;
const double resVolt = 0.8;

/* LPF parameter */
double ALPHAACCEL = 0.6;

/* Accelerometer reading offsets (char = signed byte) */
char offX, offY, offZ;

/* Degrees read from accelerometer */
double accelPitchDeg, accelRollDeg;

/* Auxiliar Value */
boolean isGrounding = false, GNDX=false, GNDY=false, GNDZ=false;

void accelInit(){
    pinMode(ACCEL_GSELECTPIN, OUTPUT);
    pinMode(ACCEL_SLEEPPIN, OUTPUT);
    digitalWrite(ACCEL_GSELECTPIN, LOW);
    digitalWrite(ACCEL_SLEEPPIN, HIGH);

    Xn[0] = 0; Xn[1] = 0;
    Yn[0] = 0; Yn[1] = 0;
    Zn[0] = 0; Zn[1] = 0;
    
    offX = DEFAULTXOFFSET;
    offY = DEFAULTYOFFSET;
    offZ = DEFAULTZOFFSET;
    
    computeAccelerometer();
}

void accelLPF(){
    
    /* Filter X */
    Xn[1] = Xn[0];
    Xn[0] = ALPHAACCEL*X + (1-ALPHAACCEL)*Xn[1];
    
    /* Filter Y */
    Yn[1] = Yn[0];
    Yn[0] = ALPHAACCEL*Y + (1-ALPHAACCEL)*Yn[1];
    
    /* Filter Z */
    Zn[1] = Zn[0];
    Zn[0] = ALPHAACCEL*Z + (1-ALPHAACCEL)*Zn[1];
}

void computeAccelX(){  
    accelXDeg = Xn[0] * 5;
    accelXDeg /= 1024;
    accelXDeg -= medVolt;
    accelXDeg /= resVolt;  
}

void computeAccelY(){
    accelYDeg = Yn[0] * 5;
    accelYDeg /= 1024;
    accelYDeg -= medVolt;
    accelYDeg /= resVolt;    
}

void computeAccelZ(){
    accelZDeg = Zn[0] * 5;
    accelZDeg /= 1024;
    accelZDeg -= medVolt;
    accelZDeg /= resVolt;        
}


void computeAccelRoll(){

    accelRollDeg = atan2(accelXDeg, sqrt(accelYDeg*accelYDeg + accelZDeg*accelZDeg));
}

void computeAccelPitch(){
    
    accelPitchDeg = atan2(accelYDeg, accelZDeg);
}

void computeAccelerometer(){
       
    X = analogRead(ACCELX_PIN) + offX;
    Y = analogRead(ACCELY_PIN) + offY;
    Z = analogRead(ACCELZ_PIN) + offZ;
    accelLPF();
    computeAccelX();
    computeAccelY();
    computeAccelZ();
    computeAccelRoll();
    computeAccelPitch();
    if( isGrounding ) computeAccelMedValue();
}

double getAccelPitch(){
    return accelPitchDeg;
}

double getAccelRoll(){
    return accelRollDeg;
}

double getXG(){
    return accelXDeg;
}

double getYG(){
    return accelYDeg;
}

double getZG(){
    return accelZDeg;
}

void setAccelLPFAlpha( double k ){
    ALPHAACCEL = k;
}

double getAccelLPFAlpha(){
    return ALPHAACCEL;
}

void setAccelOffsets( int axis, char value ){
    switch(axis){
        case 0:
            offX = value;
            break;
            
        case 1:
            offY = value;
            break;
            
        case 2:
            offZ = value;
            break;
    }
}

char getAccelOffset( int axis ){
    
    switch(axis){
        case 0: return offX;
        case 1: return offY;
        case 2: return offZ;
    }
}

void computeAccelMedValue(){
    
  if( !GNDX ){
    if( accelXDeg > 0.01 || accelXDeg < -0.01 ){
        if( accelXDeg > 0 ) offX--; else offX++;
    } else GNDX = true;
  }
  
  if( !GNDY ){
    if( accelYDeg > 0.01 || accelYDeg < -0.01 ){
        if( accelYDeg > 0 ) offY--; else offY++;
    } else GNDY = true;
  }
  
  if( !GNDZ ){
    if( accelZDeg > 1.01 || accelZDeg < 0.99 ){
        if( accelZDeg > 1 ) offZ--; else offZ++;
    } else GNDZ = true;
  }
  
  if( GNDX && GNDY && GNDZ ) isGrounding = false;
  pendAccelOffTM();
    
}

void startGNDLevel(){
    isGrounding = true;
    GNDX = false;
    GNDY = false;
    GNDZ = false;
}
