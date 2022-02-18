

import net.java.games.jogl.*;
import java.awt.event.*;

public class HTextComponent extends HComponent{

	//	int				carretPos;	

	public HTextComponent(){
	}


	public void paint(GL gl){
	}


	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_TYPED:
					//-- NOTE: For KEY_TYPED events, the keyCode is VK_UNDEFINED				
					//System.out.println("KEY_TYPED: " + ((KeyEvent)event).paramString());

					// if back space
					if(((KeyEvent)event).getKeyChar() == '\b'){

						if (text.length() > 0){
							text = text.substring(0, text.length() - 1);
						}

					}else if(((KeyEvent)event).getKeyChar() == '\n' || ((KeyEvent)event).getKeyChar() == '\r'){
						// Ignore it

					}else{
						//NOTE: 10 is assumed to be teh spacing of teh charachters
						if( (text.length()+1) * 10 + x > (x+w)){
							return;
						}

						if (!Character.isIdentifierIgnorable(((KeyEvent)event).getKeyChar())){
							text += ((KeyEvent)event).getKeyChar();
						}else{
							System.out.println("ignorable character: " + ((KeyEvent)event).getKeyChar());	
						}
					}

					break;	
			}

		}
	}
}