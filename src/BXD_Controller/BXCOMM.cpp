#include "BXCOMM.h"
#include <XBee.h>
#include "TimeOut.h"
#include "BXD.h"

/* Transmission buffer */
uint8_t output_buf[BUFFER_SIZE];

/* Receiving buffer */
uint8_t input_buf[BUFFER_SIZE];

/* Transmission buffer index */
int output_buf_idx = 0;


/*
 * Initializer
 */
void COMM_init(){
    // Start Serial
    Serial.begin(SERIAL_BPS);
}


/*
 * Write a character to the output buffer
 * 
 * @param c Character to be sent
 * @param byteStuffing True (default) if byte stuffing needs to be performed. False otherwise
 */
void COMM_write(uint8_t c, bool byteStuffing){
    COMM_write(&c, 1, byteStuffing);
}


/*
 * Write an array of characters to the output buffer
 * 
 * @param *c Pointer to the array to be sent
 * @param len Length of the array
 * @param byteStuffing True (default) if byte stuffing needs to be performed. False otherwise
 */
void COMM_write(uint8_t *c, int len, bool byteStuffing){
    for( int i=len-1 ; i>=0 ; i-- ){
        byte sending = *(c + i);

        // Send the whole buffer if full
        if( output_buf_idx >= BUFFER_SIZE )
            COMM_flush();

        // Perform byte stuffing
        if( sending == ESC && byteStuffing ){
            output_buf[output_buf_idx++] = ESC;
        }
        
        output_buf[output_buf_idx++] = sending;
    }
}


/*
 * Clean output buffer
 */
void COMM_cleanBuffer(){
    memset( output_buf, 0, BUFFER_SIZE );
    output_buf_idx = 0;
}


/*
 * Directly writes data into Serial
 * 
 * @c The character to be sent
 */
void COMM_directWrite(uint8_t c, bool byteStuffing){
    // Perform byte stuffing
    if( c == ESC && byteStuffing ){
        Serial.write(ESC);
    }
    // Write character
    Serial.write(c);
}


/*
 * Flush out output buffer
 */
void COMM_flush(){
    // Write buffer to Serial
    Serial.write(output_buf,output_buf_idx);

    // Clean buffer
    COMM_cleanBuffer();
}


/*
 * Read a character from input buffer. The function checks if the
 * connection times out while waiting for new data in the input buffer.
 * 
 * @return The byte read. -1 if connection timed out
 */
int COMM_read_wTimeOut(){
    int c;
    while( (c = COMM_read()) == -1 )
        if( TMO_check() )
            return -1;

    return c;
}


/*
 * Reads incoming data if available.
 * 
 * @return The byte read. -1 if nothing available
 */
int COMM_read(){
    return Serial.read();
}


/*
 * Returns a pointer to the input buffer
 * 
 * @return Pointer to the buffer
 */
uint8_t *COMM_getInputBuffer(){
    return input_buf;
}

