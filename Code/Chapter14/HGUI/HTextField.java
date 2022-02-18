

import net.java.games.jogl.*;
import java.awt.event.*;


public class HTextField extends HTextComponent{

	//---------  Actions  -------------
	public static final int ACTION_ENTER = 1;	
	//---------------------------------

	public HTextField(){
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

		paintText(gl);		
	}


	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_RELEASED:

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_ENTER){
						if (listener != null){
							listener.actionPerformed(this, ACTION_ENTER);
						}
						return;
					}				
					break;	
			}			
		}

		super.processInputEvent(event);					
	}

}