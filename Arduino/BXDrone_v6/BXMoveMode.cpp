/*******************************************
 *  _ _                      _ _ 
 * | 4 |                    | 1 |
 * |_ _|                    |_ _|                          
 *     \\                  //
 *      \\                //
 *       \\              //
 *        \\            //
 *         \\__________//
 *         |  BX_Drone  |
 *         | __________ |
 *         //          \\
 *        //            \\
 *       //              \\
 *      //                \\
 *  _ _//                  \\_ _ 
 * | 3 |                    | 2 |
 * |_ _|                    |_ _|
 * 
 *******************************************/

#include "BXMoveMode.h"

/* The BXDrone motors */
Servo motor1, motor2, motor3, motor4;

/* Incoming byte info */
byte _byte, speedInfo, directionInfo;

/* Variable indicating whether the motors are moving or not */
bool BXDMoving = false;

extern byte infoByte, modeInfo;

/* Current motors speed */
int M1Speed = 0, M2Speed = 0,
M3Speed = 0, M4Speed = 0;

int totalSpeed = 0;

boolean mPower[4];
byte mOffset[4];

void BX_initMoveMode(){
  motor1.attach(MOTOR_PIN1);
  motor2.attach(MOTOR_PIN2);
  motor3.attach(MOTOR_PIN3);
  motor4.attach(MOTOR_PIN4);
  setMotorSpeed( 1, 0 );
  setMotorSpeed( 2, 0 );
  setMotorSpeed( 3, 0 );
  setMotorSpeed( 4, 0 );
  mPower[0] = true;
  mPower[1] = true;
  mPower[2] = true;
  mPower[3] = true;
  mOffset[0] = 0;  
  mOffset[1] = 0;
  mOffset[2] = 0;
  mOffset[3] = 0;
}

void runMoveMode(){
  
  _byte = infoByte;
  speedInfo = _byte & SPEED_MASK;
  directionInfo = _byte & DIRECTION_MASK;
  modeInfo = _byte & MODE_MASK;
  
  if( M1Speed == 0 && M2Speed == 0 && M3Speed == 0 && M4Speed == 0 )
      BXDMoving = false;
  else BXDMoving = true;
  
  if( speedInfo == 0 ){ Serial.write(EOT + (getIDvisitor() << 4) ); return; }
  if(modeInfo == 0) ZMoveBot(speedInfo, directionInfo);
  else if(modeInfo == 12) XYMoveBot(speedInfo, directionInfo);
}

void ZMoveBot(byte speedT, byte direction){

  switch(direction){
  case 0x00:
    M2Speed += speedT * 5;
    M1Speed += speedT * 5;
    M4Speed += speedT * 5;
    M3Speed += speedT * 5;
    
//    setAddingMotorSpeed(1);
//    setAddingMotorSpeed(2);
//    setAddingMotorSpeed(3);
//    setAddingMotorSpeed(4);
    break;

  case 0x10:
    M2Speed -= speedT * 5;
    M1Speed -= speedT * 5;
    M4Speed -= speedT * 5;
    M3Speed -= speedT * 5;
    
//    setAddingMotorSpeed(1);
//    setAddingMotorSpeed(2);
//    setAddingMotorSpeed(3);
//    setAddingMotorSpeed(4);
    break;

  case 0x20:
//    setMotorSpeed( 2, totalSpeed + speedT * SPEED_DEVIATION );
//    setMotorSpeed( 1, totalSpeed - speedT * SPEED_DEVIATION );
//    setMotorSpeed( 4, totalSpeed + speedT * SPEED_DEVIATION );
//    setMotorSpeed( 3, totalSpeed - speedT * SPEED_DEVIATION );
    break;

  case 0x30:
//    setMotorSpeed( 1, totalSpeed + speedT * SPEED_DEVIATION );
//    setMotorSpeed( 2, totalSpeed - speedT * SPEED_DEVIATION );
//    setMotorSpeed( 3, totalSpeed + speedT * SPEED_DEVIATION );
//    setMotorSpeed( 4, totalSpeed - speedT * SPEED_DEVIATION );
    break;
  }
  pendMotorSpeedTM();
}

void XYMoveBot(byte speedT, byte direction){

    setTotalSpeed(0);
  switch(direction){
  case 0x00:
    setTotalSpeed(0);
    break;

  case 0x10:
    setTotalSpeed(MAX_SPEED);
    break;

  case 0x20:
    break;

  case 0x30:
    break;
  }
  pendMotorSpeedTM();
}


void setMotorSpeed( int motor, int speed ){
  if( motor < 1 || motor > 4 ) return;
  
  if( !mPower[motor-1] ) speed = 0;

  if( speed < 0 ) speed = 0;
  if( speed > MAX_SPEED ) speed = MAX_SPEED;

  switch(motor){
      case 1:
        motor1.writeMicroseconds( MIN_SIGNAL + speed );
        M1Speed = speed;
        break;
    
      case 2:
        motor2.writeMicroseconds( MIN_SIGNAL + speed );
        M2Speed = speed;
        break;
    
      case 3:
        motor3.writeMicroseconds( MIN_SIGNAL + speed );
        M3Speed = speed;
        break;
    
      case 4:
        motor4.writeMicroseconds( MIN_SIGNAL + speed );
        M4Speed = speed;
        break;
  }         
}

void setAddingMotorSpeed( int motor ){
  
  if( motor < 1 || motor > 4 ) return;
  
  int mSpeed, diffSpeed;
  
  if( !mPower[motor-1] ){
      diffSpeed = 0;
      mSpeed = 0;
  } else{  
      diffSpeed = getDiffMotor(motor);
      mSpeed = getMotorSpeed(motor);
  }
  
  if (mSpeed < 0) mSpeed = 0;
  else if (mSpeed > MAX_SPEED) mSpeed = MAX_SPEED;
  
  int speed = diffSpeed + mSpeed;

  switch(motor){
      case 1:
        M1Speed = mSpeed;
        speed += mOffset[0];
        if( speed < 0 ) speed = 0;
        if( speed > MAX_SPEED ) speed = MAX_SPEED;
        motor1.writeMicroseconds( MIN_SIGNAL + speed );
        break;
    
      case 2:
        M2Speed = mSpeed;
        speed += mOffset[1];
        if( speed < 0 ) speed = 0;
        if( speed > MAX_SPEED ) speed = MAX_SPEED;
        motor2.writeMicroseconds( MIN_SIGNAL + speed );
        break;
    
      case 3:
        M3Speed = mSpeed;
        speed += mOffset[2];
        if( speed < 0 ) speed = 0;
        if( speed > MAX_SPEED ) speed = MAX_SPEED;
        motor3.writeMicroseconds( MIN_SIGNAL + speed );
        break;
    
      case 4:
        M4Speed = mSpeed;
        speed += mOffset[3];
        if( speed < 0 ) speed = 0;
        if( speed > MAX_SPEED ) speed = MAX_SPEED;
        motor4.writeMicroseconds( MIN_SIGNAL + speed );
        break;
  }         
}

void setTotalSpeed( int speed ){
  
    if( speed < 0 ) speed = 0;
    if( speed > MAX_SPEED ) speed = MAX_SPEED;
    
    setMotorSpeed( 1, speed );
    setMotorSpeed( 2, speed );
    setMotorSpeed( 3, speed );
    setMotorSpeed( 4, speed );  
    
    totalSpeed = speed;
}

int getMotorSpeed( int motor ){
    switch(motor){
        case 1:
            return M1Speed;
            break;
        case 2:
            return M2Speed;
            break;
        case 3:
            return M3Speed;
            break;
        case 4:
            return M4Speed;
            break;
    }
}

int getTotalSpeed(){
    return totalSpeed;
}

boolean getMotorPower(int motor){
    if( motor < 1 || motor > 4 ) return false;
    return mPower[motor-1];
}

void setMotorPower(int motor, boolean power){
    if( motor < 1 || motor > 4 ) return;
    
    mPower[motor-1] = power;
}

void setMotorOffset( byte mot, byte sp ){
    mOffset[mot-1] = sp; 
}

byte getMotorOffset( int mot ){
    return mOffset[mot-1];
}

bool isBXDMoving(){
    return BXDMoving;
}
