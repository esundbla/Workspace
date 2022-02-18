package book.fullscreen;

import java.awt.DisplayMode;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.net.URL;
import java.util.Random;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.Transparency;
/**
 * @author Scott Shaver && Dustin Clingman
 *
 * A simple Game construct
 */
public class SimpleGame extends FullScreenFrame3
{
	
	static final int GAME_INIT 	   = 1;
	static final int GAME_MAIN 	   = 2;
	static final int GAME_SHUTDOWN = 4;

    // The resource location of the image to display 
	private static final String imageResourceName = "/shau.png";

    private Player temp;	
	
	// The handle to the buffered image we will load and display
	private BufferedImage bufferedImage = null;
	
	/**
	 * The handle to the image we will load and display
	 */
	private Image image = null;
	private Image image2 = null;
	
	public ActorManager am;	
	
	//current gamestate
	public int gamestate;

	//Create a new full screen mode window with the specified title and video mode.
	public SimpleGame(String title, DisplayMode mode) throws HeadlessException
	{
		super(title,mode);
				
		am = new ActorManager();
		setState(GAME_INIT);
		
	}

	
	//Create a windowed mode window with the specified title and video mode.
	public SimpleGame(String title, boolean undecorated) throws HeadlessException
	{
		super(title,undecorated);
		
    	am = new ActorManager();
		setState(GAME_INIT);
	}
	
	
	// all relevant caching of classes happens in this method.
	public void gameInit()
	{
		
	  URL imageURL = getClass().getResource(imageResourceName);
		if(imageURL==null)
		{
			System.out.println("Unable to locate the resource "+imageResourceName);
			return;
		}
		loadBufferedImage(imageURL);
		loadImage5(imageURL);
		
		
		
		
	  initToScreen(2);
	  // start the rendering thread
	  startRenderThread();
	  am.createEntity("Player",1);
	  am.createEntity("Enemy", 1); 
	  
      //Lets add some input to update the player postion on screen.
      addKeyListener(new PlayerAdapter((Player)am.actorList.getFirst()));
         
	  setState(GAME_MAIN);
	}
	
	//This is the main point of execution of our game
	public void gameMain()
	{	
	    Enemy  tempEnemy;
		while(gamestate == GAME_MAIN)
		{
		  
		  
		  tempEnemy  = (Enemy)am.actorList.get(1);
          tempEnemy.update((Player)am.actorList.get(0));

//		  
//          for (int i = 0; i<am.actorList.size(); i++) 
//            { 
//              temp = (Actor)am.actorList.get(i);   
//              temp.update();
//            }
		 
	    }
        setState(GAME_SHUTDOWN);
    }
		
	//Clean up that Mess!!!
	public void gameShutdown()
	{	
	  System.out.println("The game is shutting down.");
	  am.clearEntities();
	  System.exit(0); 
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
	  }	
	}	
	
		
	/**
	 * Read in the image to display and put it in 
	 * a BufferedImage object.
	 */
	private void loadBufferedImage(URL imageURL)
	{
		try
		{
			bufferedImage = ImageIO.read(imageURL);
		}
		catch(IOException x)
		{
			x.printStackTrace();
		}
	}
	
	
	/**
	 * Read in the image to display using the GraphicsConfiguration 
	 * class's method createCompatiableImage(width,height,transparency)
	 */
	private void loadImage5(URL imageURL)
	{
		
		
		image = getGraphicsConfiguration().createCompatibleImage(
			bufferedImage.getWidth(),
			bufferedImage.getHeight(),
			Transparency.BITMASK);
		image.getGraphics().drawImage(bufferedImage,0,0,this);
	}

	 // This method does the actual drawing. You should override this method.
	 // Don't call this method directly, always call doRender() or let the rendering
	 // thread do the calls for you.
	 
	public boolean render(Graphics g) 
	{
		super.render(g);
		Player a;
		Enemy  b;
			
		if(image!=null)
		{
//		  a = (Player)am.actorList.get(0);
//	   	  g.drawImage(image,a.getX(),a.getY(),this);
	   	 
	   	  b = (Enemy)am.actorList.get(1);
	      g.drawImage(image,b.getX(),b.getY(),this);

		}		
		return true;
	}


	//Can be left as is.
    public static void main(String[] args)
    {
		DisplayMode newMode = null;
		
		// see if the user wants to force windowed mode even if full screen mode is available
		boolean forceWindowedMode = false;
		if(args.length >= 1)
			if(args[0].equalsIgnoreCase("windowed"))
				forceWindowedMode = true;
		
		// we need to make sure the system defualt display can support full screen mode, if it can't 
		//we will run in windowed mode
		boolean fullScreenMode = false;
		if(defaultScreenDevice.isFullScreenSupported())
		{
			fullScreenMode = true;
				
			// try to get one of the modes we really want
			newMode = findRequestedMode();
			
			// if the mode doesn't exist then go into windowed mode or use full screen mode
			if(newMode==null)
				fullScreenMode = false;
		}
		else
			System.out.println("This system doesn't support full screen mode.");
			
		SimpleGame myFrame = null;
			
		if(fullScreenMode && !forceWindowedMode)
			myFrame = new SimpleGame("SimpleGame Full Screen Mode", newMode);
		else
			myFrame = new SimpleGame("SimpleGame Windowed Mode", false);
			
		
	}
}
