
public class Enemy extends Actor
{
	Enemy(){}

	public void update(Actor a)
	{
	  //This enemy uses a tracking AI algorithm to chase after the player.
	  //He tracks the player so the player needs to be passed through to
	  //the method. 	
	  if(a.getX()>= super.getX())
	     this.setX() +=2;
	  if(a.getX()<= super.getX())
	     this.setX() -= 2;
	  if(a.getY()>= super.getY())
	     this.setY() +=2;
	  if(a.getY<= super.getY())
	     this.setY() -=2;
	
	}
	
	public void draw()
	{
	 System.out.println("The Enemy has been drawn");
	}
	public void logDebugString(String s)
	{
		System.out.println(s);
	}
}