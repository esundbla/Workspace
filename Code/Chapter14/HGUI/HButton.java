
import net.java.games.jogl.*;
import java.awt.event.*;

public class HButton extends HComponent{

	//---------  Actions  -------------
	public static final int ACTION_CLICKED 		= 1;	
	public static final int ACTION_CHECKED 		= 2;	
	public static final int ACTION_UNCHECKED 	= 3;	
	//---------------------------------

	int textureIdPressed = -1;

	boolean pressed = false;	
	boolean toggleButton = false;


	public HButton(){
	}

	public void paint(GL gl){

		int gx1 = container.x+x, 
			gx2 = container.x+x+w,
			gy1 = container.y+y,
			gy2 = container.y+y+h;

		if (pressed){
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdPressed);
			gx1 += 2;
			gx2 -= 2;
			gy1 += 2;
			gy2 -= 2;

		}else{
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);			
		}

		gl.glBegin(GL.GL_QUADS);       
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3i(gx1, gy2,  0);  
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3i(gx2, gy2,  0);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3i(gx2, gy1,  0);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3i(gx1, gy1,  0);
		gl.glEnd();	
		
		paintText(gl);
	}

	// if focus is being lost, make sure the button is unpressed
	public void onFocusLost(){
		if(!toggleButton){
			pressed = false;
		}
	}

	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_RELEASED:
					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_ENTER){						
						if(toggleButton){
							pressed = !pressed;
							if (listener != null){
								if (pressed){
									listener.actionPerformed(this, ACTION_CHECKED);					
								}else{
									listener.actionPerformed(this, ACTION_UNCHECKED);					
								}
							}
						}else{
							if (listener != null){
								listener.actionPerformed(this, ACTION_CLICKED);					
							}
						}
					}
					break;	
			}

		}else if (event instanceof MouseEvent){

			switch(event.getID()){
				case MouseEvent.MOUSE_PRESSED:
					if(toggleButton){
						pressed = !pressed;
						if (listener != null){
							if (pressed){
								listener.actionPerformed(this, ACTION_CHECKED);					
							}else{
								listener.actionPerformed(this, ACTION_UNCHECKED);					
							}
						}
					}else{
						pressed = true;
					}
					break;

				case MouseEvent.MOUSE_RELEASED:
					if(!toggleButton)
						pressed = false;

					if (listener != null){
						listener.actionPerformed(this, ACTION_CLICKED);					
					}
					break;	
			}
		}		
	}
}