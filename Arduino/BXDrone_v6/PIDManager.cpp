#include "PIDManager.h"
#include <PID_v1.h>

/* Enabled PID variable */
boolean enabl;

/* Amount that has to be added to each motor speed after PID computing */
double diffM1=0, diffM2=0, diffM3=0, diffM4=0;

/* Actual Accel and Gyro values to be used by the PID controllers */
double accelXPID, accelYPID, accelZPID;
double gyroXPID, gyroYPID;

/* Output of the PID's */
double diffSpeed_AX = 0, diffSpeed_AY = 0, diffSpeed_AZ = 0;
double diffSpeed_GX = 0, diffSpeed_GY = 0;

/* Desired points for PID's */
double setPointAX = 0, setPointAY = 0, setPointAZ = 0x7FFF/2;
double setPointGX = 0, setPointGY = 0;

/* Desired angles (in degrees) */
int desiredPitch, desiredRoll;

/* Default values for PID parameters */
double PIDKv[2][3] = { {double(DEFAULT_PVALUE_ANGLES)/100, 
                        double(DEFAULT_IVALUE_ANGLES)/100, 
                        double(DEFAULT_DVALUE_ANGLES)/100},
                       {double(DEFAULT_PVALUE_GYRO)/100, 
                        double(DEFAULT_IVALUE_GYRO)/100, 
                        double(DEFAULT_DVALUE_GYRO)/100}};

/* PID's controlling Pitch and Roll, calculated from Accelerometer readings */
PID pidAccelX(&accelXPID, &diffSpeed_AX, &setPointAX, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], DIRECT);
PID pidAccelY(&accelYPID, &diffSpeed_AY, &setPointAY, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], DIRECT);
PID pidAccelZ(&accelZPID, &diffSpeed_AZ, &setPointAZ, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], DIRECT);

/* PID's stabilizing using outputs from the Gyroscope */
PID pidGyroX(&gyroXPID, &diffSpeed_GX, &setPointGX, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], DIRECT);
PID pidGyroY(&gyroYPID, &diffSpeed_GY, &setPointGY, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], REVERSE);


void PIDInit(){
    
    pidAccelX.SetMode(AUTOMATIC);
    pidAccelX.SetOutputLimits(-200, +200);
    pidAccelX.SetSampleTime(0.1);
    
    pidAccelY.SetMode(AUTOMATIC);
    pidAccelY.SetOutputLimits(-200, +200);
    pidAccelY.SetSampleTime(0.1);
    
    pidAccelZ.SetMode(AUTOMATIC);
    pidAccelZ.SetOutputLimits(-200, +200);
    pidAccelZ.SetSampleTime(0.1);
    
    pidGyroX.SetMode(AUTOMATIC);
    pidGyroX.SetOutputLimits(-200, +200);
    pidGyroX.SetSampleTime(0.1);
    
    pidGyroY.SetMode(AUTOMATIC);
    pidGyroY.SetOutputLimits(-200, +200);
    pidGyroY.SetSampleTime(0.1);
    
    enabl = true;
}

void PIDCompute(){
  
    accelXPID = getRawAccelX(); 
    accelYPID = getRawAccelY();
    gyroXPID = getRawGyroX();
    gyroYPID = getRawGyroY();
    
    diffSpeed_AX = 0;
    diffSpeed_AY = 0;
    diffSpeed_GX = 0;
    diffSpeed_GY = 0;
    
    pidAccelX.Compute();
    pidAccelY.Compute();
    pidGyroX.Compute();
    pidGyroY.Compute();
    
    diffM1 = diffSpeed_AY + diffSpeed_GX;
    diffM2 = diffSpeed_AX + diffSpeed_GY;
    diffM3 = -diffM1;
    diffM4 = -diffM2;
    
    setAddingMotorSpeed(1);
    setAddingMotorSpeed(2);
    setAddingMotorSpeed(3);
    setAddingMotorSpeed(4);
}

double getDiffMotor(int motor){
    switch(motor){
        case 1: return diffM1;
        case 2: return diffM2;
        case 3: return diffM3;
        case 4: return diffM4; 
    }
}

void setPIDValues( int roll, int pid, double value ){
    
    PIDKv[roll][pid] = value;
    
    pidAccelX.SetTunings(PIDKv[0][0], PIDKv[0][1], PIDKv[0][2]);
    pidAccelY.SetTunings(PIDKv[0][0], PIDKv[0][1], PIDKv[0][2]);
    pidAccelZ.SetTunings(PIDKv[0][0], PIDKv[0][1], PIDKv[0][2]);
    pidGyroX.SetTunings(PIDKv[1][0], PIDKv[1][1], PIDKv[1][2]);
    pidGyroY.SetTunings(PIDKv[1][0], PIDKv[1][1], PIDKv[1][2]);
}

double getPIDValues( int roll, int pid ){
    return PIDKv[roll][pid];
}

void setPIDEnabled( boolean enabled ){
    if( enabled ){
        pidAccelX.SetMode(AUTOMATIC);
        pidAccelY.SetMode(AUTOMATIC);
        pidAccelZ.SetMode(AUTOMATIC);
        pidGyroX.SetMode(AUTOMATIC);
        pidGyroY.SetMode(AUTOMATIC);
    }
    else{
        pidAccelX.SetMode(MANUAL);
        pidAccelY.SetMode(MANUAL);
        pidAccelZ.SetMode(MANUAL);
        pidGyroX.SetMode(MANUAL);
        pidGyroY.SetMode(MANUAL);
        diffM1 = 0;
        diffM2 = 0;
        diffM3 = 0;
        diffM4 = 0;
    }
    
    enabl = enabled;
}

boolean isPIDEnabled(){
    return enabl;
}

void PID_setDesiredAngles(int pitch, int roll){
  
    if( pitch == desiredPitch && roll == desiredRoll ) return;
    else{ desiredPitch == pitch; desiredRoll == roll; }
    
    
    if( pitch == 0 && roll == 0 ){
        setPointAZ = int(0x7FFF/2);
        setPointAY = 0;
        setPointAX = 0;
    } else if( pitch == 0 && roll != 0 ){
        setPointAZ = int((1/(1 + pow(tan(float(roll)*3.14159/180),2)))*(0x3FFF));
        setPointAY = 0;
        setPointAX = int(tan(float(roll)*3.14159/180) * setPointAZ);
    } else {
        setPointAZ = int((1/(1 + pow(tan(float(pitch)*3.14159/180),2)))*(0x3FFF));
        setPointAY = int(tan(float(pitch)*3.14159/180) * setPointAZ);
        setPointAX = 0;
    }

    
}

int PID_getDesiredPitch(){ return desiredPitch; }
int PID_getDesiredRoll(){ return desiredRoll; }
double PID_getSetPointAX(){ return setPointAX; }
double PID_getSetPointAY(){ return setPointAY; }
double PID_getSetPointAZ(){ return setPointAZ; }
