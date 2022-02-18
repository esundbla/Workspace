
import net.java.games.jogl.*;
import java.util.*;

public class HTextList extends HTextComponent{

	ArrayList 	texts;

	protected int viewableLines = 1;
	protected int viewableChars = 1;


	public HTextList(){
		texts = new ArrayList();
	}


	public void setBounds(int x, int y, int w, int h){
		super.setBounds(x, y, w, h);
		viewableLines = h/16;
		viewableChars = w/10;
	}


	public void paint(GL gl){
		int gx1 = container.x+x, 
			gx2 = container.x+x+w,
			gy1 = container.y+y,
			gy2 = container.y+y+h;

		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);			

		gl.glBegin(GL.GL_QUADS);       
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3i(gx1, gy2,  0);  
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(gx2, gy2,  0);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(gx2, gy1,  0);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(gx1, gy1,  0);
		gl.glEnd();	


		//----------------- paint text	
		int tgy = container.y+(y+h) - 16;
		for(int i=texts.size()-1; i>=0 && i> texts.size()-1-viewableLines; i--){
			paintText(gl, container.x+x, tgy, (String)texts.get(i), viewableChars, textureIdFont);
			tgy -=16;
		}
	}
}
