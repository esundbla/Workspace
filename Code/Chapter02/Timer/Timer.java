
import sun.misc.Perf;
 
public class Timer 
{
   Perf Timer;
   long freq;
 
   public Timer() 
   {
     Timer = Perf.getPerf();
     freq = Timer.highResFrequency();
   }
 
   public long currentTimeMillis() 
   {
 	 return Timer.highResCounter() * 1000 / freq;
   }
}
 
 
