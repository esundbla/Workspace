
public class Sample {

	public static void main(String args[]) {
	
		HighResolutionTimer highResolutionTimer = new HighResolutionTimer();
		
		while (true) {
			
			System.out.println("Elapsed Time: " + highResolutionTimer.getElapsedTimeInSeconds());
			
			try {
				 Thread.currentThread().sleep(1000/10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
