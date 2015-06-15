#include "BXInitCOMM.h"
#include "BXMoveMode.h"
#include "TimeOut.h"
#include "Telemetry.h"
#include "PIDManager.h"
#include "BXCommandMode.h"
#include "I2Cdev.h"
#include "MPU6050.h"
#include "Wire.h"
#include <PID_v1.h>
#include <Servo.h>

/*
  Communication
  
  We send sequences to the Arduino. It interpretates them
  doing the action attached to the sequence.
  
  InfoByte doc:
  [LINE_FEED][NONE][DIR_1 / SHCCM][DIR_0][MODE_1][MODE_0][SPEED_1][SPEED_0]
  
  SPEED_0 & SPEED_1 mean the speed of the robot, going from 0 to 3
 
  DIR_0 & DIR_1 mean direction: 00=UP; 01=DOWN; 10=LEFT; 11=RIGHT
  
  Bit 5 (DIR_1) is also used as Shortcut-Command mode (SHCMM) flag in Command mode, for
  commands which do not need text interface to be executed.
  
  MODE_0 & MODE_1 are: 00= XY MOVE_MODE; 01=TEXT MODE; 10=COMMAND MODE; 11= Z MOVE_MODE 
  
  On SPIN MODE, DIR_0 means 0 for UP, 1 for DOWN, DIR_1 means 0 for LEFT, 1 for RIGHT.
  
  Bits 0 & 1 are also used for shortcut commands in Command mode.
  DIR_1 indicates in Command Mode whether is a shortcut command or not
  
  Bit 7 is used as Line Feed from the NOC.
  
*/

/* Variables declarations */
byte infoByte, modeInfo;

void setup(){
    Serial.begin(SERIAL_BPS);
    pinMode(GREENLEDPIN,OUTPUT);
    pinMode(REDLEDPIN,OUTPUT);
    pinMode(SERIALPIN,OUTPUT);
    digitalWrite(GREENLEDPIN, LOW);
    digitalWrite(REDLEDPIN, LOW);
    digitalWrite(SERIALPIN, LOW);
    listen();
    BX_initMoveMode();
    PIDInit();
    IMUInit();
    feedTimeOut();
    digitalWrite(GREENLEDPIN, HIGH);
}

void loop(){
    runPitchRoll();
    if( isBXDMoving() ) 
        PIDCompute();
    checkTelemetry();
    if( checkTimeOut() );
    if(Serial.available() > 0)
        identifyByte();
}

/**
 *  Identifies an incoming byte.
 */
void identifyByte(){ 
    
    feedTimeOut();
    infoByte = Serial.read();    
    
    if( infoByte == TM_CONFIRMATION){
        startTM();
        return;
    }

    modeInfo = (infoByte & MODE_MASK);
    
    if(modeInfo == 0x04){
//        text.run();
  
        return;
    }
    if(modeInfo == 0x08){
        if( bitRead(infoByte,COMMAND_SHORTCUT_BIT) ){
            shortCutCommands();
            return; 
        }
        return;
    }
    
    runMoveMode();  
}
