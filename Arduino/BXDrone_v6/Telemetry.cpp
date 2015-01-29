#include "Telemetry.h"

/* Auxiliar variable to measure Telemetry period */
unsigned long time;

/* Turn for TM */
int turn = 0;

/* Auxiliary variables */
boolean pendingTM = false;

/* Other TM Pending Boolean Variables */
boolean pendingPIDTM = true;
boolean pendingAlphaTM = true;
boolean pendingAccelOffTM = true;
boolean pendingGyroStillTM = true;
boolean pendingMotorsPowerTM = true;
boolean pendingMotorOffsetsTM = true;

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
      Serial.write(p.charAt(i));
    }
}

void endTM(){
    byte IDv = getIDvisitor();
    Serial.write( ENDOFTM + ( IDv << 4 ) );
    pendingTM = false;
}
    

void motorsTelemetry(){
    
    /* Motors speed */
    int m1s = getMotorSpeed(1), 
        m2s = getMotorSpeed(2), 
        m3s = getMotorSpeed(3), 
        m4s = getMotorSpeed(4);

    // Send Motors speed
    Serial.write( TEL_SPEED );
    Serial.write( m1s >> 8 );
    Serial.write( m1s );
    Serial.write( ENDOFPCK );
    Serial.write( m2s >> 8 );
    Serial.write( m2s );
    Serial.write( ENDOFPCK );
    Serial.write( m3s >> 8 );
    Serial.write( m3s );
    Serial.write( ENDOFPCK );
    Serial.write( m4s >> 8 );
    Serial.write( m4s );
    Serial.write( ENDOFPCK );

    
    // Send differential Motors speed
    Serial.write( TEL_DIFFMOTOR );
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
  
    // Send G components (readings from accelerometer)  
    Serial.write( TEL_X );
    writeDouble( getXG() );
    Serial.write( ENDOFPCK );
    Serial.write( TEL_Y );
    writeDouble( getYG() );
    Serial.write( ENDOFPCK );
    Serial.write( TEL_Z );        
    writeDouble( getZG() );
    Serial.write( ENDOFPCK );
    
    double dPitch = getGyroDeltaPitch();
    double dRoll = getGyroDeltaRoll();
        
    Serial.write( TEL_DELTAPITCH );
    writeDouble( dPitch );
    Serial.write( ENDOFPCK );
    Serial.write( TEL_DELTAROLL );
    writeDouble( dRoll );
    Serial.write( ENDOFPCK );    
    
    
}

void degreesTelemetry(){    
  
    Serial.write( TEL_PITCH );
    writeDouble( getPitch() );
    Serial.write( ENDOFPCK );
    Serial.write( TEL_ROLL );
    writeDouble( getRoll() );
    Serial.write( ENDOFPCK );
    
    
    
}

void alphaTelemetry(){
    
    // Send LPF Alpha parameter
    double alphaAccel = getAccelLPFAlpha();
    Serial.write( TEL_ALPHA_ACCEL );
    writeDouble( alphaAccel );
    Serial.write( ENDOFPCK );
    double alphaGyro = getGyroLPFAlpha();
    Serial.write( TEL_ALPHA_GYRO );
    writeDouble( alphaGyro );
    Serial.write( ENDOFPCK );
    double alphaDeg = getDegreesLPFAlpha();
    Serial.write( TEL_ALPHA_DEG );
    writeDouble( alphaDeg );
    Serial.write( ENDOFPCK );    
}

void PIDTelemetry(){
    int i,j;
    boolean PIDEnabled = isPIDEnabled();
    
    Serial.write( TEL_PIDVALUES );
    
    // Send Enabled info
    if( PIDEnabled ) Serial.write( 1 );
    else Serial.write( 0 );
    Serial.write( ENDOFPCK );

    // Send PID Values    
    for( i=0; i<2 ; i++ ){
        for( j=0 ; j<3 ; j++ ){
            int val = getPIDValues( i,j );
            Serial.write(val >> 24);
            Serial.write(val >> 16);
            Serial.write(val >> 8);
            Serial.write(val);
            Serial.write( ENDOFPCK );
        }
    }
    
    Serial.write( TEL_OLEVELS );
    
    writeDouble( PIDGetPitchOLevel() );
    Serial.write( ENDOFPCK );
    writeDouble( PIDGetRollOLevel() );
    Serial.write( ENDOFPCK );
    
}

void accelOffsetsTelemetry(){
  
    Serial.write( TEL_OFFSETS );
    
    Serial.write( getAccelOffset(0) );
    Serial.write( ENDOFPCK );
    Serial.write( getAccelOffset(1) );
    Serial.write( ENDOFPCK );
    Serial.write( getAccelOffset(2) );
    Serial.write( ENDOFPCK );
    
    
}

void gyroStillValueTelemetry(){

    Serial.write( TEL_STILLLEVELS );
    
    writeDouble( getGyroXStillLevel() );
    Serial.write( ENDOFPCK );
    writeDouble( getGyroZStillLevel() );
    Serial.write( ENDOFPCK );
    
    
}


void motorsPowerTelemetry(){
    
    int i;
  
    Serial.write( TEL_MOTORSPOWER );
    
    for( i=1 ; i<=4 ; i++ ){
        if( getMotorPower(i) )
            Serial.write(1);
        else Serial.write(0);
        Serial.write( ENDOFPCK );
    }    
}


void motorOffsetsTelemetry(){
    
    int i;
  
    Serial.write( TEL_MOTOROFFSETS );
    
    for( i=1 ; i<=4 ; i++ ){
        Serial.write(getMotorOffset(i));
        Serial.write( ENDOFPCK );
    }    
}

void startTM(){
//    byte IDv = getIDvisitor();
//    Serial.write( TELEMETRY + ( IDv << 4 ) );
//    
//    digitalWrite(SERIALPIN, HIGH);    
//    while( !Serial.available() ){
//        if( checkTimeOut() ) return false;
//    }
//    digitalWrite(SERIALPIN, LOW);
//   
//    return true;
//    

    digitalWrite(SERIALPIN, HIGH);
    
    if( pendingTM ){
        byte IDv = getIDvisitor();
        Serial.write( TELEMETRY + ( IDv << 4 ) );
        
        switch( turn ){
            case 0:
                motorsTelemetry();
                turn++;
                break;
            case 1:
                accelGyroTelemetry();
                degreesTelemetry();
                turn = 0;
                break;
        }
        
        if( pendingPIDTM ){
            PIDTelemetry();
            pendingPIDTM = false;
        }else if( pendingAlphaTM ){
            alphaTelemetry();
            pendingAlphaTM = false;
        }else if( pendingAccelOffTM ){
            accelOffsetsTelemetry();
            pendingAccelOffTM = false;
        }else if( pendingGyroStillTM ){
            gyroStillValueTelemetry();
            pendingGyroStillTM = false;
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
  
    if( (!pendingTM) && ((millis() - time) >= TELEMETRYTIME) ){
        time = millis();
        byte IDv = getIDvisitor();
        Serial.write( TM_PETITION + ( IDv << 4 ) );
        pendingTM = true;
    }    
}

void pendPIDTM(){ pendingPIDTM = true; }
void pendAlphaTM(){ pendingAlphaTM = true; }
void pendAccelOffTM(){ pendingAccelOffTM = true; }
void pendGyroStillTM(){ pendingGyroStillTM = true; }
void pendMotorsPowerTM(){ pendingMotorsPowerTM = true; }
void pendMotorOffsetsTM(){ pendingMotorOffsetsTM = true; }
void cancelTM(){ pendingTM = false; }
