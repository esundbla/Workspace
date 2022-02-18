
//Notes: either mous drag or mouse move is generated, not both

// a container is like a window that has components. 
// it keeps track of the focused component and routes the key/mouse events accordingly.

// --- Key events
// -if tab (CTRL since tab is not readable (why?))is pressed, it cycles the focus.
// -if a conponent has focus, then it forwards the key events to it
// Esc pops "this" container. 
//
//---- Mouse events
// mouse pressed changes teh focused component, if the target is "enabled"
// mouse released and mouse dragged go to the focused component
// mouse move goes to the component the mous its on 
// - regardless of teh focus, wether its being dragged, or ..., when a mouse moved, the components should get enter/exit 



//Notes:
// -this class can be changed so that it can have a list of shortcut keys


//-------
// TOCONSIDER: 
// -when esc, if a comp has focus, loose it first.
// -a container may need an init and close call backs
//  so that information such as the data of a text field can be extracted and used.
//  technically, the container still exists can teh game code an extract data 


import net.java.games.jogl.*;
import java.awt.event.*;
import java.util.*;


public class HContainer{

	protected ArrayList components;
	protected int focusedComponent;
	protected int mouseOverComponent;



	//TODO:   draw focus and cursor



	// this is used so that the released event is not passed to teh component 
	// that has the focus, if the corresponding pressed event was not sent to 
	// teh component. this can occur if the last componant that was pressed is 
	// disabled and hence did not gain focus.
	private HComponent 	lastComponentThatReceivedMousePressed; 


	public int x, y;


	public int 	xInitial,
				yInitial, 
				xDestination, 
				yDestination,
				xDelta, 
				yDelta;



	public HContainer(){
		components = new ArrayList();
		focusedComponent = -1;
		mouseOverComponent = -1;
	}


	public void addComponent(HComponent component){
		component.container = this;
		components.add(component);
	}

	public void paint(GL gl){
		for (int i=0; i<components.size(); i++){
			HComponent component = (HComponent)components.get(i);	
			if(component.visible){
				component.paint(gl);
			}
		}
	}


	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){

				//BUG? 
				case KeyEvent.KEY_RELEASED:

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_CONTROL){
						System.out.println("================= Container.VK_CONTROL ");
						cycleFocus();
						return;	
					}

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_ESCAPE){
						System.out.println("================= HContainerManager.VK_ESCAPE");	
						HContainerManager.instance.popContainer();
						return;	
					}

					//---- if other key, forward it:
					if (focusedComponent != -1){
						((HComponent)components.get(focusedComponent)).processInputEvent(event);
						return;
					}
					break;	


				case KeyEvent.KEY_PRESSED:
				case KeyEvent.KEY_TYPED:
					//---- if other key, forward it:
					if (focusedComponent != -1){
						((HComponent)components.get(focusedComponent)).processInputEvent(event);
						return;
					}
					break;
			}


		}else if (event instanceof MouseEvent){

			// convert to 640x480 coordinate
			int mx = (int)((float)((MouseEvent)event).getX() / (float)HContainerManager.instance.windowWidth * 640);
			int my = (int)((float)((MouseEvent)event).getY() / (float)HContainerManager.instance.windowHeight * 480);					


			switch(event.getID()){

				// since some components cannot gain focus, 
				// and release, clicked, and daragged events must be passed to the component on which teh mouse was pressed
				// there are senarios where the events should not get routed at all
				case MouseEvent.MOUSE_DRAGGED:

					checkMouseOver(mx, my);
					//NOTE: same forwarding model as Realeased/Clicked

				case MouseEvent.MOUSE_RELEASED:
				case MouseEvent.MOUSE_CLICKED:

					// if a button is down, send drag to the conponent that the mouse was pressed on
					if (lastComponentThatReceivedMousePressed != null){
						lastComponentThatReceivedMousePressed.processInputEvent(event);
						return;
					}
					break;


					// mouse moved is always passed to the component the mouse is on. (if the component is enabled) 
				case MouseEvent.MOUSE_MOVED:

					checkMouseOver(mx, my);				

					// forwards events 						
					if (mouseOverComponent != -1 && ((HComponent)components.get(mouseOverComponent)).enabled && ((HComponent)components.get(mouseOverComponent)).visible){
						((HComponent)components.get(mouseOverComponent)).processInputEvent(event);
						return;	
					}
					break;


					// may causes a focus change. components that are diabled cannot gain focus. components that are not focusable cannot gain focus
				case MouseEvent.MOUSE_PRESSED:

					lastComponentThatReceivedMousePressed = null;

					for(int i=components.size()-1; i>=0; i--){
						HComponent component = (HComponent)components.get(i);
						if (component.visible && component.enabled && component.contains(mx, my)){
							lastComponentThatReceivedMousePressed = component;

							if(component.focusable){
								setFocus(component);
							}
								
							component.processInputEvent(event);
							return;	
						}
					}
					break;	
			}

		}else {
			System.out.println("WARNING: event not recognized");	
		}				
	}

	public void setFocus(HComponent component){
		// see if we can set it as te focused component.
		if(component.enabled){

			for (int i=0; i<components.size(); i++){
				if (component == ((HComponent)components.get(i))){

					if (focusedComponent != -1){
						((HComponent)components.get(focusedComponent)).onFocusLost();
					}

					component.onFocusGained();
					focusedComponent = i;	
				}							
			}	
		}	
	}

	public void cycleFocus(){
		// if no one has focus, find the first focusable
		// else cycle to the next component
		if(focusedComponent == -1){
			for (int i=0; i<components.size(); i++){
				if (((HComponent)components.get(i)).visible && ((HComponent)components.get(i)).enabled && ((HComponent)components.get(i)).focusable){
					setFocus(((HComponent)components.get(i)));
				}							
			}	
		}else{

			// find in remaining
			boolean wrap = true;
			for (int i=focusedComponent+1; i<components.size(); i++){
				if (((HComponent)components.get(i)).visible && ((HComponent)components.get(i)).enabled && ((HComponent)components.get(i)).focusable){
					setFocus(((HComponent)components.get(i)));
					wrap = false;
					break;
				}
			}

			if (wrap){
				// wrapp around
				for (int i=0; i<focusedComponent; i++){
					if (((HComponent)components.get(i)).visible && ((HComponent)components.get(i)).enabled && ((HComponent)components.get(i)).focusable){
						setFocus(((HComponent)components.get(i)));
						break;
					}
				}
			}

		}

		System.out.println("cycleFocus: focusedComponent @ index: " + focusedComponent);	
	}

	private void checkMouseOver(int mx, int my){
		int mouseOverComponentNew = -1;

		for(int i=components.size()-1; i>=0; i--){
			HComponent component = (HComponent)components.get(i);
			if (component.enabled && component.visible && component.contains(mx, my)){
				mouseOverComponentNew = i;
			}
		}	

		// mouseOver notification
		if (mouseOverComponent != mouseOverComponentNew){
			if(mouseOverComponent != -1){
				if (((HComponent)components.get(mouseOverComponent)).enabled){
					((HComponent)components.get(mouseOverComponent)).onMouseExited();	
				}
			}

			if(mouseOverComponentNew != -1){
				if (((HComponent)components.get(mouseOverComponentNew)).enabled){
					((HComponent)components.get(mouseOverComponentNew)).onMouseEntered();	
				}
			}

			mouseOverComponent = mouseOverComponentNew;
		}
	}

	public void onShow(){
		System.out.println("HContainer onShow: " + this);	

	}

	public void onHide(){
		System.out.println("HContainer onHide: " + this);	

	}


	// when focus is gained, check to see who should have mouseOver 
	public void onFocusGained(){
		System.out.println("HContainer onFocusGained: " + this);

		if(focusedComponent != -1)
			((HComponent)components.get(focusedComponent)).onFocusGained();

		int cx = HContainerManager.instance.convertMouseX(HContainerManager.instance.xMouse);
		int cy = HContainerManager.instance.convertMouseY(HContainerManager.instance.yMouse);

		checkMouseOver(cx, cy); 				
	}

	// tell the mouseOver and focus components that they are loosing them. 
	// when the container gains focus it will notify the component that had the focus to gain focus again
	public void onFocusLost(){
		System.out.println("HContainer onFocusLost: " + this);

		if(focusedComponent != -1){
			((HComponent)components.get(focusedComponent)).onFocusLost();
			// do not reset focusedComponent. keep track of it		
		}

		if(mouseOverComponent != -1){
			((HComponent)components.get(mouseOverComponent)).onMouseExited();
			// lets forget about who has mouseOver, when the focus is gained, the mouse might have moved
			// which means some one else will need to have mouseOver
			mouseOverComponent = -1;
		}
	}

}