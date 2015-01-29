#ifndef BXD_h
#define BXD_h

/*********************************************************/
/********************* INCLUDES **************************/
/*********************************************************/

#include "Arduino.h"


/*********************************************************/
/*************** COMM PACKAGE DEFINES ********************/
/*********************************************************/

#define ACK               0x06
#define EOT               0x04
#define LF                0x0A
#define BEACON            0x07
#define BXD_ID            0x0D
#define I_AM              0x01
#define TM_CONFIRMATION   0x80


/*********************************************************/
/****************** INFO BYTE DEFINES ********************/
/*********************************************************/

#define MODE_MASK            0x0C
#define SPEED_MASK           0x03
#define DIRECTION_MASK       0x30
#define XYMOVEMODE_ID        0x00
#define ZMOVEMODE_ID         0x0C
#define TEXTMODE_ID          0x04
#define COMMANDMODE_ID       0x08


/*********************************************************/
/********************* LEDs DEFINES **********************/
/*********************************************************/

#define SERIALPIN                8
#define REDLEDPIN                2
#define GREENLEDPIN              4
    
#endif
