/* Game.java
 * A simple implementation of the state driven core architecture.
 */

package book.fullscreen;

import java.io.*;
 
class Game
{
	static final int GAME_INIT 	   = 1;
	static final int GAME_MAIN 	   = 2;
	static final int GAME_SHUTDOWN = 4;
	static final int GAME_MENU	   = 8;
	
	
	private SpriteExample screen;
	
	public int gamestate;
		
	//default constructor
	public Game() 
	{
	 setState(GAME_INIT);
	}
		
	// all relevant caching of classes happens in this method.
	public void gameInit()
	{
	  
	  SpriteExample screen = new SpriteExample("Game Example",false);
	  	// put the window on screen using two buffers for the buffer strategy
		screen.initToScreen(2);
		// start the rendering thread
		screen.startRenderThread();
		setState(GAME_MAIN);
	}
	
	//This is the main point of execution of our game
	public void gameMain()
	{	
	  screen.doRender()
	  
	  
	  
	 //setState(GAME_MENU);
	 }
	
		//Clean up that Mess!!!
	public void gameShutdown()
	{
	  	  
	} 
	
	//draw the menu
	public void displayGameMenu()
	{
	  System.out.println("This is the game menu!");
	  System.out.println("");
	  System.out.println("Please make a choice for the game");
	  System.out.println("Select a to run the game!");
	  System.out.println("Select b to reset the menu!");
	  System.out.println("Select c to exit the game!");
	    	    
	  char enter;
	   	
	  try
	  {
	    enter = (char)System.in.read();
		  
	    //just the char please...	    
	    System.in.skip(3);
	    
	    switch(enter)
	    {
		  case 'a':
		  	setState(GAME_INIT);
		  	break;
		  case 'b':
		  	setState(GAME_MENU);
		  	break;
		  case 'c':
		  	setState(GAME_SHUTDOWN);
		  	break;
		  default:
		    System.out.println("Could not identify your choice"); 
		}
		
	  }
	  catch(IOException io)
	  {
	     System.out.println("BAD INPUT");
	  }	     
	}
	
	//directly change the state.
	public void setState(int state)
	{
	  gamestate = state;
	  update();
	}
	
	//This function will process the current game state and call the underlying functions
	//until the execution is terminated by input or breaking the game state.
	public void update()
	{
	  switch(gamestate)
	  {
		case GAME_INIT:
	  	  gameInit();
		  break;
		case GAME_MAIN:
		  gameMain();
		  break;
		case GAME_SHUTDOWN:
		  gameShutdown();
		  break;
		case GAME_MENU:
		  displayGameMenu();
		  break;
	  }			
	}
	
	//Let's run the game.
	public static void main(String args[]) 
	{
	  Game game = new Game();		
	}

}//eof Game.java

