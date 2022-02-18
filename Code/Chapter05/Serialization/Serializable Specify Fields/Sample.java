
import java.io.*;

class Sample{
	
	Actor[] actors;
	
	public void initialize(){
		actors = new Actor[3];
		
		// initialize actors
		for(int i=0; i<actors.length; i++){
			Actor actor = new Actor();
			actor.name 	= "actor" + i;
			actor.id 	= i;
			actor.position = new Point((float)Math.random()*100, 0, (float)Math.random()*100);
			
			actors[i] = actor;
		}		
	}	
	
	public void printActors(){

		System.out.println("------- printActors {");
		
		for(int i=0; i<actors.length; i++){
			Actor actor = actors[i];
			System.out.println("Actor:");
			System.out.println("	name: " + actor.name);
			System.out.println("	id: " + actor.id);
			System.out.println("	point: " + actor.position.x + ", " + actor.position.y + ", " + actor.position.z);
		}					

		System.out.println("------- printActors }");
	}
	
	public void test() throws Exception{
		
		initialize();
		printActors();
		
		System.out.println("------- writing");
		FileOutputStream fos = new FileOutputStream("actors");	
		ObjectOutputStream oos = new ObjectOutputStream (fos);		
		oos.writeObject(actors);
		oos.close(); // flush, clear, close
		
				
		actors = null;
		
		
		System.out.println("------- reading");
		FileInputStream fis = new FileInputStream("actors");	
		ObjectInputStream ois = new ObjectInputStream(fis);		
		actors = (Actor[])ois.readObject();
		
		printActors();	
	}

 	public static void main(String args[]) {
		Sample sample = new Sample();
				
		try{
			sample.test();			
		}catch(Exception e){
			e.printStackTrace();	
		}
	}
}
