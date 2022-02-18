
	// Note: this function returns a double. to convert to, say, milliseconds multiply teh returned value by 1000. 
	// Note: 32 bit number takes about 9 min to rollOver (with freq of 3579545 (p4 1.8)), but a 64bit number it will take 2576688388288 seconds to rollOver ~ 81706 years :)

public class HighResolutionTimer
{
	private long	counterStart,
					counterEnd,
					frequency;
	
	private	double	elapsedTime,
					timeStart,
					frequencyInverse;
		
	private boolean	highResSupported;
	
	public HighResolutionTimer(){
		
		boolean success = QueryPerformanceCounter.loadNativeLibrary();
		
		if (!success){
			System.out.println("====> PerformanceCounter not available. System time will be used instead");
			highResSupported = false;	
			reset();
			return;
		}
		
	
		frequency = QueryPerformanceCounter.nativeQueryPerformanceFrequency();
		
		if (frequency == 0){
			System.out.println("====>PerformanceCounter not supported. System time will be used instead");
			highResSupported = false;	
			reset();
			return;
		}
						
		highResSupported = true;
		System.out.println("----> System performance counter frequency: " + frequency);
		frequencyInverse = 1.0/frequency;
	}
		
	
	public void reset(){
		if (highResSupported){
			counterStart = QueryPerformanceCounter.nativeQueryPerformanceCounter(); 
		}else{
			timeStart = System.currentTimeMillis();	
		}
	}
	
	public double getElapsedTimeInSeconds(){
		
		if (highResSupported){
			counterEnd = QueryPerformanceCounter.nativeQueryPerformanceCounter(); 		
			elapsedTime	= (counterEnd - counterStart) * frequencyInverse;			
		}else{
			elapsedTime = 	(System.currentTimeMillis() - timeStart) * 0.001;
		}
		
		return elapsedTime;
	}
}