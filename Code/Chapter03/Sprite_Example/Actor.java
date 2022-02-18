//Actor.java

//The elemental core set of attributes for any java game. 
package book.fullscreen;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;

class Actor implements Updater,Loggable{
	
	static final int ALIVE = 1;
	static final int DEAD  = 2;
	
	private int state;
	private int x;
	private int y;
	private int height;
	private int width;
	
	//default constructor.
	Actor(){}
	
	Actor(int i, int j)
	{
		x=i;
		y=j;
	}
	
	
	//accessor functions for updating the entity
	public void setState(int curr_state){
		state = curr_state;
	}
	
	public int getState(){
		return state;
	}
	
	public void setX(int curr_x){
		x= curr_x;
	}
	public int getX(){
		return x;
	}
	
	public void setY(int curr_y){
		y = curr_y;
	}
	
	public int getY(){
		return y;
	}
	
	public void setHeight(int curr_height){
		height = curr_height;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setWidth(int curr_width){
		width = curr_width;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void update(){
		
		System.out.println("I have been updated");
		}
	public boolean collidesWith(Updater other)
	{
		boolean bool = true;
		return  bool;
	}
	public void handleCollisions(){}
	
	//drawable interface
	public boolean isVisible()
	{
      	boolean bool = true;
		return  bool;
	}
	
	//loggable
	public void logDebugString(Object s){}
 	public void logString(Object s){}
	
	
}//end Actor.java