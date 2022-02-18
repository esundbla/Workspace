

// upper left corner of the screen is 0, 0 and the entire screen is 640x480
// every component is relative to the container it is in. 


import net.java.games.jogl.*;
import java.awt.event.*;


public class HComponent{

	protected int 	x, y, w, h;			

	public boolean 	visible = true,    // should paint get called for this component
					enabled = true,	   // should it receive input
					focusable = true;  // can gain focus 

	public	boolean focused,
					mouseOver;

	public HListener	listener; 		//Doesnt have to be here, can be moved down	
	public HContainer  container;


	public String 	text = "";
	public int 	xText, yText;

	public int textureId = -1;
	public int textureIdFont = -1;


	public HComponent(){
	}

	public void setBounds(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;		
	}


	public boolean contains(float px, float py){

		if (px >= container.x+x && px <= (container.x+x+w) && py >= container.y+y && py <= (container.y+y+h)){
			return true;
		}

		return false;	
	}


	public void processInputEvent(InputEvent event){
	}


	public void onFocusGained(){
		System.out.println("onFocusGained: " + this);
		focused = true;
	}

	public void onFocusLost(){
		System.out.println("onFocusLost: " + this);
		focused = false;
	}

	public void onMouseEntered(){
		System.out.println("onMouseEntered: " + this);
		mouseOver = true;
	}

	public void onMouseExited(){
		System.out.println("onMouseExited: " + this);
		mouseOver = false;
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
		
		paintText(gl);

	}
	
	public void paintText(GL gl){
		if(text.length() > 0){
			paintText(gl, container.x+x+xText, container.y+y+yText, text, text.length(), textureIdFont);
		}
	}

	public void paintText(GL gl, int x, int y, String string, int length, int font){


		//--- save 
		//gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		gl.glPushAttrib(GL.GL_CURRENT_BIT | GL.GL_COLOR_BUFFER_BIT);
		//------------------------------------------------------

		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		gl.glEnable(GL.GL_BLEND);


		gl.glBindTexture(GL.GL_TEXTURE_2D, font);

		int tx = x;
		int ty = y;

		int dim = 16;

		for(int i=0; i<string.length() && i<length; i++){

			float cx = ((string.charAt(i)-32)%16)/16.0f;
			float cy = ((string.charAt(i)-32)/16)/16.0f;



			gl.glBegin(GL.GL_QUADS);   

			gl.glTexCoord2f(cx,1.0f-cy-0.0625f);   			
			gl.glVertex3i(tx, ty+dim,  0);    //SW

			gl.glTexCoord2f(cx+0.0625f,1.0f-cy-0.0625f);  		
			gl.glVertex3i( tx+dim, ty+dim,  0); // SE

			gl.glTexCoord2f(cx+0.0625f,1.0f-cy); 
			gl.glVertex3i( tx+dim,  ty,  0);	// NE

			gl.glTexCoord2f(cx,1.0f-cy);  
			gl.glVertex3i(tx,  ty,  0); //NW

			gl.glEnd();	

			// causes the text to be really packed. this should technically be += 16
			tx += 10;
		}


		//---- restore OGL state

		gl.glPopAttrib(); 
	}	
}