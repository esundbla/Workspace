
public class HighResolutionTimer
{
    private long    counterStart,
                    counterEnd,
                    frequency;
    
    private double  elapsedTime,
                    timeStart,
                    frequencyInverse;
        
    private boolean highResSupported;
    
    public HighResolutionTimer(){
        
        boolean success = QueryPerformanceCounter.loadNativeLibrary();
                
        if (success){
            frequency = QueryPerformanceCounter.nativeQueryPerformanceFrequency();
        }   
        
        if (success && frequency != 0){
            highResSupported = true;
            System.out.println("----> System performance counter frequency: " + frequency);
            frequencyInverse = 1.0/frequency;
            reset();
            return;
        }

        System.out.println("====> WARNING: PerformanceCounter not supported. System time will be used instead");
        reset();
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
            elapsedTime = (counterEnd - counterStart) * frequencyInverse;           
        }else{
            elapsedTime =   (System.currentTimeMillis() - timeStart) * 0.001;
        }
        
        return elapsedTime;
    }
}