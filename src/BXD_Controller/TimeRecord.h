#ifndef TimeRecord_HPP
#define TimeRecord_HPP


/*****************************************/
/**************** INCLUDES ***************/
/*****************************************/

#include "Arduino.h"

/*****************************************/
/**************** DEFINES ****************/
/*****************************************/

#define AVG_RECORDS      1000
#define LABEL_LENGTH        6

/*****************************************/
/**************** VARIABLES **************/
/*****************************************/


/*******************************************/
/**************** FUNCTIONS ****************/
/*******************************************/

class TimeRecord{
  public:
      TimeRecord(String label);
      void trigger();
      void stop();
      void resetMaxTRec(){_tRec_max = 0;};
      unsigned long getTime(){ return _tRec; }
      unsigned long getMaxTime(){ return _tRec_max; }
      unsigned long getAvgTime(){ return (unsigned long)(_tRec_avg/_iter); }
      String getLabel(){ return _label; }
  private:
      unsigned long _tRec;
      unsigned long _tRec_back;
      unsigned long _tRec_max;
      unsigned long _tRec_avg;
      String _label;
      unsigned int _iter;
};

#endif
