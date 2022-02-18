
import net.java.games.jogl.*;
import java.awt.event.*;
import java.util.*;


public class HContainerManager{

	static HContainerManager instance;

	private ArrayList 	containerStack; // the top container is always the active one
	private HContainer	nextPush;

	int xMouse, yMouse;
	int windowWidth;
	int windowHeight;


	int textureIdMouseOver = -1;
	int textureIdFocus = -1;	


	static final int 	STATE_IDLE 			= 0,
		STATE_PUSH_ENTER 	= 1,
		STATE_PUSH		 	= 2,
		STATE_PUSH_EXIT 	= 3,
		STATE_POP_ENTER  	= 4,
		STATE_POP		  	= 5,
		STATE_POP_EXIT  	= 6;

	int state = STATE_IDLE;



	static private float matrixProjectionOld[] = new float[16];



	public HContainerManager(){
		instance = this;
		containerStack = new ArrayList();
	}

	public HContainer getTopContainer(){
		if(containerStack.size() > 0){
			return 	(HContainer)containerStack.get(containerStack.size()-1);
		}

		return null;
	}	

	public void pushContainer(HContainer container){	
		if (nextPush != null){
			System.out.println("Warning: cannot push container while another one is being pushed");	
			return;
		}
		
		boolean exists = false;
		for (int i=0; i<containerStack.size(); i++){
			if (containerStack.get(i) == container){
				exists = true;
				break;			
			}
		}

		if(exists){
			System.out.println("Warning: cannot push container since it is already in teh stack");	
			return;
		}


		nextPush = container;
	}

	public void popContainer(){
		if (state != STATE_IDLE){
			System.out.println("Warning: cannot pop container while another one is being popped");	
			return;
		}
		state=STATE_POP_ENTER;
	}

	public void paint(GL gl){		

		//----- store current OGL state
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS); 
		gl.glPushMatrix();

		gl.glGetFloatv(GL.GL_PROJECTION_MATRIX, matrixProjectionOld);



		//----- set OGL state for GUI rendering
		gl.glEnable(GL.GL_TEXTURE_2D); 
		gl.glDisable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_DEPTH_TEST);     		

		// set the frustum 
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0,640,480,0,-1,1);
		gl.glMatrixMode(GL.GL_MODELVIEW);

		gl.glLoadIdentity();


		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);  // 0.5 needed for GL_ONE_MINUS_SRC_ALPHA
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_BLEND);


		//---------------------------------------------------------

		for (int i=0; i<containerStack.size(); i++){
			HContainer container = (HContainer)containerStack.get(i);
			container.paint(gl);
		}

		paintFocus(gl);


		//- - - - - - - - - - - - - - - - - - - - - - - - -
		HContainer top = getTopContainer();

		switch(state){
			case STATE_IDLE:
				if (nextPush != null){
					state = STATE_PUSH_ENTER;
				}
				break;

			case STATE_PUSH_ENTER:
				//store pointer and do these in the enter state
				if(top != null)
					top.onFocusLost();

				nextPush.x = nextPush.xInitial;
				nextPush.y = nextPush.yInitial;	
				nextPush.onShow();

				containerStack.add(nextPush);
				nextPush = null;

				state = STATE_PUSH;
				break;

			case STATE_PUSH:
				//System.out.println("STATE_PUSH_UPDATE, top.x: " + top.x + ", top.y: " + top.y);	

				top.x+= top.xDelta;
				top.y+= top.yDelta;	

				if (top.x == top.xDestination && top.y == top.yDestination){
					state = STATE_PUSH_EXIT;	
				}
				break;	

			case STATE_PUSH_EXIT:
				top.onFocusGained();
				state = STATE_IDLE;	
				break;

			case STATE_POP_ENTER:
				top.onFocusLost();
				state = STATE_POP;
				break;

			case STATE_POP:
				//System.out.println("STATE_POP_UPDATE, top.x: " + top.x + ", top.y: " + top.y);	

				top.x -= top.xDelta;
				top.y -= top.yDelta;	

				if (top.x == top.xInitial && top.y == top.yInitial){
					state = STATE_POP_EXIT;	
				}
				break;	

			case STATE_POP_EXIT:
				containerStack.remove(containerStack.size()-1);
				top.onHide();

				if(containerStack.size() > 0)
					((HContainer)containerStack.get(containerStack.size()-1)).onFocusGained();

				state = STATE_IDLE;
				break;

		}

		//---------------------------------------------------------


		//---- restore OGL state
		gl.glPopMatrix();
		gl.glPopAttrib();

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadMatrixf(matrixProjectionOld);

		gl.glMatrixMode(GL.GL_MODELVIEW);
	}

	public void paintFocus(GL gl){

		HContainer top = getTopContainer();

		if(top == null)
			return;
			
		gl.glPushAttrib(GL.GL_CURRENT_BIT | GL.GL_COLOR_BUFFER_BIT);

		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.2f); 
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		gl.glEnable(GL.GL_BLEND);
			

		//draw mouseOver and focus owner
		if(top.focusedComponent != -1){

			HComponent component = (HComponent)(top.components.get(top.focusedComponent)); 
			int gx1 = top.x+component.x, 
				gx2 = top.x+component.x+component.w,
				gy1 = top.y+component.y,
				gy2 = top.y+component.y+component.h;

			gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdFocus);			

			gl.glBegin(GL.GL_QUADS);       
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3i(gx1, gy2,  0);  
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3i(gx2, gy2,  0);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3i(gx2, gy1,  0);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3i(gx1, gy1,  0);
			gl.glEnd();	
		}

		if(top.mouseOverComponent != -1){

			HComponent component = (HComponent)(top.components.get(top.mouseOverComponent)); 
			int gx1 = top.x+component.x, 
				gx2 = top.x+component.x+component.w,
				gy1 = top.y+component.y,
				gy2 = top.y+component.y+component.h;

			gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdMouseOver);			

			gl.glBegin(GL.GL_QUADS);       
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3i(gx1, gy2,  0);  
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3i(gx2, gy2,  0);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3i(gx2, gy1,  0);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3i(gx1, gy1,  0);
			gl.glEnd();	
		}
		
		gl.glPopAttrib(); 
		
	}

	public void processInputEvent(InputEvent event){
//		System.out.println("HContainerManager.processInputEvent: " + event.toString());

		if(event instanceof MouseEvent){	
			xMouse = ((MouseEvent)event).getX();
			yMouse = ((MouseEvent)event).getY();
			
//			System.out.println("xMouse: " + xMouse + ", yMouse: " + yMouse);
		}


		if (containerStack.size() == 0 || state != STATE_IDLE)
			return;	

//		System.out.println("xMouse: " + xMouse + ", yMouse: " + yMouse);


		HContainer topContainer = (HContainer)containerStack.get(containerStack.size() - 1);
		topContainer.processInputEvent(event);		
	}

	public int convertMouseX(int xMouse){
		return (int)((float)xMouse / (float)HContainerManager.instance.windowWidth * 640);	
	}

	public int convertMouseY(int yMouse){
		return (int)((float)yMouse/ (float)HContainerManager.instance.windowHeight * 480);	
	}

}

