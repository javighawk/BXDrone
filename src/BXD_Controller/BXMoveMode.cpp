#include "BXMoveMode.h"
#include <Servo.h>
#include "BXCOMM.h"
#include "BXD.h"
#include "Telemetry.h"

/* The BXDrone motors */
Servo mServo[4];

/* Motors user speed */
int mUserSpeed[4] = {0, 0, 0, 0};

/* Motor PID speed */
int mPIDSpeed[4] = {0, 0, 0, 0}; 

/* Motor state (on/off) */
uint8_t mSwitch[4] = {ON, ON, ON, ON};


/*
 * Initializer
 */
void MVM_init(){
    // Attach servos
    mServo[MOTOR_FL].attach(MOTOR1_PIN);
    mServo[MOTOR_FR].attach(MOTOR2_PIN);
    mServo[MOTOR_BR].attach(MOTOR3_PIN);
    mServo[MOTOR_BL].attach(MOTOR4_PIN);
}


/*
 * Set total speed to a motor.
 * The function keeps the final speed within boundaries
 * and sends it to the servo
 * 
 * @m The motor index
 * @sp The user speed we want to put the motor to
 * @pidSp The speed calculated by the PID controller
 */
void MVM_setMSpeed( int m, int mSp, int pidSp ){
    // Assert motor number
    if( m < 0 || m > 3 ) return;

    // If motor is switched off, user speed is zero
    if( !mSwitch[m] ) mSp = 0;

    // Calculate prelimiary servo speed and keep it within boundaries
    int tSpeed = max(0, min(MAX_SPEED, mSp + pidSp));
    
    // Store motor speeds
    mUserSpeed[m] = mSp;
    mPIDSpeed[m] = pidSp;

    // Send speed to servo if user speed is above threshold. Otherwise, servo is stopped
    if( mSp >= USER_SP_THRESH )
        mServo[m].writeMicroseconds( MIN_SIGNAL + tSpeed ); 
    else
        mServo[m].writeMicroseconds( MIN_SIGNAL ); 
}


/*
 * Set speed to a motor updating only the user component.
 * The function keeps the final speed within boundaries
 * and sends it to the servo
 * 
 * @m The motor index
 * @mSp The user speed we want to put the motor to
 */
void MVM_setMUserSpeed( int m, int mSp ){
    MVM_setMSpeed( m, mSp, mPIDSpeed[m] );
}


/*
 * Sets speed to a motor updating only the PID component
 * The function keeps the final speed within boundaries
 * and sends it to the servo
 * 
 * @m The motor index
 * @pidSp The speed calculated by the PID controller
 */
void MVM_setMPIDSpeed( int m, int pidSp ){
    MVM_setMSpeed( m, mUserSpeed[m], pidSp );
}


/*
 * Set same speed to all propellers
 * 
 * @sp The speed to be set to
 */
void MVM_setTotalSpeed( int sp ){
    for( int m=0 ; m<4 ; m++ )
        MVM_setMUserSpeed(m, sp);      
}


/*
 * Move along the Z axis (up/down). All propellers speed are modified the same amount
 * 
 * @speedT How much we want to set the propellers to
 */
void MVM_ZMove( int speedT ){
    MVM_setTotalSpeed(speedT);

    // Enable Pending motor speed TM
    TM_pendMUserSpeedTM();
}


/*
 * Modify pitch and roll
 * 
 * @speedT How much we want to modify the propellers speed
 * @direction The direction we want to move it to (front/rear/left/right)
 */
void MVM_PRMove( int speedT, uint8_t direction ){
    // Enable Pending motor speed TM
    TM_pendMUserSpeedTM();
}


/*
 * Modify the yaw angle
 * 
 * @speedT How much we want to modify the propellers speed
 * @direction The direction in which we want to spin it (left/right)
 */
void MVM_YawMove( int speedT, uint8_t direction ){
    // Enable Pending motor speed TM
    TM_pendMUserSpeedTM();
}


/*
 * Run MoveMode
 * 
 * @param infobyte Received byte where the movement info is contained
 */
void MVM_run( int data ){
    // Get data
    uint8_t direction = data & DIRECTION_MASK;
    uint8_t mode = data & MODE_MASK;
    uint16_t speed = COMM_read_wTimeOut();
    speed = (speed << 8) + COMM_read_wTimeOut();

    switch( mode ){
        case MOVE_Z_MODE: MVM_ZMove(speed);
        case MOVE_PR_MODE: MVM_PRMove(speed, direction);
        case MOVE_YAW_MODE: MVM_YawMove(speed, direction);
    }
}


/*
 * Switch motor from ON to OFF and viceversa
 * 
 * @param m The motor to be switched
 */
void MVM_switchMotor( int m ){
    mSwitch[m] = mSwitch[m] ^ ON;
}


/*
 * Test motors connection. 
 * Run each motor sequentially 0.5 seconds at speed 100
 */
void MVM_testMotors(){
    // Check if motors are all stopped
    for( int m=0 ; m<4 ; m++ )
        if( mUserSpeed[m] != 0 )
            return; 
  
    for( int m=0 ; m<4 ; m++ ){
        mServo[m].writeMicroseconds( MIN_SIGNAL + 100 );
        delay(500);
        mServo[m].writeMicroseconds( MIN_SIGNAL );
    }
}


/*
 * Getters
 */
int MVM_getMUserSpeed( int m ){ return mUserSpeed[m]; }
int MVM_getMPIDSpeed( int m ){ return mPIDSpeed[m]; }
uint8_t MVM_getMSwitch( int m ){ return mSwitch[m]; }
