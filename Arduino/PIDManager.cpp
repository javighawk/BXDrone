#include "PIDManager.h"
#include <PID_v1.h>

/* Auxiliar variable to measure Pitch & Roll computing period */
unsigned long timePR;

/* Pitch and roll values */
double pitchDeg, rollDeg;

/* Enabled PID */
boolean enabl;

/* TEMPORARY */
double diffM1=0, diffM2=0, diffM3=0, diffM4=0;
double setPointRoll = 0;
double setPointPitch = 0;

double pitchF, pitchB, rollL, rollR;

int PIDKv[2][3] = { {DEFAULT_PVALUE, DEFAULT_IVALUE, DEFAULT_DVALUE},
                    {DEFAULT_PVALUE, DEFAULT_IVALUE, DEFAULT_DVALUE}};

PID pidPitch1(&pitchDeg, &pitchF, &setPointPitch, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], DIRECT);
PID pidPitch3(&pitchDeg, &pitchB, &setPointPitch, PIDKv[0][0], PIDKv[0][1], PIDKv[0][2], REVERSE);
PID pidRoll2(&rollDeg, &rollR, &setPointRoll, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], DIRECT);
PID pidRoll4(&rollDeg, &rollL, &setPointRoll, PIDKv[1][0], PIDKv[1][1], PIDKv[1][2], REVERSE);

/* Degrees LPF parameter */
double ALPHADEG = 1;

void PIDInit(){
    
    pidPitch1.SetMode(AUTOMATIC);
    pidPitch1.SetOutputLimits(-200, +200);
    pidPitch1.SetSampleTime(10);
    pidPitch3.SetMode(AUTOMATIC);
    pidPitch3.SetOutputLimits(-200, +200);
    pidPitch3.SetSampleTime(10);
    
    pidRoll2.SetMode(AUTOMATIC);
    pidRoll2.SetOutputLimits(-200, +200);
    pidRoll2.SetSampleTime(10);
    pidRoll4.SetMode(AUTOMATIC);
    pidRoll4.SetOutputLimits(-200, +200);
    pidRoll4.SetSampleTime(10);
    
    enabl = true;
    
    accelInit();
    gyroInit();
    
    timePR = millis();
}

void PIDCompute(){
    
    runPitchRoll();
    pidPitch1.Compute();
    pidPitch3.Compute();
    pidRoll2.Compute();
    pidRoll4.Compute();
    
    diffM1 = pitchF;
    diffM2 = rollR;
    diffM3 = pitchB;
    diffM4 = rollL;
    
    setAddingMotorSpeed(1);
    setAddingMotorSpeed(2);
    setAddingMotorSpeed(3);
    setAddingMotorSpeed(4);

}

void runPitchRoll(){
    if( millis()-timePR >= PR_PERIOD_MS ){
        double oldPitch = pitchDeg;
        double oldRoll = rollDeg;
        computeAccelerometer();
        computeGyro();
        pitchDeg = 0.95*(pitchDeg+getGyroDeltaPitch()) + 0.05*getAccelPitch();
        rollDeg = 0.95*(rollDeg+getGyroDeltaRoll()) + 0.05*getAccelRoll();
        pitchDeg = ALPHADEG*pitchDeg + (1-ALPHADEG)*oldPitch;
        rollDeg = ALPHADEG*rollDeg + (1-ALPHADEG)*oldRoll;
        timePR = millis();
    }
}

double getPitch(){
    return pitchDeg;
}

double getRoll(){
    return rollDeg;
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
        pitchF = 0;
        pitchB = 0;
        rollR = 0;
        rollL = 0;
    }
    
    enabl = enabled;
}

boolean isPIDEnabled(){
    return enabl;
}

void PIDSetCurrentOLevels(){
    setPointPitch = pitchDeg;
    setPointRoll = rollDeg;
}

double PIDGetPitchOLevel(){
    return setPointPitch;
}

double PIDGetRollOLevel(){
    return setPointRoll;
}

double getDegreesLPFAlpha(){
    return ALPHADEG;
}

void setDegreesLPFAlpha( double k ){
    ALPHADEG = k;
}
