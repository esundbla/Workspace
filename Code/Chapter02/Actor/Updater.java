//Updater.java

//This public interface creates and lists the functions an entity needs to update itself as well
// as to process collision events againsts itself or others.

public interface Updater{
	
	public boolean collidesWith(Updater other);
	public void handleCollisions();
	public void update();
	
}//end Updateable.java