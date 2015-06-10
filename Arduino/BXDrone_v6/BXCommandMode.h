#ifndef BXCommandMode_h
#define BXCommandMode_h


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "BXD.h"
#include "PIDManager.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define COMMAND_SHORTCUT_MASK     0xD3
#define COMMAND_SHORTCUT_BIT      5
#define COMMAND_LENGTH            30

#define SHRTCMD_PID               0x10
#define SHRTCMD_SWITCHPID         0x11
#define SHRTCMD_TESTMOTORS        0x12
#define SHRTCMD_SETALPHA          0x13
#define SHRTCMD_SETOFFSETS        0x40
#define SHRTCMD_SETOLEVELS        0x41
#define SHRTCMD_STARTACCELOFFSETS 0x42
#define SHRTCMD_STARTGYROOFFSETS  0x43
#define SHRTCMD_SETMOTORPOWER     0x50 
#define SHRTCMD_SETMOTOROFFSET    0x51
#define SHRTCMD_STOPMOTORS        0x52


/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

void BX_initCommandMode();
void shortCutCommands();	

#endif
