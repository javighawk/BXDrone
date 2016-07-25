#include "IMUManager.h"
#include <I2Cdev.h>
#include <MPU6050_6Axis_MotionApps20.h>
#include <Wire.h>

/* IMU object */
MPU6050 IMU;

/* Pitch, Roll & Yaw angles (in radians) */
float ypr[3];

/* Gyroscope reading */
int16_t gyro[3];

/* Flag to check whether there's data available from DMP */
bool dataAvailable = false;

/* IMU FIFO packet size */
uint16_t FIFOPacketSize;

/* Number of bytes currently in FIFO */
uint16_t fifoCount;

/* Interrupt status */
uint8_t intStatus;

/* FIFO storage buffer */
uint8_t fifoBuffer[64];

/* Extern declaration */
extern TimeRecord tIMU;


/*
 * Interrupt callback function
 */
void IMU_interrupt(){
    // Stop time recording
    tIMU.stop();

    // Activate flag
    dataAvailable = true;

    // Start time recording again
    tIMU.trigger();
}


/*
 * Initializer
 */
void IMU_init(){
    // Initialize I2C COMM
    Wire.begin();
    
    // Initialize IMU
    IMU.initialize();
    
    // Initialize DMP
    if( IMU.dmpInitialize() ){
        // Error while initializing. Implement Error message.
        while(1);
    }
    
    // Enable DMP
    IMU.setDMPEnabled(true);

    // Enable interrupt
    pinMode(IMU_INTERRUPTPIN, INPUT);
    attachInterrupt(IMU_INTERRUPTPIN, IMU_interrupt, RISING);

    // Get interrupt status
    intStatus = IMU.getIntStatus();
    
    // Get FIFO packet size
    FIFOPacketSize = IMU.dmpGetFIFOPacketSize();
}


/*
 * Get pitch, roll, yaw from the IMU's DMP and store them
 */
void IMU_compute(){
    // Return if there's no data available from DMP
    if( !dataAvailable && fifoCount < FIFOPacketSize )
        return;

    // Reset flag
    dataAvailable = false;

    // Get current interrupt status
    intStatus = IMU.getIntStatus();

    // Get current FIFO byte count
    fifoCount = IMU.getFIFOCount();

    // Check for overflow
    if( (intStatus & 0x10) || fifoCount == 1024 )
        IMU.resetFIFO();

    // Check for data
    else if( intStatus & 0x01 ){
        // Wait for correct available data length
        while( fifoCount < FIFOPacketSize ) fifoCount = IMU.getFIFOCount();
        
        // Read a packet from FIFO
        IMU.getFIFOBytes(fifoBuffer, FIFOPacketSize);

        // Update FIFO count
        fifoCount -= FIFOPacketSize;
    
        // Get angles & gyro
        Quaternion q;
        VectorFloat gravity;
        IMU.dmpGetQuaternion(&q, fifoBuffer);
        IMU.dmpGetGravity(&gravity, &q);
        IMU.dmpGetYawPitchRoll(ypr, &q, &gravity);
        IMU.dmpGetGyro(gyro, fifoBuffer);
    
        // Convert to degrees
        for( int i=0 ; i<3 ; i++ )
            ypr[i] *= (180/M_PI);
    }
}


/*
 * Getters
 */
float IMU_getYaw(){ return ypr[0]; }
float IMU_getPitch(){ return ypr[1]; }
float IMU_getRoll(){ return ypr[2]; }
int16_t IMU_getGyroX(){ return gyro[0]; }
int16_t IMU_getGyroY(){ return gyro[1]; }
int16_t IMU_getGyroZ(){ return gyro[2]; }
