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
 * How to use managed images.
 */
public class SpriteExample extends FullScreenFrame3
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
	 */
    public SpriteExample(String title, DisplayMode mode) throws HeadlessException
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
	 */
    public SpriteExample(String title, boolean undecorated) throws HeadlessException
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
	 * This method does the actual drawing you should 
	 * override this method. Don't call this method directly, 
	 * always call doRender() or let the rendering
	 * thread do the calls for you.
	 */
	public boolean render(Graphics g) 
	{
		super.render(g);
				
		if(image!=null)
		{
			g.drawImage(image,10,10,this);
		}
		return true;
	}
}

