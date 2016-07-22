#ifndef BXCOMM_h
#define BXCOMM_h


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "Arduino.h"


/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define BUFFER_SIZE       100
		

/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

void COMM_init();
void COMM_write(uint8_t c, bool byteStuffing = true);
void COMM_write(uint8_t *c, int len, bool byteStuffing = true);
void COMM_directWrite(uint8_t c, bool byteStuffing = true);
void COMM_flush();
int COMM_read();
int COMM_read_wTimeOut();
uint8_t *COMM_getInputBuffer();


#endif
