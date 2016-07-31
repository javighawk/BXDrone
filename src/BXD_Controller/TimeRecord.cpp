#include "TimeRecord.h"

/*
 * Constructor
 */
TimeRecord::TimeRecord(String label){
    _tRec = 0;
    _tRec_back = 0;
    _tRec_max = 0;
    _tRec_avg = 0;
    _iter = AVG_RECORDS;
    _label = label;
}

/*
 * Trigger the time recording
 */
void TimeRecord::trigger(){
    _tRec_back = micros();
}

/*
 * Stops the time recording
 */
void TimeRecord::stop(){

    /* Record the time difference */
    _tRec = micros() - _tRec_back;

    /* Record maximum value */
    if( _tRec > _tRec_max ) _tRec_max = _tRec;

    /* Compute the average */
    _iter = (_iter % AVG_RECORDS) + 1;
    _tRec_avg = _tRec + (_tRec_avg * (_iter != 1));
}
