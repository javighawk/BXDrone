#include "Telemetry.h"

/* Auxiliar variable to measure Telemetry period */
unsigned long timer;

/* Turn for TM */
int turn = 0;

/* Auxiliary variables */
boolean pendingTM = false;

/* Other TM Pending Boolean Variables */
boolean pendingPIDTM = true;
boolean pendingAlphaTM = true;
boolean pendingAccelOffTM = true;
boolean pendingGyroOffTM = true;
boolean pendingMotorsPowerTM = true;
boolean pendingMotorOffsetsTM = true;
boolean pendingMotorSpeedTM = true;

/* 
 * Function used to send Telemetry. It performs byte stuffing
 * so the data sent is not in conflict with ENDOFCPK byte 
 */
void SerialSendData(uint32_t data, int len){
    
    for( int i=len-1 ; i>=0 ; i-- ){
        byte sending = (data >> 8*i) & 0xFF;
        if( sending == ESC || sending == ENDOFPCK )
            Serial.write( ESC );
            
        Serial.write( sending );
    }
}


/* 
 * Function created to send a double as a String with 2 decimals
 */
void writeDouble(double param){
  
    int ent = (int)param;
    int dec = (int)((param - ent)*100);
    
    String p;
    
    if( ent == 0 ){
        if( dec < 0 ){
            p = String('-');
            dec = -dec;
        }
    } else if( ent < 0 ){
       dec = -dec;
    } 
    
    p += String(ent);
    String d = String(dec);
    p += '.';
    
    if( d.length() == 1 ) p+= '0';
    
    p+= d;
    
    
    int i;
    for( i=0 ; i<p.length() ; i++ ){
      SerialSendData(p.charAt(i), 1);
    }
}


/*
 * Notifies the end of Telemetry transmission
 */
void endTM(){
    byte IDv = getIDvisitor();
    SerialSendData( ENDOFTM + ( IDv << 4 ), 1 );
    pendingTM = false;
}
    

void motorsSpeedTelemetry(){
    
    /* Motors speed */
    int m1s = getMotorSpeed(1), 
        m2s = getMotorSpeed(2), 
        m3s = getMotorSpeed(3), 
        m4s = getMotorSpeed(4);

    // Send Motors speed
    SerialSendData( byte(TEL_SPEED), 1 );
    SerialSendData( m1s, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( m2s, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( m3s, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( m4s, 2 );
    Serial.write( ENDOFPCK );
}

void motorsDiffTelemetry(){
  
    // Send differential Motors speed
    SerialSendData( TEL_DIFFMOTOR, 1 );
    writeDouble( getDiffMotor(1) );
    Serial.write( ENDOFPCK );
    writeDouble( getDiffMotor(2) );
    Serial.write( ENDOFPCK );
    writeDouble( getDiffMotor(3) );
    Serial.write( ENDOFPCK );
    writeDouble( getDiffMotor(4) );
    Serial.write( ENDOFPCK );
}
  
  
void accelGyroTelemetry(){
  
    /* Get raw accelerometer readings */
    int16_t accelX = getRawAccelX();
    int16_t accelY = getRawAccelY();
    int16_t accelZ = getRawAccelZ();
    
    /* Get raw gyro readings */
    int16_t gyroX = getRawGyroX();
    int16_t gyroY = getRawGyroY();
    int16_t gyroZ = getRawGyroZ();
    
  
    /* Send Accel readings */  
    SerialSendData( TEL_ACCELX, 1 );
    SerialSendData( accelX, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( TEL_ACCELY, 1 );
    SerialSendData( accelY, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( TEL_ACCELZ, 1 );
    SerialSendData( accelZ, 2 );
    Serial.write( ENDOFPCK );

    /* Send Gyro readings */
    SerialSendData( TEL_GYROX, 1 );
    SerialSendData( gyroX, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( TEL_GYROY, 1 );
    SerialSendData( gyroY, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( TEL_GYROZ, 1 );
    SerialSendData( gyroZ, 2 );
    Serial.write( ENDOFPCK );   
   
   /* TEMPORARY */
   unsigned long delta = getDelta();
   unsigned long maxdelta = getMaxDelta();
   unsigned long avgdelta = getAvgDelta();
   SerialSendData( TEL_DELTA, 1 );
   SerialSendData( delta, 4 );
   Serial.write( ENDOFPCK );
   SerialSendData( maxdelta, 4 );
   Serial.write( ENDOFPCK );
   SerialSendData( avgdelta, 4 );
   Serial.write( ENDOFPCK );
   
}

void anglesTelemetry(){    
  
    double p = getPitch()*1000;
    double r = getRoll()*1000;
    
    long pint = long(p);
    long rint = long(r);
  
    SerialSendData( TEL_PITCH, 1 );
    SerialSendData( pint, 4 );
    Serial.write( ENDOFPCK );
    SerialSendData( TEL_ROLL, 1 );
    SerialSendData( rint, 4 );
    Serial.write( ENDOFPCK );   
    
}

void alphaTelemetry(){

    double alphaAccel = getAccelLPFAlpha();
    double alphaGyro = getGyroLPFAlpha();
    double alphaDeg = getDegLPFAlpha();
    
    int alphaD = int(alphaDeg*100);
    int alphaA = int(alphaAccel*100);
    int alphaG = int(alphaGyro*100);
    
    SerialSendData( TEL_ALPHA_DEG, 1 );
    SerialSendData( alphaD, 1 );
    Serial.write( ENDOFPCK );  
    
    SerialSendData( TEL_ALPHA_ACCEL, 1 );
    SerialSendData( alphaA, 1 );
    Serial.write( ENDOFPCK );   
    
    SerialSendData( TEL_ALPHA_GYRO, 1 );
    SerialSendData( alphaG, 1 );
    Serial.write( ENDOFPCK );     
}

void PIDTelemetry(){
    int i,j;
    boolean PIDEnabled = isPIDEnabled();
    
    SerialSendData( TEL_PIDVALUES, 1 );
    
    // Send Enabled info
    if( PIDEnabled ) SerialSendData( 1, 1);
    else SerialSendData( 0, 1 );
    Serial.write( ENDOFPCK );

    // Send PID Values    
    for( i=0; i<2 ; i++ ){
        for( j=0 ; j<3 ; j++ ){
            int val = getPIDValues( i,j );
            SerialSendData(val, 4);
            Serial.write( ENDOFPCK );
        }
    }
    
//    Serial.write( TEL_OLEVELS );
//    
//    writeDouble( PIDGetPitchOLevel() );
//    Serial.write( ENDOFPCK );
//    writeDouble( PIDGetRollOLevel() );
//    Serial.write( ENDOFPCK );
    
}

void accelOffsetsTelemetry(){

    int aOffX = getAccelOffsetX();
    int aOffY = getAccelOffsetY();
    int aOffZ = getAccelOffsetZ();
  
    SerialSendData( TEL_ACCELOFFSETS, 1 );
    SerialSendData( aOffX, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( aOffY, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( aOffZ, 2 );
    Serial.write( ENDOFPCK );
    
}

void gyroOffsetsTelemetry(){

    int gOffX = getGyroOffsetX();
    int gOffY = getGyroOffsetY();
    int gOffZ = getGyroOffsetZ();
  
    SerialSendData( TEL_GYROOFFSETS, 1 );
    SerialSendData( gOffX, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( gOffY, 2 );
    Serial.write( ENDOFPCK );
    SerialSendData( gOffZ, 2 );
    Serial.write( ENDOFPCK );
    
    
}


void motorsPowerTelemetry(){
    
    int i;
  
    SerialSendData( TEL_MOTORSPOWER, 1 );
    
    for( i=1 ; i<=4 ; i++ ){
        if( getMotorPower(i) )
            SerialSendData(1, 1);
        else SerialSendData( 0, 1);
        Serial.write( ENDOFPCK );
    }    
}


void motorOffsetsTelemetry(){
    
    int i;
  
    SerialSendData( TEL_MOTOROFFSETS, 1 );
    
    for( i=1 ; i<=4 ; i++ ){
        SerialSendData(getMotorOffset(i), 1);
        Serial.write( ENDOFPCK );
    }    
}

void startTM(){  

    digitalWrite(SERIALPIN, HIGH);
    
    if( pendingTM ){
        byte IDv = getIDvisitor();
        SerialSendData( TELEMETRY + ( IDv << 4 ), 1 );
        
        switch( turn ){
            case 0:
//                motorsDiffTelemetry();
                turn++;
                break;
            case 1:
                accelGyroTelemetry();
                turn++;
                break;
            case 2:
                anglesTelemetry();
                turn = 0;
                break;
        }
        
        if( pendingMotorSpeedTM ){
            motorsSpeedTelemetry();
            pendingMotorSpeedTM = false;
        }else if( pendingPIDTM ){
            PIDTelemetry();
            pendingPIDTM = false;
        }else if( pendingAlphaTM ){
            alphaTelemetry();
            pendingAlphaTM = false;
        }else if( pendingAccelOffTM ){
            accelOffsetsTelemetry();
            pendingAccelOffTM = false;
        }else if( pendingGyroOffTM ){
            gyroOffsetsTelemetry();
            pendingGyroOffTM = false;
        }else if( pendingMotorsPowerTM ){
            motorsPowerTelemetry();
            pendingMotorsPowerTM = false;
        }else if( pendingMotorOffsetsTM ){
            motorOffsetsTelemetry();
            pendingMotorOffsetsTM = false;
        }
 
        
    endTM();       
    
    }
    digitalWrite(SERIALPIN, LOW);
}
  
void checkTelemetry(){
  
    if( (!pendingTM) && ((millis() - timer) >= TELEMETRYTIME) ){
        timer = millis();
        byte IDv = getIDvisitor();
        SerialSendData( TM_PETITION + ( IDv << 4 ), 1 );
        pendingTM = true;
    }    
}

void pendPIDTM(){ pendingPIDTM = true; }
void pendAlphaTM(){ pendingAlphaTM = true; }
void pendAccelOffTM(){ pendingAccelOffTM = true; }
void pendGyroOffTM(){ pendingGyroOffTM = true; }
void pendMotorsPowerTM(){ pendingMotorsPowerTM = true; }
void pendMotorOffsetsTM(){ pendingMotorOffsetsTM = true; }
void pendMotorSpeedTM(){ pendingMotorSpeedTM = true; }
void cancelTM(){ pendingTM = false; }
