package book.fullscreen;

import java.awt.DisplayMode;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.awt.Graphics;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

/**
 * @author Scott Shaver
 *
 * How to use volatile images.
 */
public class VolatileExample extends FullScreenFrame3
{
	/**
	 * The resource location of the image to display 
	 */
	private static final String imageResourceName = "/rock.png";
	
	/**
	 * The handle to the buffered image we will load and
	 * use to restore the content of the volatile image if
	 * it is destroyed.
	 */
	private BufferedImage bufferedImage = null;
	
	/**
	 * The handle to the volatile image we will display
	 */
	private VolatileImage image = null;
	
	/**
	 * Create a new full screen mode window with the specified title and video mode.
	 * 
     * @param title
     * @param mode
     * @throws HeadlessException
     */
    public VolatileExample(String title, DisplayMode mode) throws HeadlessException
    {
        super(title, mode);
		loadImage();
    }

    /**
	 * Create a windowed mode window with the specified title and video mode.
	 * 
     * @param title
     * @param undecorated
     * @throws HeadlessException
     */
    public VolatileExample(String title, boolean undecorated) throws HeadlessException
    {
        super(title, undecorated);
		loadImage();
    }

	/**
	 * Read in the image to display and put it in a VolatileImage object.
	 */
	private void loadImage()
	{
		try
		{
			URL imageURL = getClass().getResource(imageResourceName);
			if(imageURL==null)
			{
				System.out.println("Unable to locate the resource "+imageResourceName);
				return;
			}
			bufferedImage = ImageIO.read(imageURL);
			image = getGraphicsConfiguration().createCompatibleVolatileImage(bufferedImage.getWidth(),bufferedImage.getHeight());
			image.getGraphics().drawImage(bufferedImage,0,0,this);
		}
		catch(IOException x)
		{
			x.printStackTrace();
		}
	}
	
	/**
	 * This method does the actuall drawing you should override this method.
	 * Don't call this method directly, always call doRender() or let the 
	 * rendering thread do the calls for you.
	 */
	public boolean render(Graphics g) 
	{
		super.render(g);
		Random rand = new Random();
		
		if(image!=null)
		{
			for(int loop=0; loop<100; loop++)
			{
				int returnCode = image.validate(getGraphicsConfiguration());
				
				if (returnCode == VolatileImage.IMAGE_RESTORED) 
				{
					// the contents of the volatile image need to be restored
					image.getGraphics().drawImage(bufferedImage,0,0,this);
				}
				else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE)
				{
					// the old volatile image is icompatible with the graphics configuration
					image = getGraphicsConfiguration().createCompatibleVolatileImage(bufferedImage.getWidth(),bufferedImage.getHeight());
					image.getGraphics().drawImage(bufferedImage,0,0,this);
				}
				g.drawImage(image,rand.nextInt(getWidth()),rand.nextInt(getHeight()),this);
			}
		}		
		return true;
	}

    public static void main(String[] args)
    {
		DisplayMode newMode = null;
		
		// see if the user wants to force windowed mode
		// even if full screen mode is available
		boolean forceWindowedMode = false;
		if(args.length >= 1)
			if(args[0].equalsIgnoreCase("windowed"))
				forceWindowedMode = true;
		
		// we need to make sure the system defualt display can
		// support full screen mode, if it can't we will run
		// in windowed mode
		boolean fullScreenMode = false;
		if(defaultScreenDevice.isFullScreenSupported())
		{
			fullScreenMode = true;
				
			// dump a list of the available display modes to the console
			VolatileExample.printDisplayModes();
				
			// try to get one of the modes we really want
			newMode = findRequestedMode();
			
			// if the mode doesn't exist then go into windowed mode
			// otherwise use full screen mode
			if(newMode==null)
				fullScreenMode = false;
		}
		else
			System.out.println("This system doesn't support full screen mode.");
			
		VolatileExample myFrame = null;
			
		if(fullScreenMode && !forceWindowedMode)
			myFrame = new VolatileExample("VolatileExample Full Screen Mode", newMode);
		else
			myFrame = new VolatileExample("VolatileExample Windowed Mode", false);
			
		// put the window on screen using two buffers for the buffer strategy
		myFrame.initToScreen(2);
		// start the rendering thread
		myFrame.startRenderThread();
    }
}
