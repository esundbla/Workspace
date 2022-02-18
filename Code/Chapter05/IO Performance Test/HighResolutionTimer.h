
/**
 *  This class is a timer that uses the QueryPerformanceCounter to 
 *  compute high resolution elapsed time. getElapsedTimeInSeconds()
 *  returns a double that indicated the amount of time elapsed since 
 *  last reset(). 
 */


//WARNING: the getElapsedTimeInSeconds() compensates for counter roll over, but it has not been tested
//         when counter rolls over a dialogBox is poped up that shows the elapsed time. you should comments out
//         if you are not concered with the roll over. (very unlikely. the counter is 0 when the machine starts up
//         and is 64 bit

//TODO: test the roll over 
//TODO: add a setElapsedTime() to help a clean recovery after a game pause
//NOTE: this class should not be a singleton ;)

#ifndef HIGH_RESOLUTION_TIMER_H
#define HIGH_RESOLUTION_TIMER_H

#include <windows.h>

class HighResolutionTimer  
{
public:
	
	HighResolutionTimer();
	virtual ~HighResolutionTimer();
	
	// returns a high resolution elapsed time 
	double getElapsedTimeInSeconds();	

	//resets the timer
	void reset();	


private:

	LARGE_INTEGER	counterStart,
					counterEnd;


	unsigned long frequency;

	double	elapsedTime,
			frequencyInverse;
};

#endif
