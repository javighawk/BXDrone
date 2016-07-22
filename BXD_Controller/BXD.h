#ifndef BXD_h
#define BXD_h

/*********************************************************/
/********************* INCLUDES **************************/
/*********************************************************/

#include "Arduino.h"


/*********************************************************/
/******************** MODES DEFINES **********************/
/*********************************************************/

#define MODE_MASK               0x0F
#define MOVE_Z_MODE             0x00
#define MOVE_PR_MODE            0x01
#define MOVE_YAW_MODE           0x02
#define COMMAND_MODE            0x04


/*********************************************************/
/****************** COMM PACKAGE DEFINES *****************/
/*********************************************************/

#define ENDOFTM                 0x05
#define TELEMETRY               0x0E
#define TM_PETITION             0x0F
#define ENDOFPCK                0x88
#define ESC                     0x7E
#define TM_CONFIRMATION         0x80


/*********************************************************/
/********************* PIN DEFINES ***********************/
/*********************************************************/

#define MOTOR1_PIN                3
#define MOTOR2_PIN                4
#define MOTOR3_PIN                5
#define MOTOR4_PIN                6
#define SETUPLED_PIN              10
#define COMMLED_PIN               11
#define CONNECTIONLED_PIN         12
#define IMU_INTERRUPTPIN          17



/*********************************************************/
/********************* LED DEFINES ***********************/
/*********************************************************/

#define REDLED_PIN                SETUPLED_PIN
#define GREENLED_PIN              COMMLED_PIN
#define BLUELED_PIN               CONNECTIONLED_PIN


/*********************************************************/
/******************** OTHER DEFINES **********************/
/*********************************************************/

#define SERIAL_BPS          115200
#define ON                        1
#define OFF                       0


#endif
