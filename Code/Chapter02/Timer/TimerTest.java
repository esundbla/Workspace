
public class TimerTest 
{
   public static void main(String[] args) 
   {
    long start, stop;
    Timer testTimer = new Timer();
    start = testTimer.currentTimeMillis();
  
    while(testTimer.currentTimeMillis() == start);
   
     stop = testTimer.currentTimeMillis();
     System.out.println("Time: "+(stop-start)+" ms");
   }
} 