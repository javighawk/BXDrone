#include "PIDManager.h"
#include <PID_v1.h>

/* Pitch and roll values to be used by the PID controllers */
double pitchAnglePID, rollAnglePID;

/* Enabled PID variable */
boolean enabl;

/* TEMPORARY */
double diffM1=0, diffM2=0, diffM3=0, diffM4=0;
double setPointRoll = 0;
double setPointPitch = 0;

int PIDKv[2][3] = { {DEFAULT_PVALUE, DEFAULT_IVALUE, DEFAULT_DVALUE},
                    {DEFAULT_PVALUE, DEFAULT_IVALUE, DEFAULT_DVALUE}};

PID pidPitch1(&pitchAnglePID, &diffM1, &setPointPitch, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], DIRECT);
PID pidPitch3(&pitchAnglePID, &diffM3, &setPointPitch, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], REVERSE);
PID pidRoll2(&rollAnglePID, &diffM2, &setPointRoll, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], DIRECT);
PID pidRoll4(&rollAnglePID, &diffM4, &setPointRoll, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], REVERSE);

void PIDInit(){
    
    pidPitch1.SetMode(AUTOMATIC);
    pidPitch1.SetOutputLimits(-200, +200);
    pidPitch1.SetSampleTime(1);
    pidPitch3.SetMode(AUTOMATIC);
    pidPitch3.SetOutputLimits(-200, +200);
    pidPitch3.SetSampleTime(1);
    
    pidRoll2.SetMode(AUTOMATIC);
    pidRoll2.SetOutputLimits(-200, +200);
    pidRoll2.SetSampleTime(1);
    pidRoll4.SetMode(AUTOMATIC);
    pidRoll4.SetOutputLimits(-200, +200);
    pidRoll4.SetSampleTime(1);
    
    enabl = true;
}

void PIDCompute(){
    
    runPitchRoll();
    pidPitch1.Compute();
    pidPitch3.Compute();
    pidRoll2.Compute();
    pidRoll4.Compute();
    
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
    
    pidPitch1.SetTunings(PIDKv[0][0], PIDKv[0][1], PIDKv[0][2]);
    pidPitch3.SetTunings(PIDKv[0][0], PIDKv[0][1], PIDKv[0][2]);
    pidRoll2.SetTunings(PIDKv[1][0], PIDKv[1][1], PIDKv[1][2]);
    pidRoll4.SetTunings(PIDKv[1][0], PIDKv[1][1], PIDKv[1][2]);
}

int getPIDValues( int roll, int pid ){
    return PIDKv[roll][pid];
}

void setPIDEnabled( boolean enabled ){
    if( enabled ){
        pidPitch1.SetMode(AUTOMATIC);
        pidPitch3.SetMode(AUTOMATIC);
        pidRoll2.SetMode(AUTOMATIC);
        pidRoll4.SetMode(AUTOMATIC);
    }
    else{
        pidPitch1.SetMode(MANUAL);
        pidPitch3.SetMode(MANUAL);
        pidRoll2.SetMode(MANUAL);
        pidRoll4.SetMode(MANUAL);
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
