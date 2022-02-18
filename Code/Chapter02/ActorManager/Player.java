public class Player extends Actor
{
	Player(){}

	public void update()
	{
		
	System.out.println("The Player has been updated");
	}
	
	public void draw()
	{
	 System.out.println("The Player has been drawn");
	}
	public void logDebugString(String s)
	{
		System.out.println(s);
	}
}