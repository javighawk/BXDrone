#include "PIDManager.h"
#include <PID_v1.h>

/* Pitch and roll values to be used by the PID controllers */
double pitchAnglePID, rollAnglePID;

/* Gyro values to be used by the PID controllers */
double gyroXPID, gyroYPID;

/* Enabled PID variable */
boolean enabl;

/* Amount that has to be added to each motor speed after PID computing */
double diffM1=0, diffM2=0, diffM3=0, diffM4=0;

/* Output of the PID's */
double diffSpeed_Pitch = 0, diffSpeed_Roll = 0, diffSpeed_GX = 0, diffSpeed_GY = 0;

/* Desired points for PID's */
double setPointRoll = 0, setPointPitch = 0;
double setPointGX = 0, setPointGY = 0;

/* Default values for PID parameters */
int PIDKv[2][3] = { {DEFAULT_PVALUE, DEFAULT_IVALUE, DEFAULT_DVALUE},
                    {DEFAULT_PVALUE, DEFAULT_IVALUE, DEFAULT_DVALUE}};

/* PID's controlling Pitch and Roll, calculated from Accelerometer readings */
PID pidPitch(&pitchAnglePID, &diffSpeed_Pitch, &setPointPitch, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], DIRECT);
PID pidRoll(&rollAnglePID, &diffSpeed_Roll, &setPointRoll, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], DIRECT);

/* PID's stabilizing using outputs from the Gyroscope */
PID pidGyroX(&gyroXPID, &diffSpeed_GX, &setPointPitch, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], DIRECT);
PID pidGyroY(&gyroYPID, &diffSpeed_GY, &setPointPitch, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], REVERSE);


void PIDInit(){
    
    pidPitch.SetMode(AUTOMATIC);
    pidPitch.SetOutputLimits(-200, +200);
    pidPitch.SetSampleTime(0.1);
    
    pidRoll.SetMode(AUTOMATIC);
    pidRoll.SetOutputLimits(-200, +200);
    pidRoll.SetSampleTime(0.1);
    
    pidGyroX.SetMode(AUTOMATIC);
    pidGyroX.SetOutputLimits(-200, +200);
    pidGyroX.SetSampleTime(0.1);
    
    pidGyroY.SetMode(AUTOMATIC);
    pidGyroY.SetOutputLimits(-200, +200);
    pidGyroY.SetSampleTime(0.1);
    
    enabl = true;
}

void PIDCompute(){
  
    gyroXPID = getRawGyroX();
    gyroYPID = getRawGyroY();
    
    pidPitch.Compute();
    pidRoll.Compute();
    pidGyroX.Compute();
    pidGyroY.Compute();
    
    diffM1 = diffSpeed_Pitch + diffSpeed_GX;
    diffM2 = diffSpeed_Roll + diffSpeed_GY;
    diffM3 = -diffM1;
    diffM4 = -diffM2;
    
    setAddingMotorSpeed(1);
    setAddingMotorSpeed(2);
    setAddingMotorSpeed(3);
    setAddingMotorSpeed(4);
}

void runPitchRoll(){
    computeIMU();
    pitchAnglePID = getPitch();
    rollAnglePID = getRoll();
}

double getDiffMotor(int motor){
    switch(motor){
        case 1: return diffM1;
        case 2: return diffM2;
        case 3: return diffM3;
        case 4: return diffM4; 
    }
}

void setPIDValues( int roll, int pid, int value ){
    
    PIDKv[roll][pid] = value;
    
    pidPitch.SetTunings(PIDKv[0][0], PIDKv[0][1], PIDKv[0][2]);
    pidRoll.SetTunings(PIDKv[0][0], PIDKv[0][1], PIDKv[0][2]);
    pidGyroX.SetTunings(PIDKv[1][0], PIDKv[1][1], PIDKv[1][2]);
    pidGyroY.SetTunings(PIDKv[1][0], PIDKv[1][1], PIDKv[1][2]);
}

int getPIDValues( int roll, int pid ){
    return PIDKv[roll][pid];
}

void setPIDEnabled( boolean enabled ){
    if( enabled ){
        pidPitch.SetMode(AUTOMATIC);
        pidRoll.SetMode(AUTOMATIC);
        pidGyroX.SetMode(AUTOMATIC);
        pidGyroY.SetMode(AUTOMATIC);
    }
    else{
        pidPitch.SetMode(MANUAL);
        pidRoll.SetMode(MANUAL);
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

void PIDSetCurrentOLevels(){
    setPointPitch = pitchAnglePID;
    setPointRoll = rollAnglePID;
}

double PIDGetPitchOLevel(){
    return setPointPitch;
}

double PIDGetRollOLevel(){
    return setPointRoll;
}
