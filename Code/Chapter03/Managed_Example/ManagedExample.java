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
 * @author Scott Shaver
 *
 * How to use managed images.
 */
public class ManagedExample extends FullScreenFrame3
{
	/**
	 * The resource location of the image to display 
	 */
	private static final String imageResourceName = "/rock.png";
	
	/**
	 * The handle to the buffered image we will load and
	 * use to create the managed images
	 */
	private BufferedImage bufferedImage = null;
	
	/**
	 * The handle to the image we will load and display
	 */
	private Image image = null;
	
    /**
	 * Create a new full screen mode window with the specified title and video mode.
	 * 
     * @param title
     * @param mode
     * @throws HeadlessException
     */
    public ManagedExample(String title, DisplayMode mode) throws HeadlessException
    {
        super(title, mode);
        
		URL imageURL = getClass().getResource(imageResourceName);
		if(imageURL==null)
		{
			System.out.println("Unable to locate the resource "+imageResourceName);
			return;
		}
		
		loadBufferedImage(imageURL);
		loadImage5(imageURL);
    }

    /**
	 * Create a windowed mode window with the specified title and video mode.
	 * 
     * @param title
     * @param undecorated
     * @throws HeadlessException
     */
    public ManagedExample(String title, boolean undecorated) throws HeadlessException
    {
        super(title, undecorated);
        
		URL imageURL = getClass().getResource(imageResourceName);
		if(imageURL==null)
		{
			System.out.println("Unable to locate the resource "+imageResourceName);
			return;
		}
		
		loadBufferedImage(imageURL);
		loadImage5(imageURL);
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
	 * Read in the image to display using the swing ImageIcon class
	 */
	private void loadImage1(URL imageURL)
	{
		image = new ImageIcon(imageURL).getImage();
	}
	
	/**
	 * Read in the image to display using the Toolkit 
	 * class's method createImage(URL)
	 */
	private void loadImage2(URL imageURL)
	{
		image = Toolkit.getDefaultToolkit().createImage(imageURL);
	}
	
	/**
	 * Read in the image to display using the Toolkit class's method getImage(URL)
	 */
	private void loadImage3(URL imageURL)
	{
		image = Toolkit.getDefaultToolkit().getImage(imageURL);
	}
	
	/**
	 * Read in the image to display using the 
	 * Component class's method createImage(width,height)
	 */
	private void loadImage4(URL imageURL)
	{
		pack(); // the component must be displayable to call createImage
		image = createImage(bufferedImage.getWidth(),bufferedImage.getHeight());
		Graphics2D g2d = (Graphics2D)image.getGraphics(); 
		g2d.setComposite(AlphaComposite.Src);
		g2d.drawImage(bufferedImage,0,0,this);
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
	
	/**
	 * This method does the actuall drawing you should 
	 * override this method. Don't call this method directly, 
	 * always call doRender() or let the rendering
	 * thread do the calls for you.
	 */
	public boolean render(Graphics g) 
	{
		super.render(g);
		Random rand = new Random();
		
		if(image!=null)
		{
			for(int loop=0; loop<10; loop++)
				g.drawImage(image,rand.nextInt(getWidth()),rand.nextInt(getHeight()),this);
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
			ManagedExample.printDisplayModes();
				
			// try to get one of the modes we really want
			newMode = findRequestedMode();
			
			// if the mode doesn't exist then go into windowed mode
			// otherwise use full screen mode
			if(newMode==null)
				fullScreenMode = false;
		}
		else
			System.out.println("This system doesn't support full screen mode.");
			
		ManagedExample myFrame = null;
			
		if(fullScreenMode && !forceWindowedMode)
			myFrame = new ManagedExample("ManagedExample Full Screen Mode", newMode);
		else
			myFrame = new ManagedExample("ManagedExample Windowed Mode", false);
			
		// put the window on screen using two buffers for the buffer strategy
		myFrame.initToScreen(2);
		// start the rendering thread
		myFrame.startRenderThread();
    }
}
