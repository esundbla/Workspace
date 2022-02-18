package book.fullscreen;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;

public interface Drawable{
	
	public boolean isVisible();
	public void draw();
	public void draw(Image img, int x_Pos, int y_Pos);
		
}//end Drawable.java