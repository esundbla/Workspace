
public class Sample {

	public static void main(String args[]) {
		
		//TODO: remove this line so the dll info is not displayed 
		JoystickWin32Ex.showInfo();

		while (true) {
				
			// poll and check for error	
			if (JoystickWin32Ex.poll(false)){
				JoystickWin32Ex.dumpJoyInfoExStruct(0);	
			}else{
				JoystickWin32Ex.dumpErrors();
			}
			
			
			// sleep a bit
			try {
				 Thread.currentThread().sleep(1000/10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
