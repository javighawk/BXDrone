#include "BXCommandMode.h"

/* infoByte declaration which carries the command info */
extern byte infoByte;

/* Functions declarations */
void CMD_PIDValues(); 
void CMD_TestMotors();
void CMD_SwitchPID(); 
void CMD_SetLPFAlpha(); 
void CMD_SetOffsets(); 
void CMD_Set0Levels(); 
void CMD_StartAccelOffsets(); 
void CMD_StartGyroOffsets(); 
void CMD_MotorsSwitch(); 
void CMD_SetMotorOffset(); 
void CMD_StopMotors();

void BX_initCommandMode(){    
}
                          
void shortCutCommands(){
  
    digitalWrite(SERIALPIN, HIGH);
  
    switch(infoByte & COMMAND_SHORTCUT_MASK){
       case SHRTCMD_PID: CMD_PIDValues(); break;
       case SHRTCMD_TESTMOTORS: CMD_TestMotors(); break;
       case SHRTCMD_SWITCHPID: CMD_SwitchPID(); break;
       case SHRTCMD_SETALPHA: CMD_SetLPFAlpha(); break;
       case SHRTCMD_SETOFFSETS: CMD_SetOffsets(); break;
       case SHRTCMD_SETOLEVELS: CMD_Set0Levels(); break;
       case SHRTCMD_STARTACCELOFFSETS: CMD_StartAccelOffsets(); break;
       case SHRTCMD_STARTGYROOFFSETS: CMD_StartGyroOffsets(); break;
       case SHRTCMD_SETMOTORPOWER: CMD_MotorsSwitch(); break;
       case SHRTCMD_SETMOTOROFFSET: CMD_SetMotorOffset(); break;
       case SHRTCMD_STOPMOTORS: CMD_StopMotors(); break;
    }
   
    digitalWrite(SERIALPIN, LOW);
}


void CMD_PIDValues(){
            byte pid, roll, value;
        
            while(!Serial.available()){
                if( checkTimeOut() ) return;
            }
            
            digitalWrite(SERIALPIN, LOW);
            roll = Serial.read();
            digitalWrite(SERIALPIN, HIGH);
            
            while(!Serial.available()){
                if( checkTimeOut() ) return;
            }
            
            digitalWrite(SERIALPIN, LOW);
            pid = Serial.read();
            digitalWrite(SERIALPIN, HIGH);
            
            while(!Serial.available()){
                if( checkTimeOut() ) return;
            }
            
            digitalWrite(SERIALPIN, LOW);
            value = Serial.read();
            digitalWrite(SERIALPIN, HIGH);
            
            if( roll==0xFF && pid==0xFF && value==0xFF ){
                setPIDValues(0, 0, DEFAULT_PVALUE);
                setPIDValues(0, 1, DEFAULT_IVALUE);
                setPIDValues(0, 2, DEFAULT_DVALUE);
                setPIDValues(1, 0, DEFAULT_PVALUE);
                setPIDValues(1, 1, DEFAULT_IVALUE);
                setPIDValues(1, 2, DEFAULT_DVALUE);
            } else setPIDValues( roll, pid, value );
            pendPIDTM();
}

void CMD_TestMotors(){
           byte motor;
           unsigned long time;
         
           setTotalSpeed(0);
           
           while(!Serial.available()){
                if( checkTimeOut() ) return;
           }   
           
           digitalWrite(SERIALPIN, LOW); 
           motor = Serial.read();
           digitalWrite(SERIALPIN, HIGH);
           
           setMotorSpeed(motor, 80);
           time = millis();
           
           while( millis() - time < 1000 );
           
           setTotalSpeed(0);
}

void CMD_SwitchPID(){
           if( isPIDEnabled() )
               setPIDEnabled(false);
           else setPIDEnabled(true);
           
           pendPIDTM();
}


void CMD_SetLPFAlpha(){
           byte alpha, device;
             
           while(!Serial.available()){
                if( checkTimeOut() ) return;
           }    
           
           digitalWrite(SERIALPIN, LOW);
           device = Serial.read();
           digitalWrite(SERIALPIN, HIGH);
           
           while(!Serial.available()){
                if( checkTimeOut() ) return;
           }    
           
           digitalWrite(SERIALPIN, LOW);
           alpha = Serial.read();
           digitalWrite(SERIALPIN, HIGH);
           
           if( alpha<0 || alpha>100 ) return;
           
           switch( device ){
               
               case 0: setDegLPFAlpha( (double) alpha/100 ); break;
               case 1: setAccelLPFAlpha( (double) alpha/100 ); break;
               case 2: setGyroLPFAlpha( (double) alpha/100 ); break;
           }
           
           pendAlphaTM();
}

void CMD_SetOffsets(){
           byte dev, axis;
           int val;
           
           while(!Serial.available()){
                if( checkTimeOut() ) return;
           }
           
           digitalWrite(SERIALPIN, LOW);
           dev = Serial.read();
           digitalWrite(SERIALPIN, HIGH);
           
           while(!Serial.available()){
                if( checkTimeOut() ) return;
           }
           
           digitalWrite(SERIALPIN, LOW);
           axis = Serial.read();
           digitalWrite(SERIALPIN, HIGH);
           
           if( axis != 0xFF ){
           
               while(!Serial.available()){
                    if( checkTimeOut() ) return;
                }
                
               digitalWrite(SERIALPIN, LOW);
               val = Serial.read();
               digitalWrite(SERIALPIN, HIGH);
               
               while(!Serial.available()){
                    if( checkTimeOut() ) return;
                }
                
               digitalWrite(SERIALPIN, LOW);
               val = (val << 8) + Serial.read();
               digitalWrite(SERIALPIN, HIGH);
           }
           
           setOffsets(dev, axis, val);
           
           pendAccelOffTM();
           pendGyroOffTM();
}

void CMD_Set0Levels(){
           PIDSetCurrentOLevels();
           pendPIDTM();
}

void CMD_StartAccelOffsets(){ startAccelOffsets(); }
void CMD_StartGyroOffsets(){ startGyroOffsets(); }

void CMD_MotorsSwitch(){
           boolean pow;
           int m;
           while(!Serial.available()){
                if( checkTimeOut() ) return;
            }
          
           digitalWrite(SERIALPIN, LOW);
           m = Serial.read();
           digitalWrite(SERIALPIN, HIGH);
          
           pow = !getMotorPower(m);
           setMotorPower( m, pow );
           pendMotorsPowerTM();  
}

void CMD_SetMotorOffset(){
           byte mot, sp;
           
           while(!Serial.available()){
                if( checkTimeOut() ) return;
           }
           digitalWrite(SERIALPIN, LOW);
           mot = Serial.read();
           digitalWrite(SERIALPIN, HIGH);
          

           while(!Serial.available()){
                if( checkTimeOut() ) return;
           }
           digitalWrite(SERIALPIN, LOW);
           sp = Serial.read();
           digitalWrite(SERIALPIN, HIGH);

           setMotorOffset( mot, sp );
           pendMotorOffsetsTM();  
}

void CMD_StopMotors(){
           setTotalSpeed(0);
           Serial.write(EOT + (getIDvisitor() << 4) );
           pendMotorSpeedTM();
}

