
import net.java.games.jogl.*;
import java.awt.event.*;


public class HSlider extends HComponent{

	//---------  Actions  -------------
	public static final int ACTION_VALUE_CHANGED = 1;	
	//---------------------------------

	int textureIdIndicator = -1;

	int	indicatorWidth = 20;

	public float value = 0.5f;
	public float step  = 0.1f;


	public HSlider(){
	}

	public void paint(GL gl){
		int gx1 = container.x+x, 
			gx2 = container.x+x+w,
			gy1 = container.y+y,
			gy2 = container.y+y+h;

		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);			

		gl.glBegin(GL.GL_QUADS);       
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3i(gx1, gy2,  0);  
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3i(gx2, gy2,  0);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3i(gx2, gy1,  0);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3i(gx1, gy1,  0);
		gl.glEnd();	


		int indicatorX = gx1 + (int)((float)w*value)- indicatorWidth/2;


		gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdIndicator);			

		gl.glBegin(GL.GL_QUADS);       
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3i(indicatorX, gy2,  0);  
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3i(indicatorX+indicatorWidth, gy2,  0);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3i(indicatorX+indicatorWidth, gy1,  0);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3i(indicatorX, gy1,  0);
		gl.glEnd();	
	}

	public void processInputEvent(InputEvent event){
		System.out.println(".processInputEvent: " + event.toString());

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_RELEASED:
					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_PAGE_UP){
						value += step;	

						if (value > 1)
							value = 1;

						if (listener != null){
							listener.actionPerformed(this, ACTION_VALUE_CHANGED);					
						}
					}

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_PAGE_DOWN ){						
						value -= step;						

						if (value < 0)
							value = 0;

						if (listener != null){
							listener.actionPerformed(this, ACTION_VALUE_CHANGED);					
						}
					}
					break;	
			}

		}else if (event instanceof MouseEvent){

			switch(event.getID()){

				case MouseEvent.MOUSE_PRESSED:
				case MouseEvent.MOUSE_DRAGGED:
				case MouseEvent.MOUSE_RELEASED:


					// convert to 640x480 coordinate
					int mx = (int)((float)((MouseEvent)event).getX() / (float)HContainerManager.instance.windowWidth * 640);

					value = (float)(mx - (container.x+x)) / (float)w;

					if (value < 0)
						value = 0;
					else if (value > 1)
						value = 1;


					if (listener != null){
						listener.actionPerformed(this, ACTION_VALUE_CHANGED);					
					}
					break;	
			}
		}		
	}
}