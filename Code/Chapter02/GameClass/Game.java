/* Game.java
 * A simple implementation of the core architecture.
 */
 
class Game{
	
	public int playerlives;
	
	//default constructor
	public Game() {}
		
	// all relevant caching of classes happens in this method.
	public void gameInit()
	{
		System.out.println("The Game Init has occurred");
		playerlives = 5;
		System.out.println("");		
	}
	
	//This is the main point of execution 
	public void gameMain()
	{		
		System.out.println("The Game Main is executing");
		System.out.println("Darn, you died!");
		playerlives-=1;		
	}
	
	//Clean up that Mess!!!
	public void gameShutdown()
	{
		System.out.println("The Game has shutdown");
		System.out.println("");
	}

	//Let's run the game.
	public static void main(String args[]) 
	{
		Game game = new Game();
		
		//intialize our subsystems
		game.gameInit();
		
		//the game stays here till some termination condition is met
		while(game.playerlives>=0)
		{
		game.gameMain();
	    }
		System.out.println("");
		
		//cleanup
		game.gameShutdown();

	}
}
