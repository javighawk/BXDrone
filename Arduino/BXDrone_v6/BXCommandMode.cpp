#include "BXCommandMode.h"

extern byte infoByte;

void BX_initCommandMode(){    
}
                          
void shortCutCommands(){
  
    digitalWrite(SERIALPIN, HIGH);
  
    switch(infoByte & COMMAND_SHORTCUT_MASK){
        case SHRTCMD_PID:
        
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
            break;       
   

       case SHRTCMD_TESTMOTORS:
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
           break;    


       case SHRTCMD_SWITCHPID:
       
           if( isPIDEnabled() )
               setPIDEnabled(false);
           else setPIDEnabled(true);
           
           pendPIDTM();
           
           break;
           
           
       case SHRTCMD_SETALPHA:                  /* STILL TO DEFINE */
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
           
           break;
    
           
       case SHRTCMD_SETOFFSETS:
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
           break;
            
        case SHRTCMD_SETOLEVELS:
           PIDSetCurrentOLevels();
           pendPIDTM();
           break;
            
        case SHRTCMD_STARTACCELOFFSETS:
           startAccelOffsets();
           break;      
      
        case SHRTCMD_STARTGYROOFFSETS:
           startGyroOffsets();
           break;    
          
        case SHRTCMD_SETMOTORPOWER:
           
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
           break;
           
           
        case SHRTCMD_SETMOTOROFFSET:
           
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
           break;
    }
    
    digitalWrite(SERIALPIN, LOW);
    
}
