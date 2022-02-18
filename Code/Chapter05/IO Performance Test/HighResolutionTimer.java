
	// Note: this function returns a double. to convert to, say, milliseconds multiply teh returned value by 1000. 
	// Note: 32 bit number takes about 9 min to rollOver (with freq of 3579545 (p4 1.8)), but a 64bit number it will take 2576688388288 seconds to rollOver ~ 81706 years :)

public class HighResolutionTimer
{
	private long	counterStart,
					counterEnd,
					frequency;
	
	private	double	elapsedTime,
					frequencyInverse;
		
	
	public HighResolutionTimer(){
				
		frequency = QueryPerformanceCounter.nativeQueryPerformanceFrequency();
		
		if (frequency == 0){
			System.out.println("====> System does NOT support high-res performance counter <====");
		}else{			
			frequencyInverse = 1.0/frequency;
			reset();
			System.out.println("====> System performance counter frequency: " + frequency);
		}
	}
		
	
	public void reset(){
		counterStart = QueryPerformanceCounter.nativeQueryPerformanceCounter(); 
		elapsedTime = 0;
	}
	
	public double getElapsedTimeInSeconds(){
		
		counterEnd = QueryPerformanceCounter.nativeQueryPerformanceCounter(); 		
	
		elapsedTime	= (counterEnd - counterStart) * frequencyInverse;			
	
		return elapsedTime;
	}
	
	
	
}