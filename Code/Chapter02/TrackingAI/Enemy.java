package book.fullscreen;

public class Enemy extends Actor
{
	Enemy(){}

	public void update(Player a)
	{
        //This enemy uses a tracking AI algorithm to chase after the player.
      //He tracks the player so the player needs to be passed through to
      //the method.     
      if(a.getX()>= this.getX())
         this.setX(this.getX()-1);
      if(a.getX()<= this.getX())
         this.setX(this.getX()+1);
      if(a.getY()<= this.getY())
        this.setY(this.getY()+1);
      if(a.getY()>= this.getY())
        this.setY(this.getY()-1);
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